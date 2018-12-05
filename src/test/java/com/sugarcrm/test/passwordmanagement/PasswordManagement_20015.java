package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20015 extends SugarTest {
	UserRecord userRecord;
	public void setup() throws Exception {
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify the page is user detail view page after cancel
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20015_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		
		// Go to user profile page
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-987
		// Enter wrong password in "current password" field
		new VoodooControl("input", "css", "#old_password").set(customFS.get("oldPassword"));
		
		// Enter new password
		sugar().users.editView.getEditField("newPassword").set(customFS.get("newPassword"));
		sugar().users.editView.getEditField("confirmPassword").set(customFS.get("confirmPassword"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Click on PasswordTab
		// Error: Incorrect current password for user testUser. Re-enter password information.
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click(); // Click on Password Tab
		
		// Verify that current page is user edit view with error
		String errorMsg = String.format("%s%s%s", customFS.get("error_msg1"),"qauser",customFS.get("error_msg2"));
		new VoodooControl("span", "id", "error_pwd").assertContains(errorMsg, true);
		
		// Cancel editing password
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that current page is user detail view page with no error
		sugar().users.detailView.assertContains(errorMsg, false);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}