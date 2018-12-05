package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_30064 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that error message should be displayed while copying same user name
	 * @throws Exception
	 */
	@Test
	public void Admin_30064_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin's user profile
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// Select copy from primary dropdown 
		// TODO: VOOD-563 need lib support for user profile edit page
		new VoodooControl("span", "css", ".sugar_action_button .ab").click();
		new VoodooControl("a", "id", "duplicate_button").click();

		// Enter same UserName as Admin in username fields
		FieldSet fs = testData.get(testName).get(0);
		sugar().users.editView.getEditField("userName").set(fs.get("userName"));

		// Type New Password and Confirm Password and click save
		sugar().users.editView.getControl("passwordTab").click();
		VoodooUtils.waitForReady();
		sugar().users.editView.getEditField("newPassword").set(fs.get("newPassword"));
		sugar().users.editView.getEditField("confirmPassword").set(fs.get("newPassword"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Asserting the error message on entering same username as Admin
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "id", "ajax_error_string").assertEquals(fs.get("errorMessage"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}