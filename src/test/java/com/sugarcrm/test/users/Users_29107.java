package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_29107 extends SugarTest{
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that Reset user preferences option is not available in Create New user form
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29107_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> User management
		sugar.users.navToListView();

		// Click on Create New user
		sugar.navbar.selectMenuItem(sugar.users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Reset user preference option should not be available in Create new user form
		// TODO: VOOD-563
		new VoodooControl("input", "id", "reset_user_preferences_header").assertExists(false);

		// Cancel the Create form of the user
		sugar.users.editView.getControl("cancelButton").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}