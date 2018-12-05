package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20294 extends SugarTest {
	UserRecord customUser;
	FieldSet dashboardData = new FieldSet();
	VoodooControl dashboardTitle;

	// TODO: VOOD-591 - Dashlets support
	// TODO: VOOD-592 - Add dashlet support to home screen model.
	// Add dashlets
	private void addActiveTasksDashlet(int row, int column, String dashletName) throws Exception {
		sugar.accounts.dashboard.edit();
		sugar.accounts.dashboard.addRow();
		sugar.accounts.dashboard.addDashlet(row, column);

		// TODO: VOOD-960 -  Dashlet selection
		// TODO: VOOD-1645 - Need to update method(s) in 'Dashboard.java' for Edit page.
		// Add a dashlet -> select "Active Tasks"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashletName);
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		new VoodooControl("a", "css", ".layout_Accounts.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();
	}

	public void setup() throws Exception {
		dashboardData = testData.get(testName).get(0);
		sugar.accounts.api.create();
		sugar.login();

		// Create custom user
		customUser = (UserRecord) sugar.users.create();

		// Logout from the admin user and login as custom user
		sugar.logout();
		customUser.login();

		// Add the active tasks and inactive tasks dashlets to an account record 
		// Go to Accounts and select one of the accounts
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// In the intelligence pane on the right (i.e RHS pane), add a new Active Tasks dashlet
		dashboardTitle = sugar.accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(dashboardData.get("dashboardTitle"), true)) {
			sugar.dashboard.chooseDashboard(dashboardData.get("dashboardTitle"));
		}
		addActiveTasksDashlet(6, 1, dashboardData.get("activeTasks"));
		addActiveTasksDashlet(7, 1, dashboardData.get("inactiveTasks"));
	}

	/**
	 * Verify the tasks dashlet can be toggled by a Close/Open option from configure menu.
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20294_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the account record view that used in setup
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady(); //Extra wait needed
		if(!dashboardTitle.queryContains(dashboardData.get("dashboardTitle"), true)) {
			sugar.dashboard.chooseDashboard(dashboardData.get("dashboardTitle"));
		}
		// TODO: VOOD-960 - Dashlet selection
		String activeDashletHookString = ".row-fluid.sortable:nth-of-type(6) .dashlet-container";
		VoodooControl activeTaskConfigureBtnCtrl = new VoodooControl("i", "css", ".dashlet-row li:nth-child(6) .dashlet-header .btn-group i");
		VoodooControl firstConfigureMenuCtrl = new VoodooControl("a", "css", activeDashletHookString + " .dropdown-menu li a");
		VoodooControl secondConfigureMenuCtrl = new VoodooControl("a", "css", activeDashletHookString + " .dropdown-menu li:nth-child(2) a");
		VoodooControl activeTaskCloseOrOpenBtnCtrl = new VoodooControl("a", "css", activeDashletHookString + " .dropdown-menu li:nth-child(3) a");
		VoodooControl fourthConfigureMenuCtrl = new VoodooControl("a", "css", activeDashletHookString + " .dropdown-menu li:nth-child(4) a");

		// Click Configure button on the Active Tasks dashlet from the RHS dashboard
		activeTaskConfigureBtnCtrl.scrollIntoViewIfNeeded(true); // Scroll and click

		// Verify that there are 4 options under configure menu: Edit, Refresh, Close or Open, Remove
		firstConfigureMenuCtrl.assertContains(dashboardData.get("edit"), true);
		secondConfigureMenuCtrl.assertContains(dashboardData.get("refresh"), true);
		activeTaskCloseOrOpenBtnCtrl.assertContains(dashboardData.get("close"), true);
		fourthConfigureMenuCtrl.assertContains(dashboardData.get("remove"), true);

		// Click Minimize/Close
		activeTaskCloseOrOpenBtnCtrl.click();

		// Verify that the active tasks dashlets collapse with only "Active Tasks" label shown
		VoodooControl activeTaskTitle = new VoodooControl("div", "css", activeDashletHookString + " .dashlet-title");
		new VoodooControl("div", "css", activeDashletHookString + " .dashlet .dashlet-header").assertVisible(true);
		VoodooControl activeTashDashlet =  new VoodooControl("div", "css", activeDashletHookString + " .dashlet .tab-pane.active");
		activeTashDashlet.assertVisible(false);
		activeTaskTitle.assertContains(dashboardData.get("activeTasks"), true);

		// Click Configure button again on the Active Tasks dashlet
		activeTaskConfigureBtnCtrl.click();

		// Verify that There are 4 options under configure menu: Edit, Refresh, Maximize / Open, Remove
		firstConfigureMenuCtrl.assertContains(dashboardData.get("edit"), true);
		secondConfigureMenuCtrl.assertContains(dashboardData.get("refresh"), true);
		activeTaskCloseOrOpenBtnCtrl.assertContains(dashboardData.get("open"), true);
		fourthConfigureMenuCtrl.assertContains(dashboardData.get("remove"), true);

		// Click Maximize/Open 
		activeTaskCloseOrOpenBtnCtrl.click();

		// Verify that the active tasks dashlets expands to show the tasks list
		new VoodooControl("div", "css", activeDashletHookString + " .dashlet").assertVisible(true);
		activeTashDashlet.assertVisible(true);
		activeTaskTitle.assertContains(dashboardData.get("activeTasks"), true);
		new VoodooControl("div", "css", activeDashletHookString + " .block-footer").assertContains(dashboardData.get("noDataAvailable"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
