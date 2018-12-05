package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_29705 extends SugarTest{
	public void setup() throws Exception {
		// Login
		sugar().login();
	}

	/**
	 * Verify that User details should be populated from same user name error page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29705_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet userData = testData.get(testName).get(0);

		// Go to admin -> User management and click on create user
		sugar().users.navToListView();
		sugar().navbar.selectMenuItem(sugar().users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame");

		// Define Edit Fields for the user edit view for user Name, First Name, Last Name, Email, Reports To, New Password and Confirm Password 
		VoodooControl userNameCtrl = sugar().users.editView.getEditField("userName");
		VoodooControl firstNameCtrl = sugar().users.editView.getEditField("firstName");
		VoodooControl lastNameCtrl = sugar().users.editView.getEditField("lastName");
		VoodooControl emailAddressCtrl = sugar().users.editView.getEditField("emailAddress");
		VoodooControl reportsToCtrl = sugar().users.editView.getEditField("reportsTo");
		VoodooControl newPasswordCtrl = sugar().users.editView.getEditField("newPassword");
		VoodooControl confirmPasswordCtrl = sugar().users.editView.getEditField("confirmPassword");

		// Define controls for Title, Department, Work Phone and for error message on the user edit view
		VoodooControl titleCtrl = sugar().users.editView.getEditField("title");
		VoodooControl departmentCtrl = sugar().users.editView.getEditField("department");
		VoodooControl phoneWorkCtrl = sugar().users.editView.getEditField("phoneWork");
		// TODO: VOOD-563
		VoodooControl errorMessageCtrl = new VoodooControl("span", "id", "ajax_error_string");

		// Enter details like user name (another user should exist with same user name), last name, title, department, email, reports to, work phone etc.. 
		firstNameCtrl.set(userData.get("firstName"));
		lastNameCtrl.set(userData.get("lastName"));
		userNameCtrl.set(userData.get("userName"));
		emailAddressCtrl.set(userData.get("emailAddress"));
		reportsToCtrl.set(userData.get("reportsTo"));
		titleCtrl.set(userData.get("title"));
		departmentCtrl.set(userData.get("department"));
		phoneWorkCtrl.set(userData.get("phoneWork"));

		// Enter password
		sugar().users.editView.getControl("passwordTab").click();
		VoodooUtils.waitForReady();
		newPasswordCtrl.set(userData.get("password"));
		confirmPasswordCtrl.set(userData.get("password"));

		// Click on save
		sugar().users.editView.getControl("save").click();
		VoodooUtils.waitForReady();

		// Verify that the Error message "The username 'entered username' already exists,Duplicate usernames are not allowed.Change the username to be unique" should be displayed
		errorMessageCtrl.assertEquals(userData.get("errorMessage"), true);

		// Verify that the details filled by the user should not be deleted
		newPasswordCtrl.assertEquals("", false);
		confirmPasswordCtrl.assertEquals("", false);

		// Navigate back to profile tab
		sugar().users.editView.getControl("profileTab").click();
		VoodooUtils.waitForReady();

		// Verify that the Error message "The username 'entered username' already exists,Duplicate usernames are not allowed.Change the username to be unique" should be displayed
		errorMessageCtrl.assertEquals(userData.get("errorMessage"), true);

		// Verify that the details filled by the user should not be deleted
		firstNameCtrl.assertEquals(userData.get("firstName"), true);
		lastNameCtrl.assertEquals(userData.get("lastName"), true);
		userNameCtrl.assertEquals(userData.get("userName"), true);
		emailAddressCtrl.assertEquals(userData.get("emailAddress"), true);
		titleCtrl.assertEquals(userData.get("title"), true);
		departmentCtrl.assertEquals(userData.get("department"), true);
		phoneWorkCtrl.assertEquals(userData.get("phoneWork"), true);
		sugar().users.editView.getControl("reportsTo").assertEquals(userData.get("reportsTo"), true);
		VoodooUtils.focusDefault();

		// Cancel the edit view of the user
		sugar().users.editView.cancel();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}