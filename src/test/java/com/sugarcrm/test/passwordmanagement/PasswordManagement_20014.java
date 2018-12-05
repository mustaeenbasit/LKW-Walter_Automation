package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20014 extends SugarTest {
	UserRecord chrisUserRecord;

	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-1200 Once resolved it should create via API
		// Create a new User Chris
		chrisUserRecord = (UserRecord) sugar().users.create();

		// logout as Admin and login as Chris
		sugar().logout();
	}

	/**
	 * verify "change password",re-login successful
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20014_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().login(chrisUserRecord);

		// verify that Chris logged in successfully
		sugar().dashboard.assertExists(true);

		// Change/Update user Login password
		sugar().navbar.navToProfile();

		// Password Settings
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-987
		FieldSet customFS = testData.get(testName).get(0);
		new VoodooControl("input", "css", "#old_password").set(chrisUserRecord.get("newPassword"));
		sugar().users.editView.getEditField("newPassword").set(customFS.get("newPassword"));
		sugar().users.editView.getEditField("confirmPassword").set(customFS.get("confirmPassword"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		// Logout as Chris and re-login as Chris with updated password
		sugar().logout();

		FieldSet userLoginInfo = new FieldSet();
		userLoginInfo.put("userName", chrisUserRecord.get("userName"));
		userLoginInfo.put("password", customFS.get("newPassword"));
		sugar().login(userLoginInfo);

		// Verify that Chris user new password re-login Successfully
		sugar().dashboard.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}