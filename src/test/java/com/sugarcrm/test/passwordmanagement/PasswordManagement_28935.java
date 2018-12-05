package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_28935 extends SugarTest {
	UserRecord customUserRecord;

	public void setup() throws Exception {
		FieldSet userData = testData.get(testName+"_user").get(0);
		sugar().login();
		customUserRecord = (UserRecord) sugar().users.create(userData);
		VoodooUtils.waitForReady(); // Required more wait here

		// Logout Admin and login as new created user
		sugar().logout();
		customUserRecord.login();
	}

	/**
	 * Verify that User is able to login while reset password as 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_28935_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Before update password, Verify Login is successful and User redirected on Home Page or User profile (step 1) if first time login.
		sugar().home.dashboard.assertExists(true);

		// Logout New created user and login as Admin
		sugar().logout();
		sugar().login();

		// Update new user Password and Confirm Password as "<Test123>"
		FieldSet customFS = testData.get(testName).get(0);
		customUserRecord.navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").waitForVisible();
		VoodooUtils.focusDefault();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		sugar().users.editView.getEditField("newPassword").set(customFS.get("password"));
		sugar().users.editView.getEditField("confirmPassword").set(customFS.get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Log-out from Admin and login again using same password i.e. <Test123>
		sugar().logout();
		FieldSet loginData = customUserRecord;
		loginData.put("password", customFS.get("password"));
		sugar().login(loginData);

		// Login is successful and User redirected on Home Page or User profile (step 1) if first time login.
		sugar().home.dashboard.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}