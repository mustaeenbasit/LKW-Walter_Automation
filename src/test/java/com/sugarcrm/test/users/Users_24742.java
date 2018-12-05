package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24742 extends SugarTest { 
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * User_Create_Email_Validation
	 * @throws Exception
	 */
	@Test
	public void Users_24742_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to User management in Admin and select 'CreateNewUser'
		sugar().admin.navToAdminPanelLink("userManagement");
		sugar().navbar.selectMenuItem(sugar().users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame");

		// Enter values for "First Name", "Last Name", "User Name", "Password" and "Confirm Password" fields.
		// TODO: VOOD-563
		sugar().users.editView.getEditField("userName").set(sugar().users.getDefaultData().get("userName"));
		new VoodooControl("input", "id", "first_name").set(sugar().users.getDefaultData().get("firstName"));
		new VoodooControl("input", "id", "last_name").set(sugar().users.getDefaultData().get("lastName"));
		sugar().users.editView.getControl("passwordTab").click();
		new VoodooControl("input", "id", "new_password").set(sugar().users.getDefaultData().get("password"));
		new VoodooControl("input", "id", "confirm_pwd").set(sugar().users.getDefaultData().get("confirmPassword"));

		// Enter invalid value for Email field and click save
		FieldSet emailData = testData.get(testName).get(0);
		sugar().users.editView.getControl("profileTab").click();
		new VoodooControl("input", "id", "Users0emailAddress0").set(emailData.get("invalidEmail"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Asserting the invalid email error message
		// TODO: VOOD-1588 - Need lib support for asserting required input fields error messages in BWC modules
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".required.validation-message").assertEquals(emailData.get("errorMessage"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}