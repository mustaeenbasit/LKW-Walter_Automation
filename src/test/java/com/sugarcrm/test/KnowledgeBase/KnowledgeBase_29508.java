package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29508 extends SugarTest {
	DataSource kbData = new DataSource();
	UserRecord myUser;

	public void setup() throws Exception {
		kbData = testData.get(testName);

		// Login
		sugar().login();

		// Create a custom user
		// TODO: VOOD-1200
		myUser = (UserRecord) sugar().users.create();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create 2 published KB with several minutes apart
		// TODO: VOOD-444
		sugar().knowledgeBase.create(kbData);
		sugar().alerts.getSuccess().closeAlert();

		// Logout from admin user and login as custom user
		sugar().logout();
		myUser.login();
	}

	/**
	 * Verify that should be ordered by date created (recently created first) when 2 KB has same score&same positive vote&same frequency
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29508_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dashletsData = testData.get(testName + "_Dashboard").get(0);

		// Define Useful button control
		// TODO: VOOD-1783
		VoodooControl usefulnessBtnCtrl = new VoodooControl("a", "css", "a[data-action='useful']");

		// Open each KB record and click on Useful button
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		usefulnessBtnCtrl.click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.recordView.gotoNextRecord();
		usefulnessBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate back to KB list view
		sugar().knowledgeBase.navToListView();

		// In KB list view, make sure you are able to see 'Most Useful Published KB Articles' Dashlet
		// Switch to My Dashboard
		VoodooControl dashboardTitle = sugar().knowledgeBase.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(dashletsData.get("myDashboard"), true))
			sugar().dashboard.chooseDashboard(dashletsData.get("myDashboard"));

		// Define Controls for 'Most Useful Published KB Articles' dashlets
		// TODO: VOOD-960
		VoodooControl usefulPublishedKbDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(2) .dashlet-container:nth-of-type(1)");
		VoodooControl usefulPublishedKbRecordViewDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(3) .dashlet-container:nth-of-type(1)");
		VoodooControl firstRecordInDashletCtrl = new VoodooControl("li", "css", usefulPublishedKbDashletCtrl.getHookString() + " .dashlet-content .kbcontents li");
		VoodooControl secondRecordInDashletCtrl = new VoodooControl("li", "css", usefulPublishedKbDashletCtrl.getHookString() + " .dashlet-content .kbcontents li:nth-child(2)");
		VoodooControl thirdRecordInDashletCtrl = new VoodooControl("li", "css", usefulPublishedKbDashletCtrl.getHookString() + " .dashlet-content .kbcontents li:nth-child(3)");

		// Verify that The KB that is created first is listed in the 2nd, the KB created at the second is listed in the first in the Dashlet 'Most Useful Published KB'.
		firstRecordInDashletCtrl.assertContains(kbData.get(1).get("name"), true);
		secondRecordInDashletCtrl.assertContains(kbData.get(0).get("name"), true);

		// Create a 3rd published KB
		FieldSet kbName = new FieldSet();
		kbName.put("name", testName);
		kbName.put("status", kbData.get(0).get("status"));
		sugar().knowledgeBase.create(kbName);
		sugar().alerts.getSuccess().closeAlert();

		// Open the 3rd KB record view and check out the "Most Useful Published KB" Dashlet too
		sugar().knowledgeBase.listView.clickRecord(1);

		// Switch to My Dashboard
		if(!dashboardTitle.queryContains(dashletsData.get("myDashboard"), true))
			sugar().dashboard.chooseDashboard(dashletsData.get("myDashboard"));

		// Add 'Most Useful Published KB Articles' Dashlet
		sugar().knowledgeBase.dashboard.edit();
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(3, 1);

		// TODO: VOOD-960, VOOD-1645
		// Add a dashlet -> select "Most Useful Published Knowledge Base Articles"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashletsData.get("usefulPublishedKbDashlet"));
		VoodooUtils.waitForReady(); // Wait Needed
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Save
		new VoodooControl("a", "css", ".layout_KBContents.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the Dashboard
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Verify that the KB that is created first is listed in the 2nd, the KB created at the second is listed in the first.The 3rd KB should not listed in the Dashlet
		new VoodooControl("li", "css", usefulPublishedKbRecordViewDashletCtrl.getHookString() + " .dashlet-content .kbcontents li").assertContains(kbData.get(1).get("name"), true);
		new VoodooControl("li", "css", usefulPublishedKbRecordViewDashletCtrl.getHookString() + " .dashlet-content .kbcontents li:nth-child(2)").assertContains(kbData.get(0).get("name"), true);
		new VoodooControl("li", "css", usefulPublishedKbRecordViewDashletCtrl.getHookString() + " .dashlet-content .kbcontents li:nth-child(3)").assertExists(false);

		// Also verifying in the list view dashlet
		sugar().knowledgeBase.navToListView();
		firstRecordInDashletCtrl.assertContains(kbData.get(1).get("name"), true);
		secondRecordInDashletCtrl.assertContains(kbData.get(0).get("name"), true);
		thirdRecordInDashletCtrl.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}