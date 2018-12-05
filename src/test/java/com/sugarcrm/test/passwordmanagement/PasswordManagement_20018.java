package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20018 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Black font message displays when not set email server
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20018_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		
		// Go to Admin > PasswordManagement
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").click();

		// TODO: VOOD-948
		VoodooControl forgotPasswordCheckbox = new VoodooControl("input", "id", "forgotpassword_checkbox");
		if(!forgotPasswordCheckbox.isChecked())
			// If uncheck then check "Enable System-Generated Passwords Feature"
			forgotPasswordCheckbox.click();
		
		// Check "Enable Forgot Password feature"
		new VoodooControl("input", "id", "SystemGeneratedPassword_checkbox").click();
		
		// Verify that message displays with black font for "Enable System-Generated Passwords Feature"
		new VoodooControl("td", "id", "SystemGeneratedPassword_warning").queryContains(customFS.get("warningMsg"), true);
		
		// Verify that message displays with black font for "Enable Forgot Password feature"
		new VoodooControl("td", "id", "SystemGeneratedPassword_warning2").queryContains(customFS.get("warningMsg"), true);
		
		// Save updation of PasswordManagement settings 
		new VoodooControl("input", "css", "input[title='Cancel']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}