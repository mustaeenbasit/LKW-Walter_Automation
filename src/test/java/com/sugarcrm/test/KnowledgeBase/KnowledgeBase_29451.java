package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29451 extends SugarTest {
	UserRecord myUserRecord;

	public void setup() throws Exception {
		sugar().login();
		
		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a custom user
		// TODO: VOOD-1200
		myUserRecord = (UserRecord) sugar().users.create();

		// Logout from admin user and Login as custom user
		sugar().logout();
		myUserRecord.login();
	}

	/**
	 * Verify that auto-re-load in "Most Usefu Published KB" Dashlet in record view  
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29451_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Create a new published KB
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customData.get("name"));
		sugar().knowledgeBase.createDrawer.getEditField("status").set(customData.get("status"));
		sugar().knowledgeBase.createDrawer.save();

		// Navigate to the record view of the created KB record
		sugar().knowledgeBase.listView.clickRecord(1);
		VoodooUtils.waitForReady(); // Extra wait needed

		// Select My Dashboard from the right hand side
		VoodooControl dashboardTitle = sugar().knowledgeBase.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(customData.get("dashboardTitle"), true))
			sugar().dashboard.chooseDashboard(customData.get("dashboardTitle"));

		// Add "Most Useful Published Knowledge Base Articles" Dashlet in record view. 
		sugar().knowledgeBase.dashboard.edit();
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(3, 1);

		// Add a Dashlet -> select "Most Useful Published Knowledge Base Articles"
		// TODO: VOOD-960, VOOD-1645
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customData.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Save
		new VoodooControl("a", "css", ".layout_KBContents.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Verify that no data in that Dashlet in record view
		// TODO: VOOD-960, VOOD-1645
		VoodooControl noDataAvailableCtrl = new VoodooControl("div", "css", ".row-fluid.sortable:nth-of-type(3) .dashlet-container:nth-of-type(1) .dashlet-content .block-footer");
		noDataAvailableCtrl.assertEquals(customData.get("noDataAvailable"), true);

		// Click on Useful button for that KB
		// TODO: VOOD-1783
		new VoodooControl("a", "css", "a[data-action='useful']").click();
		VoodooUtils.waitForReady();

		// Verify that the KB should appear in the "Most Useful Published KB" Dashlet now.
		// TODO: VOOD-960, VOOD-1645
		new VoodooControl("div", "css", ".row-fluid.sortable:nth-of-type(3) .dashlet-container:nth-of-type(1) .fld_name div").assertEquals(customData.get("name"), true);

		// Click on UN-Useful button for that KB
		// TODO: VOOD-1783
		new VoodooControl("a", "css", "[data-action='notuseful']").click();

		// Verify that the KB should disappear in the "Most Useful Published KB" Dashlet now
		noDataAvailableCtrl.assertEquals(customData.get("noDataAvailable"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}