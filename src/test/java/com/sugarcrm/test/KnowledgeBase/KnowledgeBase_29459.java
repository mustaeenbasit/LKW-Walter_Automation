package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29459 extends SugarTest {
	FieldSet customData = new FieldSet();
	DataSource kbRecordData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		kbRecordData = testData.get(testName + "_" + sugar().knowledgeBase.moduleNamePlural);

		// Login as an Admin
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a KB1 with Status=Draft, also has Expiration Date is past, e.g. July 1, 2015, leave Published Date blank.
		// Create another KB2 with Status=Draft, also has Expiration Date and Published Date is past.
		// As we are not able to create empty date via API, hence create KB records from UI
		sugar().knowledgeBase.navToListView();
		for(int i = 0; i < kbRecordData.size(); i++) {
			sugar().knowledgeBase.listView.create();
			sugar().knowledgeBase.createDrawer.showMore();
			sugar().knowledgeBase.createDrawer.getEditField("name").set(kbRecordData.get(i).get("name"));
			sugar().knowledgeBase.createDrawer.getEditField("status").set(kbRecordData.get(i).get("status"));
			sugar().knowledgeBase.createDrawer.getEditField("date_expiration").set(kbRecordData.get(i).get("date_expiration"));
			sugar().knowledgeBase.createDrawer.getEditField("date_publish").set(kbRecordData.get(i).get("date_publish"));
			sugar().knowledgeBase.createDrawer.save();
		}

		// Add 'Publish Date' in the List View Layout
		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Studio -> KB -> Layouts -> List View
		// TODO: VOOD-542
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#Hidden li[data-name='active_date']").dragNDrop(new VoodooControl("td", "id", "Default"));
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Canceling mass update should re-set back the orignal Published Date 
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29459_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB list view, select above both KB, mass update to change Status = Published.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.massUpdate();
		sugar().knowledgeBase.massUpdate.getControl("massUpdateField02").set(customData.get("status"));
		sugar().knowledgeBase.massUpdate.getControl("massUpdateValue02").set(customData.get("published"));

		// Define controls for publish date
		// TODO: VOOD-1489
		VoodooControl firstKBRecord = new VoodooControl("div", "css", "tr .fld_active_date div");
		VoodooControl secondKBRecord = new VoodooControl("div", "css", "tr:nth-child(2) .fld_active_date div");
		String todaysData = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"); 

		// Click on Update button
		sugar().knowledgeBase.massUpdate.getControl("update").click();

		// Verify that Warning message appears instead of error message saying: "The Expiration Date occur on a date before the Publish Date. Do you wish to continue without modify a Expiration Date?" 
		sugar().alerts.getError().assertExists(true);
		sugar().alerts.getError().assertContains(customData.get("warningMessage"), true);

		// Verify that the "Published Date" is not set to today's date
		firstKBRecord.assertEquals(todaysData, false);
		secondKBRecord.assertEquals(todaysData, false);

		// Cancel the warning message
		sugar().alerts.getError().closeAlert();

		// Click on Cancel in the mass update
		sugar().knowledgeBase.massUpdate.cancelUpdate();

		// Verify that KB1's published date is set to the past date and KB2 is set back to blank.
		firstKBRecord.assertEquals(kbRecordData.get(1).get("date_publish"), true);
		secondKBRecord.assertEquals(kbRecordData.get(0).get("date_publish"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}