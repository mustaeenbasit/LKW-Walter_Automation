package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_29676 extends SugarTest {
	UserRecord customUserRecord;

	public void setup() throws Exception {
		FieldSet userPasswordData = testData.get(testName).get(0);

		// Login as an Admin user
		sugar().login();

		// Go to Admin > User Management and create a new "Regular User" and set their password as <Test123>
		// TODO: VOOD-1200
		customUserRecord = (UserRecord) sugar().users.create(userPasswordData);

		// Log-out from Sugar and Login again as Regular user (Newly created).
		sugar().logout();
		customUserRecord.login();
	}

	/**
	 * Verify that Regular User is able to change their password while set password as <Test123> from Administrator
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_29676_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Profile > Edit > Password tab > Provide "Current", "New" and "Confirm" password
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		//Go to Password tab and change the password and save
		// TODO: VOOD-987
		sugar().users.editView.getControl("passwordTab").click();
		new VoodooControl("input", "id", "old_password").set(customUserRecord.get("password"));
		sugar().users.editView.getEditField("newPassword").set(testName);
		sugar().users.editView.getEditField("confirmPassword").set(testName);
		sugar().users.editView.getControl("saveButton").click();

		// Verify that No error message should display
		new VoodooControl("span", "id", "error_pwd").assertExists(false);
		VoodooUtils.waitForReady(60000);

		// Verify that the user is able to change password while providing correct Current password as "<Test123>"
		sugar().users.editView.getControl("confirmCreate").click();
		VoodooUtils.waitForReady(60000);

		// Verify that password saved successfully and instance navigates to the user's detail view 
		sugar().users.detailView.assertVisible(true);
		sugar().users.detailView.getControl("editButton").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}