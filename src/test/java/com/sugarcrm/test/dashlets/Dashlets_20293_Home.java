package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20293_Home extends SugarTest {
	UserRecord customUser;
	FieldSet dashboardData = new FieldSet();
	DataSource tasksDS = new DataSource();

	public void setup() throws Exception {
		dashboardData = testData.get(testName).get(0);
		tasksDS = testData.get(testName + "_" + sugar.tasks.moduleNamePlural);

		// Create two tasks - 1) Active: Set old Due Date 2) Inactive: Status: Deferred
		sugar.tasks.api.create(tasksDS);

		// Login
		sugar.login();

		// Create custom user
		customUser = (UserRecord) sugar.users.create();
		VoodooUtils.waitForReady(); // Needed extra wait

		// Logout from the admin user and login as custom user
		sugar.logout();
		customUser.login();
	}

	// TODO: VOOD-591/VOOD-592
	// Add dashlets
	private void addActiveTasksDashlet(int row, int column, String dashletName) throws Exception {
		sugar.home.dashboard.edit();
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(row, column);

		// TODO: VOOD-960, VOOD-1645
		// Add a dashlet -> select "Active Tasks"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashletName);
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		new VoodooSelect("div", "css", "span[data-voodoo-name='visibility'] div").set(dashboardData.get("onlyMyItem"));

		// Save
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		VoodooControl saveDashBoardBtnCtrl = new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']");
		saveDashBoardBtnCtrl.click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify tasks dashlets should be available on record view of home
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20293_Home_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add the active tasks and inactive tasks dashlets to Home 
		// Go to main Dashboard
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		addActiveTasksDashlet(4, 1, dashboardData.get("activeTasks"));
		addActiveTasksDashlet(5, 1, dashboardData.get("inactiveTasks"));

		// TODO: VOOD-960
		VoodooControl activeTaskDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(4) .dashlet-container:nth-of-type(1)");
		VoodooControl inactiveTaskDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(5) .dashlet-container:nth-of-type(1)");

		// Verify that both Active Tasks and Inactive Tasks dashlets should be available to be used on record view of home
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