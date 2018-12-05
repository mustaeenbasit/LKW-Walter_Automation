package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24744 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify on creating new inactive user, no reassign record pop-up window displays
	 * @throws Exception
	 */
	@Test
	public void Users_24744_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to user management
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("userManagement").click();
		VoodooUtils.focusDefault();

		// Now select 'create User' from navbar -> Users 
		sugar().navbar.selectMenuItem(sugar().users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame");
		FieldSet customData = testData.get(testName).get(0);

		// Fill 'User name','last name','status'
		// TODO: VOOD-563 - need lib support for user profile edit page
		sugar().users.editView.getEditField("userName").set(testName);
		sugar().users.editView.getEditField("lastName").set(testName);
		new VoodooControl("select", "id", "status").set(customData.get("userStatus"));

		// Now Fill Email Address and mark this as Primary
		sugar().users.editView.getEditField("emailAddress").set(sugar().users.getDefaultData().get("emailAddress"));

		// Enter password
		sugar().users.editView.getControl("passwordTab").click();
		VoodooUtils.waitForReady();
		sugar().users.editView.getEditField("newPassword").set(sugar().users.getDefaultData().get("newPassword"));
		sugar().users.editView.getEditField("confirmPassword").set(sugar().users.getDefaultData().get("confirmPassword"));

		// Now click on Save button to Save the changes 
		sugar().users.editView.getControl("save").click();
		VoodooUtils.waitForReady();
		sugar().users.editView.getControl("confirmCreate").click();

		// Verify re-assign_popup window doesn't appear when create inactive user
		// TODO: VOOD-1871
		new VoodooControl("div", "css", "#popup_window div").assertVisible(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}