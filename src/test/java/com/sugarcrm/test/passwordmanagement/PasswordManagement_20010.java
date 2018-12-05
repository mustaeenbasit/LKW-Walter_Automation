package com.sugarcrm.test.passwordmanagement;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20010 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify selected Email Templates is saved for the password setting
	 * @throws Exception
	 */
	@Test
	public void PasswordManagement_20010_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to password management
		sugar().admin.navToAdminPanelLink("passwordManagement");

		// TODO: VOOD-993
		// Set Email Templates, Generated Password and Lost Password
		FieldSet customFS = testData.get(testName).get(0);
		VoodooUtils.focusFrame("bwc-frame"); 
		VoodooControl generatePasswordTml = new VoodooControl("select", "id", "generatepasswordtmpl");
		generatePasswordTml.click();
		VoodooControl selectGenratedPassword = new VoodooControl("option","css","#generatepasswordtmpl option:nth-child(3)");
		selectGenratedPassword.click();
		VoodooControl lostPasswordTmpl = new VoodooControl("select", "id", "lostpasswordtmpl");
		lostPasswordTmpl.click();
		VoodooControl selectLostPassword = new VoodooControl("option","css","#lostpasswordtmpl option:nth-child(3)");
		selectLostPassword.click();
		VoodooControl saveCtrl = sugar().admin.passwordManagement.getControl("save");
		saveCtrl.click();

		// Go to password Management
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl passwordManagementCtrl = sugar().admin.adminTools.getControl("passwordManagement");
		passwordManagementCtrl.click();
		VoodooUtils.waitForReady();

		// Verify the Email Template be the selected one.
		generatePasswordTml.assertContains(customFS.get("selectedGenratedPasswordEmail"), true);
		lostPasswordTmpl.assertContains(customFS.get("forgotPasswordEmail"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}