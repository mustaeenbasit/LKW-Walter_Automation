package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_27718 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user icon is displaying correctly after switching login users
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_27718_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Define common controls
		VoodooControl toggleUserActionsCtrl = sugar().navbar.userAction.getControl("userActions");
		VoodooControl adminSelectionCtrl = sugar().navbar.userAction.getControl("admin");
		String adminString = customFS.get("admin");
		String attributeString = customFS.get("attributeValue");
		String qaUser = sugar().users.getQAUser().get("userName");

		// Verify that the user icon is indicated as admin
		toggleUserActionsCtrl.hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(adminString, true);

		// Go to User menu
		sugar().navbar.toggleUserActionsMenu();

		// Verify that there is 'admin' option
		adminSelectionCtrl.assertVisible(true);
		adminSelectionCtrl.hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		adminSelectionCtrl.assertEquals(customFS.get("adminLabel"), true);
		
		sugar().navbar.toggleUserActionsMenu(); // Close the user action drop down

		// Logout and Login as QAUser this time
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Verify that user icon is indicated as 'qauser'
		toggleUserActionsCtrl.hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(qaUser, true);

		// Go to User menu
		sugar().navbar.toggleUserActionsMenu();

		// Verify that there is no 'admin' option
		adminSelectionCtrl.assertExists(false);
		sugar().navbar.toggleUserActionsMenu(); // Close the user action drop down

		// Logout and Login as admin
		sugar().logout();
		sugar().login();

		// Verify the user icon is 'admin', not 'qauser'
		toggleUserActionsCtrl.hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(adminString, true);
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(qaUser, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}