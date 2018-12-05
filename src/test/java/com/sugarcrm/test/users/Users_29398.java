package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_29398 extends SugarTest {
	String lastName;

	public void setup() throws Exception {
		// Login as an Admin user
		sugar().login();
	}

	/**
	 * Verify that Next button on User Profile Page is not disabled after filling mandatory details
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_29398_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		FieldSet dashboardTitle = testData.get(testName).get(0);

		// Now navigate to admin->User management
		sugar().users.navToListView();

		// Now select 'create User' from navbar -> Users 
		sugar().navbar.selectMenuItem(sugar().users, "createNewUser");
		VoodooUtils.focusFrame("bwc-frame");

		// Define Values
		String userName = sugar().users.getDefaultData().get("userName");
		String firstName = sugar().users.getDefaultData().get("firstName");
		lastName = sugar().users.getDefaultData().get("lastName");
		String emailAddress = sugar().users.getDefaultData().get("emailAddress");
		String password = sugar().users.getDefaultData().get("newPassword");

		// Fill 'User name', 'first name', 'last name'
		sugar().users.editView.getEditField("userName").set(userName);
		sugar().users.editView.getEditField("firstName").set(firstName);
		sugar().users.editView.getEditField("lastName").set(lastName);

		// Now Fill Email Address and mark this as Primary
		sugar().users.editView.getEditField("emailAddress").set(emailAddress);

		// Now add the same Email address again in new field and mark it as 'reply to'
		// TODO: VOOD-563
		new VoodooControl("button", "id", "Users0_email_widget_add").click();
		new VoodooControl("input", "id", "Users0emailAddress1").set(emailAddress);
		new VoodooControl("input", "id", "Users0emailAddressReplyToFlag1").click();

		// Enter password
		sugar().users.editView.getControl("passwordTab").click();
		VoodooUtils.waitForReady();
		sugar().users.editView.getEditField("newPassword").set(password);
		sugar().users.editView.getEditField("confirmPassword").set(password);

		// Set 'New User Wizard' field to 'True'
		sugar().users.editView.getControl("advancedTab").click();
		VoodooUtils.waitForReady();
		sugar().users.editView.getEditField("newUserWizard").set("true");

		// Now click on Save button to Save the changes 
		sugar().users.editView.getControl("save").click();
		VoodooUtils.waitForReady();
		sugar().users.editView.getControl("confirmCreate").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(40000); // Extra wait needed

		// Now Log in using User Credentials
		sugar().logout();
		sugar().loginScreen.getControl("loginUserName").set(userName);
		sugar().loginScreen.getControl("loginPassword").set(password);
		sugar().loginScreen.getControl("login").click();
		VoodooUtils.waitForReady(40000); // Extra wait needed

		// Verify the "First Name", "Last Name", "Email" fields should be pre-populated with data
		sugar().newUserWizard.getControl("firstName").assertAttribute("value", firstName, true);
		sugar().newUserWizard.getControl("lastName").assertAttribute("value", lastName, true);
		sugar().newUserWizard.getControl("emailAddress").assertAttribute("value", emailAddress, true);

		// Verify that the 'Next' button on User Profile Page should be enabled
		VoodooControl nextButtonCtrl = sugar().newUserWizard.getControl("nextButton");
		nextButtonCtrl.waitForVisible();
		nextButtonCtrl.assertAttribute("class", "disabled", false);
		sugar().newUserWizard.clickNextButton();
		sugar().newUserWizard.clickNextButton();
		sugar().newUserWizard.clickStartSugar();
		VoodooUtils.waitForReady(60000); // Needed extra wait
		sugar().dashboard.getControl("firstDashlet").waitForVisible();

		// Verify the successful login
		sugar().dashboard.getControl("dashboardTitle").assertContains(dashboardTitle.get("dashboardTitle"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}