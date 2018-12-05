package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24767 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Only see Email Settings notification when it exists
	 * @throws Exception
	 */
	@Test
	public void Users_24767_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to User profile
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		FieldSet customFS = testData.get(testName).get(0);

		// TODO: VOOD-563 - need lib support for user profile edit page
		// Verify, See email settings notification in user profile area
		VoodooControl errorMessage = new VoodooControl("p", "css", "#content p.error");
		errorMessage.assertContains(customFS.get("errorMsg"), true);
		VoodooUtils.focusDefault();

		// Verify, see email settings notification with red background and number in it, ex. 1
		// TODO: VOOD-2063 - Need lib support for Notifications and its related views(list view, record view)
		VoodooControl notificationCtrl = new VoodooControl("button", "css", ".dropdown.notification-list button");
		notificationCtrl.assertCssAttribute(customFS.get("cssAttribute"), customFS.get("cssValue"));
		notificationCtrl.assertContains(customFS.get("notificationNumber"), true);

		// Click on the notification
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#content p.error a").click();
		VoodooUtils.focusDefault();

		// Add email setup via Admin-> Email Settings and save changes
		FieldSet emailSetupData = testData.get("env_email_setup").get(0);
		sugar().admin.setEmailServer(emailSetupData);

		// Go to User Profile
		sugar().navbar.navToProfile();

		// Verify, not see notification in display
		errorMessage.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}