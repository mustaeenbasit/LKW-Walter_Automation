package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest; 

public class Emails_26601 extends SugarTest {
	ContactRecord testContact;
	FieldSet customData, emailSettings;
	String testLastName = null, testEmail = null, emailSubject = null, noDataMessage = null;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);		
		testLastName = customData.get("lastName");
		emailSubject = customData.get("email_subject");		
		noDataMessage = customData.get("message");
		testEmail = customData.get("emailAddress");
		emailSettings = testData.get(testName+"_1").get(0);
		
		// Create contact
		FieldSet fs = new FieldSet();
		fs.put("lastName", testLastName);
		fs.put("emailAddress", testEmail);
		testContact = (ContactRecord) sugar.contacts.api.create(fs);
		sugar.login();		
		
		// Set email settings in admin
		sugar.admin.setEmailServer(emailSettings);
		
		// Set email settings individually
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailSettings.get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSettings.get("userName")); // To allow to fetch email 
		new VoodooControl("input", "id", "email_password").set(emailSettings.get("password")); // To allow to fetch email 
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "reply_to_addr").set(emailSettings.get("userName"));
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
	 * Open email composer when click on email address link 
	 * @throws Exception
	 */
	@Test
	public void Emails_26601_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		StandardSubpanel emailSub = sugar.contacts.recordView.subpanels.get(sugar.emails.moduleNamePlural);
		testContact.navToRecord();
		
		// Open email composer from email address link
		// TODO: VOOD-843 - Lib support to handle email composer UI
		new VoodooControl("a", "css", ".fld_email.detail a").click(); // click on email ID
		new VoodooControl("input", "css", "span.inherit-width.fld_email_config.edit input").waitForVisible(5000);
		new VoodooControl("span", "css", ".fld_to_addresses.edit").assertContains(testLastName, true);
		// Cancel email creation
		new VoodooControl("a", "css", "div.layout_Emails span.fld_cancel_button.detail a").click();
		VoodooUtils.waitForAlertExpiration();
		
		// TODO: VOOD-898 - Lib support that has ability to click on any links of a record in the listview
		new VoodooControl("a", "css", ".fld_email.detail a").click();
		new VoodooControl("input", "css", ".fld_subject.edit input").waitForVisible(5000);
		new VoodooControl("span", "css", ".fld_to_addresses.edit").assertContains(testLastName, true);
		
		// TODO: VOOD-808 - Can not input text in iframe (blank) in Email Composer's body
		new VoodooControl("span", "css", "span.fld_template_button").click();
		new VoodooControl("div", "css", "div[data-original-title='Forgot Password email']").waitForVisible();
		new VoodooControl("input", "css", "table.search-and-select tr td span.fld_EmailTemplates_select").click();
		new VoodooControl("div", "css", "div.alert-warning").waitForVisible();
		sugar.alerts.confirmAllAlerts();
		new VoodooControl("input", "css", ".fld_subject.edit input").set(emailSubject);
		
		// Send created email message
		new VoodooControl("a", "css", ".fld_send_button.detail a").click();
		sugar.alerts.confirmAllAlerts();
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusDefault();
		
		// Verify that email is present in the sub-panel
		testContact.navToRecord();		
		
		// TODO: VOOD-1100 -verify method is not working in Calls Subpanel
		emailSub.assertContains(customData.get("email_subject"), true);
		sugar.alerts.waitForLoadingExpiration();
		
		//TODO: VOOD-919 - lib support for removing email records	
        // Remove  created email 	
		new VoodooControl("a", "css", "div.layout_Emails span.fld_name a").click();
		new VoodooControl("iframe", "css", "#bwc-frame").waitForVisible(3000);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", "span.ab").click();
		new VoodooControl("a", "id", "delete_button").waitForVisible();
		new VoodooControl("a", "id", "delete_button").click();
		VoodooUtils.pause(1000); // Required for populate dialog
		VoodooUtils.acceptDialog();		
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusDefault();
	
		// Verify the email record is deleted
		testContact.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		emailSub.expandSubpanel();
		emailSub.isEmpty();
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}
		
	public void cleanup() throws Exception {}
}
