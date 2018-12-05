package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20017 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * "Email address" field is not required
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20017_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > PasswordManagement
		sugar().admin.navToAdminPanelLink("passwordManagement");
		VoodooUtils.focusFrame("bwc-frame"); 

		// TODO: VOOD-948
		// Not check "System-Generated Passwords", not check "User Reset Password"
		new VoodooControl("input", "id", "forgotpassword_checkbox").click();

		// Save updation of PasswordManagement settings 
		new VoodooControl("input", "css", "input[title='Save']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		sugar().users.navToListView();

		// Click on Create Link
		sugar().navbar.selectMenuItem(sugar().users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame");

		// Set all required filed except EmailAddress field
		FieldSet userFS = sugar().users.getDefaultData();
		sugar().users.editView.getEditField("userName").set(userFS.get("userName"));
		sugar().users.editView.getEditField("firstName").set(userFS.get("firstName"));
		sugar().users.editView.getEditField("lastName").set(userFS.get("lastName"));
		sugar().users.editView.getControl("passwordTab").click();
		sugar().users.editView.getEditField("newPassword").set(userFS.get("newPassword"));
		sugar().users.editView.getEditField("confirmPassword").set(userFS.get("confirmPassword"));
		sugar().users.editView.getControl("save").click();
		sugar().users.editView.getControl("confirmCreate").click();
		VoodooUtils.waitForReady();

		// Verify that "Email address" field are not required and user save successfully
		sugar().users.detailView.getDetailField("userName").assertEquals(userFS.get("userName"), true);
		sugar().users.detailView.getDetailField("fullName").assertEquals(userFS.get("fullName"), true);

		// Verify that "Email address" is not exist
		sugar().users.detailView.getDetailField("emailAddress").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}