package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20020 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * [Duplicate User]_Duplicate "System Administrator User",The Change Password area is requirement
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20020_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to User Management
		sugar().users.navToListView();

		// Click "Create New User" link from navigation Shortcuts
		sugar().navbar.selectMenuItem(sugar().users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame");

		FieldSet userAndPasswordData = testData.get(testName).get(0);
		// Fill mandatory fields and select User Type to "System Administrator User"
		sugar().users.editView.getEditField("firstName").set(userAndPasswordData.get("firstName"));
		sugar().users.editView.getEditField("lastName").set(userAndPasswordData.get("lastName"));
		sugar().users.editView.getEditField("userName").set(userAndPasswordData.get("userName"));
		sugar().users.editView.getEditField("emailAddress").set(userAndPasswordData.get("email"));

		// Save without entering password
		sugar().users.editView.getControl("saveButton").click();

		// Observe that change password area is required
		// TODO: VOOD-948
		VoodooControl validationMessage = new VoodooControl("div", "css", ".validation-message");
		validationMessage.waitForVisible();

		validationMessage.assertAttribute("class", userAndPasswordData.get("requiredClassName"), true);
		validationMessage.assertContains(userAndPasswordData.get("validationMessage"), true);

		VoodooUtils.waitForReady();

		// Enter the password and click "Save" button
		sugar().users.editView.getEditField("newPassword").set(userAndPasswordData.get("password"));
		sugar().users.editView.getEditField("confirmPassword").set(userAndPasswordData.get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();

		VoodooUtils.waitForReady(30000);

		// Click "Copy" button
		sugar().users.detailView.copy();
		VoodooUtils.focusFrame("bwc-frame");

		// Go to duplicate user Edit View -> Enter User Name and click on Save button
		sugar().users.editView.getEditField("userName").set(userAndPasswordData.get("newUserName"));
		sugar().users.editView.getControl("saveButton").click();

		// Verify that the Change Password area should be required
		validationMessage.assertAttribute("class", userAndPasswordData.get("requiredClassName"), true);
		validationMessage.assertContains(userAndPasswordData.get("validationMessage"), true);
		VoodooUtils.focusDefault();

		// Click on Cancel button
		sugar().users.editView.cancel();

		VoodooUtils.waitForReady(30000);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}