package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_28967 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Error message should appear while save user without password.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_28967_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > UserManagement 
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("userManagement").click();

		// Click on QAuser
		sugar.users.listView.clickRecord(2);
		VoodooUtils.focusFrame("bwc-frame");

		// Click on 'Copy' button from edit action drop-down list 
		sugar.users.detailView.openPrimaryButtonDropdown();
		sugar.users.detailView.getControl("copyButton").click();
		VoodooUtils.waitForReady();

		// Fill all required fields except Password
		sugar.users.editView.getEditField("userName").set(testName);
		sugar.users.userPref.getControl("save").click();
		FieldSet customFS = testData.get(testName).get(0);
		
		// TODO: VOOD-1588 -Need lib support for asserting required input fields error messages in BWC modules
		// Verify that Navigate to Password tab and "Missing required field:New Password" error message should be shown
		new VoodooControl("div", "css", "#generate_password td.dataField div.required.validation-message").assertContains(customFS.get("error_msg"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}