package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24373 extends SugarTest {
	TaskRecord myTask;

	public void setup() throws Exception {
		// Create an Opportunity and a Task record
		sugar().opportunities.api.create();
		myTask = (TaskRecord) sugar().tasks.api.create();

		// Login
		sugar().login();

		// Create a custom user
		// TODO: VOOD-1200 - Authentication failed on calling Users default data
		UserRecord customUser = (UserRecord) sugar().users.create();

		// Logout from admin user and Login as the custom user
		sugar().logout();
		customUser.login();

		// Navigates to the Opportunity record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Task records which are created for an opportunity exist
		sugar().opportunities.recordView.subpanels.get(sugar().tasks.moduleNamePlural).linkExistingRecord(myTask);

		// Active and Inactive Tacks Dashlets exist on Opportunity Dashboard
		// In the intelligence pane on the right (i.e RHS pane), add a new Active Tasks dashlet
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		FieldSet customFS = testData.get(testName).get(0);
		if(!dashboardTitle.queryContains(customFS.get("dashboardTitle"), true)) {
			sugar().dashboard.chooseDashboard(customFS.get("dashboardTitle"));
			sugar().opportunities.dashboard.edit();
			sugar().opportunities.dashboard.addRow();
			sugar().opportunities.dashboard.addDashlet(7, 1);
		}

		// TODO: VOOD-960 - Dashlet selection
		// TODO: VOOD-1645 - Need to update method(s) in 'Dashboard.java' for Edit page.
		// Add a dashlet -> select "In-active Tasks"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customFS.get("inactiveTasks"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Save
		new VoodooControl("a", "css", ".layout_Opportunities.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Edit Task_Verify that the completed task is displayed in Inactive Tasks dashlet
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24373_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Opportunities" tab on navigation bar
		sugar().opportunities.navToListView();

		// Click the name of an existing opportunity in "Opportunities" list view
		sugar().opportunities.listView.clickRecord(1);

		// Define controls for the dashlets
		// TODO: VOOD-960 - Dashlet selection
		VoodooControl activeTaskDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(4) .dashlet-container");
		VoodooControl inactiveTaskDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(7) .dashlet-container");

		// Go to Active Tasks Dashlet and click 'Completed' button
		activeTaskDashletCtrl.scrollIntoViewIfNeeded(false);
		new VoodooControl("span", "css", activeTaskDashletCtrl.getHookString() + " .dashlet-tabs-row .dashlet-tab:nth-child(2) span").click();
		VoodooUtils.waitForReady();
		new VoodooControl("i", "css", activeTaskDashletCtrl.getHookString() + " .active-tasks i").scrollIntoViewIfNeeded(true);
		sugar().alerts.getWarning().confirmAlert();
		
		// TODO: remove this block of code after TR-8565 is fixed (To refresh Inactive dashlets)
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Verify that the task is displayed in 'Inactive Tasks' Dashlet
		inactiveTaskDashletCtrl.scrollIntoView();
		new VoodooControl("span", "css", inactiveTaskDashletCtrl.getHookString() + " .dashlet-tabs-row .dashlet-tab:nth-child(2) span").click();
		VoodooUtils.waitForReady();
		new VoodooControl("p", "css", inactiveTaskDashletCtrl.getHookString() + " .tab-content .listed p").assertContains(myTask.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}