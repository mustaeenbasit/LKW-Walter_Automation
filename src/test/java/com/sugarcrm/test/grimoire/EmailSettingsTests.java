package com.sugarcrm.test.grimoire;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;

public class EmailSettingsTests extends SugarTest {
	FieldSet emailSettings = new FieldSet();

	public void setup() throws Exception {
		// Set email settings
		emailSettings = testData.get("env_email_setup").get(0);
		sugar().login();
		sugar().admin.setEmailServer(emailSettings);
	}

	@Test
	public void EmailSettingsTests_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify email settings data fields
		sugar().admin.navToEmailSettings();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.emailSettings.getControl("userName").assertEquals(emailSettings.get("userName"), true);
		sugar().admin.emailSettings.getControl("passwordLink").assertVisible(true);
		Assert.assertTrue("Allow users checkbox is unchecked.", sugar().admin.emailSettings.getControl("allowAllUsers").isChecked());
		sugar().admin.emailSettings.getControl("cancel").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}