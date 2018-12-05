package com.sugarcrm.test.KnowledgeBase;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29382 extends SugarTest {
	FieldSet KBData = new FieldSet();
	
	public void setup() throws Exception {
		KBData = testData.get(testName+"_KBData").get(0);
		sugar().knowledgeBase.api.create(KBData);

		// Login as admin user
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Add 'Publish Date' in the List View Layout
		// TODO: VOOD-542
		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Studio -> KB -> Layouts -> List View
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(new VoodooControl("td", "id", "Default"));
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#Hidden li[data-name='exp_date']").dragNDrop(new VoodooControl("td", "id", "Default"));
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that not allow mass update status = published for the KB with Expiration Date in past
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29382_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout from admin user and login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		FieldSet customData = testData.get(testName).get(0);

		// Go to KB list view, select above both KB, mass update to change Status = Published.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();
		VoodooControl massUpdateFieldCtrl = sugar().knowledgeBase.massUpdate.getControl("massUpdateField02");
		VoodooControl massUpdateValueCtrl = sugar().knowledgeBase.massUpdate.getControl("massUpdateValue02");
		massUpdateFieldCtrl.set(customData.get("status"));
		massUpdateValueCtrl.set(customData.get("published"));

		// Click on Update button
		sugar().knowledgeBase.massUpdate.getControl("update").click();

		// Verify that Warning message appears instead of error message saying: "The Expiration Date occur on a date before the Publish Date. Do you wish to continue without modify a Expiration Date?" 
		sugar().alerts.getError().assertExists(true);
		sugar().alerts.getError().assertContains(customData.get("warningMessage"), true);

		// Define controls for publish date
		// TODO: VOOD-1489
		VoodooControl publishDateForKBRecord = new VoodooControl("div", "css", "tr .fld_active_date div");
		String todaysData = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"); 

		// Verify that the "Published Date" is not set to today's date
		publishDateForKBRecord.assertEquals(todaysData, false);

		// Cancel the warning message
		sugar().alerts.getError().closeAlert();

		// Go to in line edit and Select a date as today, status = Published
		// TODO: VOOD-1489
		sugar().knowledgeBase.listView.editRecord(1);
		sugar().knowledgeBase.listView.getEditField(1, "date_expiration").set(todaysData);
		sugar().knowledgeBase.listView.saveRecord(1);

		// Click on Update button
		sugar().knowledgeBase.massUpdate.update();

		// Verify that Published Date is the date you have chosen and status = Published
		// TODO: VOOD-1489
		new VoodooControl("div", "css", ".fld_active_date.list div").assertEquals(KBData.get("date_publish"), true);
		new VoodooControl("div", "css", ".fld_exp_date.list div").assertEquals(todaysData, true);
		new VoodooControl("span", "css", ".fld_status.list span").assertEquals(customData.get("published"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}