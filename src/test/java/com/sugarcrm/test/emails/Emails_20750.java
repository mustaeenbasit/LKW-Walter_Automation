package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_20750 extends SugarTest {
	DataSource emailSetup;
		
	public void setup() throws Exception {
		sugar.login();
		emailSetup = testData.get(testName);
		
		// configure Admin->Email Settings
		sugar.admin.setEmailServer(emailSetup.get(0));
	}

	/**
	 * Edit Outgoing SMTP Mail Server in Emails module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20750_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		// TODO: VOOD-672
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("img", "css", "#outboundAccountsTable td.yui-dt-col-edit div img").waitForVisible();
		new VoodooControl("img", "css", "#outboundAccountsTable td.yui-dt-col-edit div img").click();
		
		// Verify the "Outgoing Mail Server Properties" window pop up open
		new VoodooControl("div", "id", "outboundDialog_h").assertContains("Outgoing Mail Server Properties", true);
		new VoodooControl("input", "id", "mail_smtpuser").set("");
		new VoodooControl("input", "css", "#outboundEmailForm tr:nth-child(5) input:nth-child(1)").click();
		// Alert shown, if required field is missing
		new VoodooControl("div", "id", "sugarMsgWindow_h").assertContains("Missing required field", true);
		new VoodooControl("button", "css", "#sugarMsgWindow button").click();
		
		new VoodooControl("input", "id", "mail_smtpuser").set(emailSetup.get(0).get("userName"));
		new VoodooControl("input", "css", "#outboundEmailForm tr:nth-child(5) input:nth-child(1)").click();
		VoodooUtils.pause(1000); // needs minor wait after clicking done button
		
		// Verify visibility of Settings window
		new VoodooControl("div", "css", "#settingsDialog .hd").assertContains("Settings", true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}