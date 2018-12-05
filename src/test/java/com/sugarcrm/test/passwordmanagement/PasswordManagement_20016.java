package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20016 extends SugarTest {
	VoodooControl passwordManagementCtrl, systemPassCheckBox, saveTemplateCreation;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Administrator set "System-Generated Password Expiration" none login times
	 * 
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20016_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		
		// Go to Admin > PasswordManagement
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		passwordManagementCtrl = sugar().admin.adminTools.getControl("passwordManagement");
		passwordManagementCtrl.click();

		// TODO: VOOD-948
		// Check "Enable System-Generated Passwords Feature" 
		systemPassCheckBox = new VoodooControl("input", "id", "SystemGeneratedPassword_checkbox");
		systemPassCheckBox.click();
		new VoodooControl("input", "css", "[name='passwordsetting_systexpirationtime']").set("");
		
		// Cancel updation of PasswordManagement settings 
		saveTemplateCreation = new VoodooControl("input", "css", "input[title='Save']");
		saveTemplateCreation.click();
		
		// Verify the red font message displays "Missing required field:Specify the time after which the password will expire."
		new VoodooControl("div", "css", "#syst_generated_pwd_table .required.validation-message").assertEquals(customFS.get("errorMsg"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}