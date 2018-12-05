package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_19997 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * [Password Rules]-Selected"Must contain one lower case letter(a-z)"and"Must contain one number (0-9)",
	 * Verify user according with the rule
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_19997_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		String qauser = sugar().users.getQAUser().get("newPassword");

		sugar().admin.navToAdminPanelLink("passwordManagement");
		VoodooUtils.focusFrame("bwc-frame"); 

		// Verify that Must contain one upper case letter (A-Z) is checked.
		sugar().admin.passwordManagement.getControl("passwordSettingOneUpper").assertChecked(true);

		// Verify that Must contain one number (0-9) is checked
		sugar().admin.passwordManagement.getControl("passwordSettingOneNumber").assertChecked(true);

		// Verify that Must contain one lower case letter (a-z) is checked
		sugar().admin.passwordManagement.getControl("passwordSettingOneLower").assertChecked(true);
		sugar().admin.passwordManagement.getControl("save").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// create a demo user
		UserRecord demoUser = (UserRecord)sugar().users.create();
		sugar().logout();

		// login as a demo user
		sugar().login(demoUser);

		// navigate to user profile page
		sugar().navbar.navToProfile();

		// click edit 
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// navigate to password management tab
		sugar().users.editView.getControl("passwordTab").click();

		// Verify fields exists:Current Password,New password and Confirm password 
		// TODO: VOOD-987
		VoodooControl currentPassword = new VoodooControl("input", "id", "old_password");
		VoodooControl newPassword = sugar().users.editView.getEditField("newPassword");
		VoodooControl confirmPassword = sugar().users.editView.getEditField("confirmPassword");
		currentPassword.assertVisible(true);
		newPassword.assertVisible(true);
		confirmPassword.assertVisible(true);

		// TODO: VOOD-947
		VoodooControl upperCaseLetter = new VoodooControl("div", "id", "1upcase");
		VoodooControl lowerCaseLetter  = new VoodooControl("div", "id", "1lowcase");
		VoodooControl number = new VoodooControl("div", "id", "1number");
		VoodooControl minLength = new VoodooControl("div", "id", "lengths");

		//  Verify password rules exists with red tips besides "change password" panel
		upperCaseLetter.assertAttribute("class", customFS.get("badClassName"));
		lowerCaseLetter.assertAttribute("class", customFS.get("badClassName"));
		number.assertAttribute("class", customFS.get("badClassName"));
		minLength.assertAttribute("class", customFS.get("badClassName"));

		// edit the password
		// TODO: VOOD-987
		currentPassword.set(demoUser.get("password"));
		newPassword.set(qauser);
		confirmPassword.set(qauser);

		// Verify that If the password meet the Password Requirements rules,the tips change green
		upperCaseLetter.assertAttribute("class", customFS.get("goodClassName"));
		lowerCaseLetter.assertAttribute("class", customFS.get("goodClassName"));
		number.assertAttribute("class", customFS.get("goodClassName"));
		minLength.assertAttribute("class", customFS.get("goodClassName"));
		VoodooUtils.focusDefault();
		// save the changed password
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();

		// logout
		sugar().logout();

		// update user object to replace changed password
		demoUser.put("password", qauser);

		// login again with the same user
		sugar().login(demoUser);

		// verify the Login in again as successful
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		// verify the login user 
		sugar().users.detailView.getDetailField("fullName").assertContains(demoUser.get("fullName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}