package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20022 extends SugarTest {
	FieldSet userAndPasswordData = new FieldSet();
	UserRecord myUser;
	VoodooControl userTypeCtrl;

	public void setup() throws Exception {
		userAndPasswordData = testData.get(testName).get(0);
		sugar().login();

		// Create another admin user		
		myUser = (UserRecord)sugar().users.create();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-987
		userTypeCtrl = new VoodooControl("select", "id", "UserType");
		userTypeCtrl.set(userAndPasswordData.get("userType"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
	}

	/**
	 * Change Other Admin Passwords 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20022_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to users management and edit created admin user
		myUser.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the password tab display on the user's editview page
		VoodooControl passwordTab = sugar().users.editView.getControl("passwordTab");
		passwordTab.assertContains(userAndPasswordData.get("passwordTab"), true);

		//Go to Password tab and Change the password of the newly created admin user and save
		passwordTab.click();
		sugar().users.editView.getEditField("newPassword").set(userAndPasswordData.get("newPassword"));
		sugar().users.editView.getEditField("confirmPassword").set(userAndPasswordData.get("newPassword"));

		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-994
		// Verify that the new password is saved
		new VoodooControl("div", "css", "#sugarMsgWindow .bd").assertContains(userAndPasswordData.get("warningMessage"), true);
		sugar().users.editView.getControl("confirmCreate").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Logout from the current Admin user
		sugar().logout();

		// Verify that the user can login using the new password
		myUser.put("password", userAndPasswordData.get("newPassword"));

		sugar().login(myUser);
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		userTypeCtrl.assertContains(userAndPasswordData.get("userType"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}