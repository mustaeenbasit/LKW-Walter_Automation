package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_20770 extends SugarTest {
	FieldSet customData, emailSettings, fs;
	UserRecord qaUser;
	ContactRecord myContact;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		emailSettings = testData.get(testName+"_email").get(0);
		qaUser =  new UserRecord(sugar.users.getQAUser());
		sugar.login();
		
		// Set email settings in admin
		sugar.admin.setEmailServer(emailSettings);
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// Admin Logout, QAUser Login
		sugar.logout();
		qaUser.login();
		
		// TODO: VOOD-672 Need Lib support for Email settings
		// Set email settings individually
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(customData.get("userName"));
		new VoodooControl("input", "id", "email_user").set(customData.get("emailAddress")); // To allow to fetch email 
		new VoodooControl("input", "id", "email_password").set(customData.get("password")); // To allow to fetch email 
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "reply_to_addr").set(customData.get("emailAddress"));
		new VoodooControl("input", "id", "saveButton").click();
		
		// Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		// Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		// Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		VoodooUtils.pause(3000); // Let action complete. Could not find suitable waitForxxx control.
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();

		// Create a Contact record
		myContact = (ContactRecord) sugar.contacts.api.create();
	}

	/**
	 * Verify that Email can be successfully composed from Contacts module list view and send out
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20770_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.contacts.navToListView();
		sugar.contacts.listView.toggleSelectAll();
		
		// Select Email
		new VoodooControl("a", "css", "span.list div.btn-group > a.btn.dropdown-toggle").click();
		new VoodooControl("a", "name", "mass_email_button").click();

		// Verify that Compose Email page is open
		new VoodooControl("span", "css", "div.layout_Emails span.module-title").assertContains("Compose Email", true);

		// Select multiple emails from Address Book
		new VoodooControl("a", "css", "a[data-name='to_addresses']").click();
		VoodooUtils.pause(3000);
		
		new VoodooControl("input", "css", "div.layout_Emails.drawer.transition.active div.btn.checkall input").set("true");
		new VoodooControl("a", "css", "span.fld_done_button.compose-addressbook-headerpane a.btn.btn-primary").click();
		
		// Verify that multiple emails appear in TO textbox
		new VoodooControl("span", "xpath", "//span[@class='fld_to_addresses edit']/div/ul/li[contains(.,'qauser')]/div/span").assertExists(true);		
		new VoodooControl("span", "xpath", "//span[@class='fld_to_addresses edit']/div/ul/li[contains(.,'Administrator')]/div/span").assertExists(true);

		// Remove Administrator from TO list
		new VoodooControl("a", "xpath", "//span[@class='fld_to_addresses edit']/div/ul/li[contains(.,'Administrator')]/a").click();

		// Input Subject and Body
		new VoodooControl("input", "name", "subject").set(customData.get("subject"));
		VoodooUtils.focusFrame(0);
		new VoodooControl("body", "id", "tinymce").set(customData.get("body"));
		VoodooUtils.focusDefault();
		VoodooUtils.pause(100);
		// Send Email
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.waitForAlertExpiration();
		
		// TODO: VOOD-685 Provide Inbound Email Module Support
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("button", "id", "checkEmailButton").click();
		if (!(new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").queryExists())) {
			new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'"+customData.get("userName")+"')]").waitForVisible();
			new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'"+customData.get("userName")+"')]").click();
		}
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").click();
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+customData.get("subject")+"')][1]").click();
		
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.pause(5000); // Let mail load completely

		// Check Expected Result
		String textToCheck1 = customData.get("subject");
		new VoodooControl("td", "css", "div#listBottom table > tbody > tr:nth-child(2) > td.displayEmailValue").waitForVisible();
		new VoodooControl("td", "css", "div#listBottom table > tbody > tr:nth-child(2) > td.displayEmailValue").assertContains(textToCheck1, true);
		
		// Delete this mail.
		new VoodooControl("button", "xpath", "//*[@id='_blank']/div/div[2]/button[4]").waitForVisible(100);
		new VoodooControl("button", "xpath", "//*[@id='_blank']/div/div[2]/button[4]").click();

		VoodooUtils.acceptDialog();		
		VoodooUtils.waitForAlertExpiration(); // Let delete complete. no suitable waitForxxx control could be located
		VoodooUtils.focusDefault();	
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}