package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_28081 extends SugarTest {
	VoodooControl dashboardTitle, moduleCtrl, fieldLayoutCtrl, nameFieldCtrl, defaultValueCtrl, saveButtonCtrl, saveDashBoardBtnCtrl;
	FieldSet dashboardData;
	UserRecord customUser;

	public void setup() throws Exception {
		dashboardData = testData.get(testName).get(0);
		sugar.accounts.api.create();
		sugar.login();

		// Create custom user
		customUser = (UserRecord) sugar.users.create();
		VoodooUtils.waitForReady(); // Needed extra wait 
		sugar.users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-987
		new VoodooControl("select", "id", "UserType").set(dashboardData.get("userType"));
		VoodooUtils.focusDefault();
		sugar.users.editView.save();
		VoodooUtils.waitForReady(); //Extra wait needed

		// Logout from the admin user and login as custom user
		sugar.logout();
		customUser.login();
	}

	// TODO: VOOD-591/VOOD-592
	// Go to Accounts and select one of the accounts. -> In the intelligence pane on the right, of the account add 'Active Tasks' dashlet
	private void addActiveTasksDashlet(int row, int column) throws Exception {
		// Go to Accounts and select one of the accounts
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady(); //Extra wait needed

		// In the intelligence pane on the right (i.e RHS pane), add a new Active Tasks dashlet
		dashboardTitle = sugar.accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(dashboardData.get("dashboardTitle"), true))
			sugar.dashboard.chooseDashboard(dashboardData.get("dashboardTitle"));
		sugar.accounts.dashboard.edit();
		sugar.accounts.dashboard.addRow();
		sugar.accounts.dashboard.addDashlet(row, column);

		// TODO: VOOD-960, VOOD-1645
		// Add a dashlet -> select "Active Tasks"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashboardData.get("activeTasks"));
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Save
		new VoodooControl("a", "css", ".layout_Accounts.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		saveDashBoardBtnCtrl = new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']");
		saveDashBoardBtnCtrl.click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that on changing the Default value for name field does not affect active tasks dashlet.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_28081_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add Active Tasks dashlet
		addActiveTasksDashlet(6, 1);

		// Verify that the dashlet is added successfully
		// TODO: VOOD-960
		VoodooControl oldTaskDashletCtrl = new VoodooControl("h4", "css", ".row-fluid.sortable:nth-of-type(6) .dashlet-container:nth-of-type(1) .dashlet-title");
		oldTaskDashletCtrl.assertEquals(dashboardData.get("activeTasks"), true);

		// TODO: VOOD-542, VOOD-1504
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		fieldLayoutCtrl = new VoodooControl("td", "id", "fieldsBtn");
		nameFieldCtrl = new VoodooControl("a", "id", "name");
		defaultValueCtrl = new VoodooControl("input", "id", "default");
		saveButtonCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");

		// Go to Admin > Studio > Accounts > Fields > Name and set the default value to "Test Value" and save the field
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		fieldLayoutCtrl.click();
		VoodooUtils.waitForReady();
		nameFieldCtrl.click();
		VoodooUtils.waitForReady();
		defaultValueCtrl.set(dashboardData.get("testValue"));
		saveButtonCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Accounts and select one of the accounts. -> In the intelligence pane on the right, of the account add another Active Tasks dashlet
		addActiveTasksDashlet(7, 1);

		// Verify that the dashlet is added successfully
		oldTaskDashletCtrl.assertEquals(dashboardData.get("activeTasks"), true);
		// TODO: VOOD-960
		new VoodooControl("h4", "css", ".row-fluid.sortable:nth-of-type(7) .dashlet-container:nth-of-type(1) .dashlet-title").assertEquals(dashboardData.get("activeTasks"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}