package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_30595 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Test Case 30595: Verify that duplicate warning message should not be seen when creating and updating users
	 * @throws Exception
	 */
	@Test
	public void Users_30595_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		UserRecord qaUser = new UserRecord(sugar().users.getQAUser());

		// Scenario 1: Verifying Warning message while creating new user
		sugar().navbar.navToProfile();
		sugar().navbar.selectMenuItem(sugar().users, "createNewUser");
		
		// Click on save without entering any data
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1588
		// Required error message for bwc module
		VoodooControl userNameLabelCtrl = new VoodooControl("td", "css", "#LBL_USER_INFORMATION tr td:nth-child(2)");
		VoodooControl lastNameLabelCtrl = new VoodooControl("td", "css", "#LBL_USER_INFORMATION tr:nth-child(2) td:nth-child(4)");
		String invalidUserNameErrorMessage = customData.get("userNameValidError") + "\n" + customData.get("userNameDuplicateError");
		String invalidLastNameErrorMessage = customData.get("lastNameValidError") + "\n" + customData.get("lastNameDuplicateError");
		
		// Verifying duplicate error message are not showing in User Name field.
		userNameLabelCtrl.assertEquals(invalidUserNameErrorMessage, false);

		// Verifying correct error message is showing in User Name field.
		userNameLabelCtrl.assertEquals(customData.get("userNameValidError"), true);

		// Verifying duplicate error message are not showing in Last Name field.
		lastNameLabelCtrl.assertEquals(invalidLastNameErrorMessage, false);

		// Verifying correct error message is showing in Last Name field.
		lastNameLabelCtrl.assertEquals(customData.get("lastNameValidError"), true);
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();

		// Scenario 2: Verifying Warning message when editing existing user.
		// Navigate to qauser
		qaUser.navToRecord();
		
		// Edit the qauser
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Removing filled info in User Name and Last Name field
		sugar().users.editView.getEditField("userName").set("");
		sugar().users.editView.getEditField("lastName").set("");
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verifying duplicate error message are not showing in User Name field.
		userNameLabelCtrl.assertEquals(invalidUserNameErrorMessage, false);

		// Verifying correct error message is showing in User Name field.
		userNameLabelCtrl.assertEquals(customData.get("userNameValidError"), true);

		// Verifying duplicate error message are not showing in Last Name field.
		lastNameLabelCtrl.assertEquals(invalidLastNameErrorMessage, false);

		// Verifying correct error message is showing in Last Name field.
		lastNameLabelCtrl.assertEquals(customData.get("lastNameValidError"), true);
		VoodooUtils.focusDefault();
		sugar().users.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}