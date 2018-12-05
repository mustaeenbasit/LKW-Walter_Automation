package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20223 extends SugarTest {
	DataSource emailSetup;
	
	public void setup() throws Exception {
		sugar().login();
		
		emailSetup = testData.get(testName);
	}

	/**
	 * Verify SMTP mail server settings are saved 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20223_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToEmailSettings();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-672
		new VoodooControl("button", "id", "other-button").click();
		new VoodooControl("select", "id", "mail_smtpssl").set(emailSetup.get(0).get("mail_smtpssl"));
		new VoodooControl("input", "id", "mail_smtpport").set(emailSetup.get(0).get("mail_smtpport"));
		new VoodooControl("input", "id", "mail_smtpserver").set(emailSetup.get(0).get("mail_smtpserver"));
		new VoodooControl("input", "id", "mail_smtpuser").set(emailSetup.get(0).get("userName"));
		if(sugar().admin.emailSettings.getControl("passwordLink").queryVisible()){
			sugar().admin.emailSettings.getControl("passwordLink").click();
		}
		new VoodooControl("input", "id", "mail_smtppass").set(emailSetup.get(0).get("password"));

		sugar().admin.emailSettings.getControl("save").click();
		VoodooUtils.focusDefault(); // focus back to the default content
		VoodooUtils.waitForAlertExpiration(100000); // Wait for Loading... message to expire

		// Now Verify Values
		sugar().admin.navToEmailSettings();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-672
		new VoodooControl("button", "id", "other-button").click();
		new VoodooControl("option", "css", "select#mail_smtpssl option[selected][value='2']").assertExists(true);
		new VoodooControl("input", "id", "mail_smtpport").assertAttribute("value", emailSetup.get(0).get("mail_smtpport"), true);
		new VoodooControl("input", "id", "mail_smtpserver").assertAttribute("value", emailSetup.get(0).get("mail_smtpserver"), true);

		sugar().admin.emailSettings.getControl("cancel").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
