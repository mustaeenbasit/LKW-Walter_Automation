package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Emails_26687 extends SugarTest {
	FieldSet customData, emailSetup;
	ContactRecord myContact;

	public void setup() throws Exception {
		// Create contact record
		myContact = (ContactRecord) sugar.contacts.api.create();
		emailSetup = testData.get(testName).get(0);
		customData = testData.get(testName+"_1").get(0);
		sugar.login();
		
		// configure Admin->Email Settings
		sugar.admin.setEmailServer(emailSetup);
		
		// Set email settings individually
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailSetup.get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSetup.get("userName")); // To allow to fetch email 
		new VoodooControl("input", "id", "email_password").set(emailSetup.get("password")); // To allow to fetch email 
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "reply_to_addr").set(emailSetup.get("userName"));
		new VoodooControl("input", "id", "saveButton").click();
		
		// Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		// Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		// Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		VoodooUtils.pause(1000); // Let action complete. Could not find suitable waitForxxx control.
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.refresh(); // Needed to refresh email module after email account is configured.
		VoodooUtils.focusDefault();
	}

	/**
	 * Unlink is not available in emails sub panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_26687_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// edit record and add email id
		myContact.navToRecord();
		sugar.contacts.recordView.edit();
		sugar.contacts.createDrawer.getEditField("emailAddress").set(emailSetup.get("userName"));
		sugar.contacts.recordView.save();
		sugar.alerts.waitForLoadingExpiration();

	    StandardSubpanel emailSub = sugar.contacts.recordView.subpanels.get(sugar.emails.moduleNamePlural);
	    emailSub.composeEmail();
	    
		// TODO: VOOD-809
		sugar.alerts.waitForLoadingExpiration();				
		new VoodooControl("input", "css", "input[name='subject']").waitForVisible();		
		new VoodooControl("input", "css", "input[name='subject']").set(customData.get("subject"));
		new VoodooControl("a", "css", "a.rowaction[name='send_button']").click();
		sugar.alerts.confirmAllAlerts();
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusDefault();
		
		myContact.navToRecord();
		
		// Verify that email record is present in the sub-panel and unlink button is not available
		// TODO: VOOD-1100 -verify method is not working in Calls Subpanel
		FieldSet subjectData = new FieldSet();
		subjectData.put("subject", customData.get("subject"));
		emailSub.assertContains(customData.get("subject"), true);
		new VoodooControl("a", "css", "div.flex-list-view.left-actions.right-actions span.actions.btn-group.list > a").assertExists(false);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}	
}