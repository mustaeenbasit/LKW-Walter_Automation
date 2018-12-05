package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_20765 extends SugarTest {
	FieldSet emailSetup;
	
	public void setup() throws Exception {
		emailSetup = testData.get(testName).get(0);
		sugar.login();
		
		// Set email settings in admin
		sugar.admin.setEmailServer(emailSetup);
		sugar.alerts.waitForLoadingExpiration();
	}

	/**
	 * Disable users to use the system outgoing mail account for outbound email
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20765_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		sugar.logout();

		// Now login as non admin user
		sugar.login(sugar.users.getQAUser());
		
		sugar.navbar.navToProfile();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		sugar.users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify "SMTP username" and "SMTP password" is empty in the Email Settings section
		sugar.admin.emailSettings.getControl("userName").assertEquals("", true);
		sugar.admin.emailSettings.getControl("password").assertEquals("", true);
		VoodooUtils.focusDefault();
		
		// TODO: VOOD-1099 -Library support needed for controls in admin/user > Inbound Email
		// Set email settings individually
		sugar.navbar.navToModule("Emails");		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		VoodooUtils.waitForAlertExpiration();
		
		// Verify warning message:"Warning: Missing username and password for outgoing mail account"		
		new VoodooControl("div", "id", "outboundAccountsTable").assertContains("Data error", false);
		new VoodooControl("div", "id", "outboundAccountsTable").assertContains("Warning: Missing username and password for outgoing mail account", true);
		new VoodooControl("input", "id", "addButton").click();
		sugar.alerts.waitForLoadingExpiration();

		// Verify pop up message. Warning: Missing username and password for outbound account.
		new VoodooControl("input", "id", "inbound_mail_smtpuser").assertExists(true);
		new VoodooControl("input", "id", "inbound_mail_smtppass").assertExists(true);
		new VoodooControl("input", "id", "inbound_mail_smtpuser").assertEquals("", true);
		new VoodooControl("input", "id", "inbound_mail_smtppass").assertEquals("", true);
		new VoodooControl("a", "css", "div#editAccountDialogue a.container-close").click();
		new VoodooControl("a", "css", "div#settingsDialog a.container-close").click();

		// TODO: VOOD-797 -Lib support to handle compose archive email in new email composer
		new VoodooControl("button", "id", "composeButton").click();
		VoodooUtils.waitForAlertExpiration();
		
		// Expected Result 4
		new VoodooControl("div", "id", "sugarMsgWindow").assertContains("Warning: Missing username and password for outgoing mail account", true);
		new VoodooControl("a", "css", "div#sugarMsgWindow a.container-close").click();
		
		VoodooUtils.focusDefault();
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}