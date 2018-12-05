package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Admin_28935 extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-1200 - Authentication failed on calling Users default data
		// Once resolved, it should create via API
		myUser = (UserRecord) sugar().users.create();
	}

	/**
	 * Verify that User is able to login while reset password as Test123
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_28935_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Go to Admin -> User Management
		sugar().admin.navToAdminPanelLink("userManagement");
		myUser.navToRecord();

		// Click 'Edit' and change Password of the user
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
		VoodooUtils.waitForReady();

		// Type New Password and Confirm Password and click save
		sugar().users.editView.getEditField("newPassword").set(customData.get("newPassword"));
		sugar().users.editView.getEditField("confirmPassword").set(customData.get("newPassword"));
		sugar().users.editView.getControl("save").click();
		sugar().users.editView.getControl("confirmCreate").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Log-out from sugarCRM and login again using same password
		sugar().logout();
		myUser.put("password", customData.get("newPassword"));
		sugar().login(myUser);

		// Verify that login is successful and user redirected on Home Page
		// TODO: VOOD-1884: Differentiate modules in view element hooks. Once resolved commented line will work
		// sugar().home.dashboard.assertVisible(true);
		new VoodooControl("div", "css", ".layout_Home .dashboard").assertVisible(true);

		// TODO: VOOD-999: Need a unique, consistent, and does not get translated when the language is changed (Valid testability hooks)
		new VoodooControl("button", "css", "[title='" + myUser.get("firstName") + "']").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}