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

public class KnowledgeBase_29488_a extends SugarTest {
	KBRecord kbRecord;
	
	public void setup() throws Exception {
		sugar().cases.api.create();

		// Login
		sugar().login();	

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Creating a KB record with valid expiration date and published date blank
		FieldSet fs = new FieldSet();
		fs.put("name", sugar().knowledgeBase.getDefaultData().get("name"));
		fs.put("date_expiration", DateTime.now().plusMonths(2).toString("MM/dd/yyyy"));
		fs.put("date_publish", "");
		kbRecord = (KBRecord) sugar().knowledgeBase.create(fs);
		
		// Navigate to Admin > studio > Cases > sub panel > KB sub panel > Add publish date to default column 
		// TODO: VOOD-542 and VOOD-1511
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studiolink_Cases").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons tr:nth-child(2) td:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		VoodooControl languageCtrl = new VoodooControl("li", "css",  "[data-name='language']");
		new VoodooControl("li", "css", "[data-name='active_date']").dragNDrop(languageCtrl);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css",  "[data-name='exp_date']").dragNDrop(languageCtrl);
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that correct handling when edit a KB by changing status to Approved in Cases sub panel
	 * KnowledgeBase_29488_a : Verify the KB with valid expiration date and published date blank in Cases sub panel gives
	 * correct results when edited and status changed to Approved
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29488_a_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to cases sub panel
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		StandardSubpanel kbSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		
		// Adding KB record to Cases sub panel
		kbSubpanel.linkExistingRecord(kbRecord);

		// Updating the status of KB record
		FieldSet fs = testData.get(testName).get(0);
		kbSubpanel.editRecord(1);
		kbSubpanel.getEditField(1, "status").set(fs.get("status"));
		kbSubpanel.saveAction(1);

		// Asserting the Alert message
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().assertContains(fs.get("alertMessage"), true);
		sugar().alerts.getWarning().cancelAlert();

		// Cancel action and re navigate
		kbSubpanel.cancelAction(1);
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		kbSubpanel.scrollIntoViewIfNeeded(false);
		kbSubpanel.expandSubpanel();
		kbSubpanel.scrollIntoViewIfNeeded(false);
		kbSubpanel.editRecord(1);

		// Define controls for publish date calendar and publish date field
		// TODO: VOOD-1489
		VoodooControl newPublishDate = new VoodooControl("div", "css", "tr .fld_active_date div");
		String publishDate = DateTime.now().plusMonths(1).toString("MM/dd/yyyy"); 

		// Asserting the publish date calendar is visible and setting future date from calendar
		new VoodooDate("input", "css", ".fld_active_date.edit input").scrollIntoViewIfNeeded(false);
		new VoodooDate("input", "css", ".fld_active_date.edit input").set(publishDate);
		kbSubpanel.saveAction(1);

		// Asserting the publish date field after save
		newPublishDate.assertContains(publishDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 