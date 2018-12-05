package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_27701 extends SugarTest {
	FieldSet passwordData;
	VoodooControl currentPassword;

	public void setup() throws Exception {
		passwordData = testData.get(testName).get(0);
		// Login as valid user(QAUser)
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify user should not be logged out on the password change 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_27701_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to User profile and Click on Edit button
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		//Go to Password tab and change the password and save
		// TODO: VOOD-987
		currentPassword = new VoodooControl("input", "id", "old_password");
		sugar().users.editView.getControl("passwordTab").click();
		currentPassword.set(passwordData.get("defaultPassword"));
		sugar().users.editView.getEditField("newPassword").set(passwordData.get("newPassword"));
		sugar().users.editView.getEditField("confirmPassword").set(passwordData.get("newPassword"));

		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify that the User should not be logged out and should be display "Password was changed successfully"
		new VoodooControl("div", "css", "#sugarMsgWindow .bd").assertContains(passwordData.get("warningMessage"), true);
		sugar().users.editView.getControl("confirmCreate").click();
		VoodooUtils.waitForReady(120000);

		sugar().users.detailView.assertVisible(true);
		sugar().users.detailView.getControl("editButton").assertVisible(true);
		VoodooUtils.focusDefault();

		sugar().logout(); //Logout from admin user

		// Login into QAUser with the new password
		FieldSet userData = new FieldSet();
		userData.put("userName", sugar().users.getQAUser().get("userName"));
		userData.put("password", passwordData.get("newPassword"));
		sugar().login(userData);
		userData.clear();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}