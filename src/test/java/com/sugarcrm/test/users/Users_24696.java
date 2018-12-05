package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24696 extends SugarTest{
	DataSource userDataSet = new DataSource();
	UserRecord user1, user2;

	public void setup() throws Exception {
		sugar().login();

		// Create users
		userDataSet = testData.get(testName);
		user1 = (UserRecord)sugar().users.create(userDataSet.get(0));
		VoodooUtils.focusDefault();
		user2 = (UserRecord)sugar().users.create(userDataSet.get(1));
	}

	/**
	 * Verify that a user can be removed from a team.
	 * @throws Exception
	 */
	@Test
	public void Users_24696_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Team management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("teamsManagement").click();

		// TODO: VOOD-518
		// Create team and save
		VoodooControl teamCtrl = new VoodooControl("button", "css", ".dropdown.active .btn-group .dropdown-toggle");
		teamCtrl.waitForVisible();
		teamCtrl.click();
		VoodooControl createTeamCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll ul li:nth-of-type(1)");
		createTeamCtrl.waitForVisible();
		createTeamCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		FieldSet teamSet = testData.get(testName+"_1").get(0);
		// TODO: VOOD-518
		new VoodooControl("input", "css", "table.edit input").set(teamSet.get("teamName"));
		new VoodooControl("input", "id", "btn_save").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("a", "css", "#team_memberships_select_button").click();
		VoodooUtils.focusWindow(1);

		// TODO: VOOD-518
		// Select Jim user to team
		new VoodooControl("input", "css", ".list.view tr:nth-child(4) td:nth-child(1) input").click();
		new VoodooControl("input", "css", "#MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-518
		// unlink Jim user
		new VoodooControl("a", "id", "users_remove_1").click();
		VoodooUtils.acceptDialog();

		// TODO: VOOD-518
		// Verify Jim and Will are not displayed in user list.
		new VoodooControl("div", "id", "list_subpanel_users").assertContains(userDataSet.get(0).get("userName"), false);
		new VoodooControl("div", "id", "list_subpanel_users").assertContains(userDataSet.get(1).get("userName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
