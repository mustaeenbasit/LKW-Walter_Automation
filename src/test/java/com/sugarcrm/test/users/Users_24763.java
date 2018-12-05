package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24763 extends SugarTest {
	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify User dropdown list on navigation bar
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24763_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click the triangle after the User on navigation bar
		sugar().navbar.toggleUserActionsMenu();
		
		// Verify that user name showing up as a drop down list
		sugar().navbar.userAction.getControl("userActions").assertAttribute("data-original-title", sugar().users.getQAUser().get("userName"), true);
		
		// Verify that user dropdown list on navigation bar (Profile, Employees, About, Log out)
		sugar().navbar.userAction.getControl("profile").assertVisible(true);
		
		// TODO: VOOD-1041
		new VoodooControl("li", "css", ".profileactions-employees").assertVisible(true);
		
		// TODO: VOOD-1881
		new VoodooControl("li", "css", ".profileactions-about").assertVisible(true);
		
		sugar().navbar.userAction.getControl("logout").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}