package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_24723  extends SugarTest {
	UserRecord chris, sally;
	FieldSet teamData = new FieldSet();

	public void setup() throws Exception {
		sugar().login();

		// Require 2 non-admin users having same team.
		DataSource userData = testData.get(testName);
		chris = new UserRecord(sugar().users.create(userData.get(0)));
		sally = new UserRecord(sugar().users.create(userData.get(1)));

		// Go to Admin -> Team Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("teamsManagement").click();
		// Search the team
		teamData = testData.get(testName+"_customData").get(0);
		sugar().teams.listView.basicSearch(teamData.get("teamField").substring(0, 4));
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-518
		new VoodooControl("a", "id", "team_memberships_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", ".list.view tr:nth-child(3) td:nth-child(1) input").click();
		new VoodooControl("input", "css", ".list.view tr:nth-child(4) td:nth-child(1) input").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);

		// Changing default team of chris user
		// Admin -> User Management
		chris.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl advancedTabCtrl = sugar().users.userPref.getControl("tab4");
		advancedTabCtrl.click();
		// TODO: VOOD-1040 need defined control for the team widget on user profile edit page
		VoodooControl teamRemoveCtrl = new VoodooControl("button", "css", "#EditView_team_name_table .id-ff-remove");
		teamRemoveCtrl.click();
		VoodooControl teamFieldCtrl = new VoodooControl("input", "css", "#EditView_team_name_table td:nth-child(1) span .yui-ac-input");
		teamFieldCtrl.set(teamData.get("teamField").substring(0, 4));
		VoodooUtils.waitForReady();
		VoodooControl teamTypeAheadCtrl = new VoodooControl("li", "css", "#EditView_team_name_table tbody td:nth-child(1) span div ul li:nth-child(1)");
		teamTypeAheadCtrl.click();
		VoodooControl radioButtonCtrl = new VoodooControl("input", "css", ".radio");
		radioButtonCtrl.click();
		VoodooUtils.focusDefault();
		// Save
		sugar().users.editView.save();

		// Changing default team of sally user.
		sally.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		advancedTabCtrl.click();
		teamRemoveCtrl.click();
		teamFieldCtrl.set(teamData.get("teamField").substring(0, 4));
		VoodooUtils.waitForReady();
		teamTypeAheadCtrl.click();
		radioButtonCtrl.click();
		VoodooUtils.focusDefault();
		// Save
		sugar().users.editView.save();
		sugar().logout();
	}

	/**
	 *  User-Normal Team_Verify that team member can access the team's records
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24723_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log in as non-admin user
		chris.login();

		// Create record in leads module.
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		sugar().leads.createDrawer.save();

		// Ensure team field in lead record.
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();

		// Verify the team field is the one mentioned in "Precondition".
		sugar().leads.recordView.getDetailField("relTeam").assertEquals(teamData.get("teamField"), true);

		// Create record in accounts module
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.save();

		// Ensure team field in account record.
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("relTeam").assertEquals(teamData.get("teamField"), true);

		sugar().logout();
		// Login to SugarCRM as another non-admin user which is in the same team as the user1
		sally.login();

		// Navigate to leads.
		sugar().leads.navToListView();

		// Verify record created by first user in the same team is displayed.
		sugar().leads.listView.getDetailField(1, "fullName").assertEquals(testName, true);

		// Navigate to Accounts
		sugar().accounts.navToListView();

		// Verify record created by first user in the same team is displayed.
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}