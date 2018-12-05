package com.sugarcrm.test.dashlets;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20293_Opportunities extends SugarTest {
	UserRecord customUser;
	FieldSet dashboardData = new FieldSet();
	DataSource tasksDS = new DataSource();
	ArrayList<Record> taskRecords = new ArrayList<Record>();
	VoodooControl dashboardTitle, saveDashBoardBtnCtrl;

	public void setup() throws Exception {
		dashboardData = testData.get(testName).get(0);
		tasksDS = testData.get(testName + "_" + sugar.tasks.moduleNamePlural);
		sugar.opportunities.api.create();

		// Create two tasks - 1) Active: Set old Due Date 2) Inactive: Status: Deferred
		taskRecords = sugar.tasks.api.create(tasksDS);

		// Login
		sugar.login();

		// Create custom user
		customUser = (UserRecord) sugar.users.create();
		VoodooUtils.waitForReady(); // Needed extra wait

		// Logout from the admin user and login as custom user
		sugar.logout();
		customUser.login();

		// Link the created Tasks to the created opportunities
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.clickRecord(1);
		VoodooUtils.waitForReady(); //Extra wait needed
		StandardSubpanel tasksSubpanel = sugar.opportunities.recordView.subpanels.get(sugar.tasks.moduleNamePlural);
		tasksSubpanel.scrollIntoViewIfNeeded(false);
		tasksSubpanel.linkExistingRecords(taskRecords);
	}

	// TODO: VOOD-591/VOOD-592
	// Add dashlets
	private void addActiveTasksDashlet(int row, int column, String dashletName) throws Exception {
		sugar.opportunities.dashboard.edit();
		sugar.opportunities.dashboard.addRow();
		sugar.opportunities.dashboard.addDashlet(row, column);

		// TODO: VOOD-960, VOOD-1645
		// Add a dashlet -> select "Active Tasks"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashletName);
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Save
		new VoodooControl("a", "css", ".layout_Opportunities.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		saveDashBoardBtnCtrl = new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']");
		saveDashBoardBtnCtrl.click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify tasks dashlets should be available on record view of opportunities
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20293_Opportunities_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add the active tasks and inactive tasks dashlets to an account record 
		// In the intelligence pane on the right (i.e RHS pane), add a new Active Tasks and Inactive tasks dashlets
		dashboardTitle = sugar.opportunities.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(dashboardData.get("dashboardTitle"), true))
			sugar.dashboard.chooseDashboard(dashboardData.get("dashboardTitle"));
		addActiveTasksDashlet(7, 1, dashboardData.get("activeTasks"));
		addActiveTasksDashlet(8, 1, dashboardData.get("inactiveTasks"));

		// TODO: VOOD-960
		VoodooControl activeTaskDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(7) .dashlet-container:nth-of-type(1)");
		VoodooControl inactiveTaskDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(8) .dashlet-container:nth-of-type(1)");

		// Verify that both Active Tasks and Inactive Tasks dashlets should be available to be used on record view of opportunities
		// Active Tasks Dashlet
		new VoodooControl("div", "css", activeTaskDashletCtrl.getHookString() + " .dashlet-title").assertContains(dashboardData.get("activeTasks"), true);
		new VoodooControl("a", "css", activeTaskDashletCtrl.getHookString() + " .tab-content p a:nth-child(2)").assertContains(tasksDS.get(0).get("subject"), true);
		// Inactive Tasks Dashlet
		new VoodooControl("div", "css", inactiveTaskDashletCtrl.getHookString() + " .dashlet-title").assertContains(dashboardData.get("inactiveTasks"), true);
		new VoodooControl("a", "css", inactiveTaskDashletCtrl.getHookString() + " .tab-content p a:nth-child(2)").assertContains(tasksDS.get(1).get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}