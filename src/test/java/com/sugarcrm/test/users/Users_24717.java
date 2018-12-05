package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24717  extends SugarTest {

	public void setup() throws Exception {
		// Log in as non-admin user
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 *  User-Normal_Verify that non-admin user cannot access "Admin" page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24717_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.toggleUserActionsMenu();

		// Verify  No "Admin" link is displayed in top right corner.
		sugar().navbar.userAction.getControl("admin").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}