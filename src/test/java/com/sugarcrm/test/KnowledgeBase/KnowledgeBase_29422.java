package com.sugarcrm.test.KnowledgeBase;

import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29422 extends SugarTest {
	KBRecord myKBRecord;

	public void setup() throws Exception {
		FieldSet KBData = testData.get(testName+"_KBData").get(0);
		sugar().cases.api.create();
		myKBRecord = (KBRecord) sugar().knowledgeBase.api.create(KBData);

		// Login
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Studio-> Cases-> sub panel-> Knowledge base-> Add "Published Date" and "Expiration Date" in the Default list
		// TODO: VOOD-1506, VOOD-542
		new VoodooControl("a", "id", "studiolink_Cases").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons tr:nth-child(2) td:nth-child(2) tr:nth-child(1) a").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultColumn = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(defaultColumn);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#Hidden li[data-name='exp_date']").dragNDrop(defaultColumn);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that correct handling when save a KB by changing status to published while expiration date is past in KB sub panel
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29422_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Open one Case record
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		StandardSubpanel kbSubpanelCtrl = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);

		// In KB sub panel, link an existing KB (draft=status, expiration date is past)
		kbSubpanelCtrl.linkExistingRecord(myKBRecord);

		// Edit the KB record by changing status to published
		kbSubpanelCtrl.editRecord(1);
		kbSubpanelCtrl.getEditField(1, "status").set(customData.get("published"));
		kbSubpanelCtrl.saveAction(1);

		// Verify that a red color error message "Error The expiration date can not be before date of publishing" appear.
		sugar().alerts.getError().assertEquals(customData.get("errorMessage"), true);
		sugar().alerts.getError().closeAlert();

		// Verify that "Expiration Date" is marked as red
		// TODO: VOOD-1445 and VOOD-1755
		new VoodooControl("span", "css", ".fld_exp_date.edit.error").assertExists(true);

		// Tomorrow date
		String tomorrowDate = DateTime.now().plusDays(1).toString("MM/dd/yyyy");

		// Change "Expiration Date" become tomorrow
		VoodooDate expDate = new VoodooDate("input", "css", ".fld_exp_date.edit input");
		expDate.scrollIntoViewIfNeeded(false);
		expDate.set(tomorrowDate);
		VoodooUtils.waitForReady();

		// Click on Save button.
		kbSubpanelCtrl.saveAction(1);

		// The KB is saved with Expiration Date is tomorrow, Published Date is today. Status is Published
		kbSubpanelCtrl.getDetailField(1, "status").assertEquals(customData.get("published"), true);

		// TODO: VOOD-1489
		new VoodooControl("div", "css", ".list.fld_exp_date div").assertContains(tomorrowDate, true);
		new VoodooControl("div", "css", ".fld_active_date.list div").assertEquals(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
