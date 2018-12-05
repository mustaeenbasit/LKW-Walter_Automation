package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_29920 extends SugarTest {
	VoodooControl settingBtn, mailAccount, doneCtrl;

	public void setup() throws Exception {				
		sugar().login();
	}

	/**
	 * Verify that "Data error" message should not be displayed on Outgoing SMTP Mail Servers in Emails module
	 * @throws Exception
	 */
	@Test
	public void Emails_29920_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to email module
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Navigating to outbound email configuration window 
		// TODO: VOOD-1078
		settingBtn = new VoodooControl("button", "id", "settingsButton");
		settingBtn.click();
		mailAccount = new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a");
		mailAccount.click();
		new VoodooControl("input", "id", "outbound_email_add_button").click();

		// Entering data in outbound email form and click Done.
		FieldSet emailSetupData = testData.get("env_email_setup").get(0);
		new VoodooControl("input", "id", "mail_name").set(emailSetupData.get("userName"));
		new VoodooControl("button", "id", "gmail-button").click();
		new VoodooControl("input", "id", "mail_smtpuser").set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "mail_smtppass").set(emailSetupData.get("password"));
		new VoodooControl("input", "css", "#outboundEmailForm .button").click();
		VoodooUtils.waitForReady();

		// Verifying the Outbound Email Form gets closed after clicking on Done 
		new VoodooControl("form", "id", "outboundEmailForm").assertVisible(false);

		// Verifying the Settings Dialog page appears after clicking on Done 
		new VoodooControl("div", "id", "settingsDialog").assertVisible(true);

		// Verifying the Outbound Email setting gets saved
		new VoodooControl("div", "css", "#outboundAccountsTable .yui-dt-even .yui-dt-col-name div").assertEquals(emailSetupData.get("userName"), true);
		doneCtrl = new VoodooControl("input", "css", "#settingsTabDiv div div:nth-of-type(2) input[value='   Done   ']");
		doneCtrl.click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}