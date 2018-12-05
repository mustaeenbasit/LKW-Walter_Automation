package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_28158 extends SugarTest{
	FieldSet userData;
	UserRecord user;

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that default team should only set the user's private team.
	 * @throws Exception
	 */
	@Test
	public void Users_28158_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create User
		userData = testData.get(testName).get(0);
		user = (UserRecord)sugar.users.create(userData);

		// Edit created user
		user.navToRecord();
		sugar.users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.users.userPref.getControl("tab4").click();
		
		VoodooUtils.waitForReady();
		
		// TODO VOOD-1040 need defined control for the team widget on user profile edit page
		new VoodooControl("button", "id", "remove_team_name_collection_0").click();
		new VoodooControl("input", "id", "EditView_team_name_collection_0").set(userData.get("firstName")); //+" " + userData.get("lastName"));
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("li", "css", "div#EditView_EditView_team_name_collection_0_results li:nth-of-type(1)").click();
		new VoodooControl("input", "css", ".radio").click();
		VoodooUtils.focusDefault();

		// Save
		sugar.users.editView.save();
		sugar.logout();

		// Login with created user
		sugar.login(user);
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "tab2").click();

		// Verify Default Team should only say the User's Private Team.
		new VoodooControl("slot", "css", "#settings tr:nth-child(3) td:nth-child(2) slot").assertContains(userData.get("firstName") + " " + userData.get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}