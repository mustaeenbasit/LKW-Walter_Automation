package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_20794 extends SugarTest {
	FieldSet emailSettings;
	DataSource ds;
	
	UserRecord userJim;
	ContactRecord myContact;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar.login();
		
		emailSettings = testData.get(testName).get(0);
		ds = testData.get(testName+"_user");

		// 1. Set Outbound Mail
		sugar.admin.setEmailServer(emailSettings);
		
		// 2. Create and Login as user Jim
		userJim = (UserRecord) sugar.users.create(ds.get(0));
		
		sugar.logout();
		
		sugar.login(userJim);

		// 3. Configure email settings for Jim
		// TODO: VOOD-672
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		VoodooUtils.pause(3000);
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		VoodooUtils.pause(3000);
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(ds.get(0).get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSettings.get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(emailSettings.get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "ie_from_addr").set(emailSettings.get("userName"));
		new VoodooControl("input", "id", "saveButton").click();

		// Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);
		
		// Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);
		
		// Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);
		
		VoodooUtils.pause(3000); // Let action complete. Could not find suitable waitForxxx control.
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();

		if (VoodooUtils.isDialogVisible())
			VoodooUtils.acceptDialog();
		
		VoodooUtils.focusDefault();

		sugar.logout();

		// Now login as Admin
		sugar.login();
	}

	/**
	 * Verify that assigned user is able to receive email when Assignment Notifications option is set 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20794_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Contact record and assign it to Jim
		myAccount = (AccountRecord) sugar.accounts.api.create();
		myContact = (ContactRecord) sugar.contacts.api.create();

		FieldSet fs = new FieldSet();
		fs.put("relAccountName", myAccount.getRecordIdentifier());
		fs.put("relAssignedTo", ds.get(0).get("userName"));
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		sugar.contacts.recordView.edit();
		sugar.contacts.recordView.showMore();
		sugar.contacts.recordView.setFields(fs);
		sugar.alerts.confirmAllAlerts();
		sugar.contacts.recordView.save();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.pause(8000); // This save requires more time to finish, otherwise
								 // a warning for pending save appears
		
		sugar.logout();
		
		// Now login as Jim and check email for the assignment mail
		sugar.login(userJim);

		// TODO: VOOD-685
		sugar.navbar.navToModule("Emails");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.pause(5000);
		new VoodooControl("button", "id", "checkEmailButton").click();
		if (!(new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").queryExists())) {
			new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'"+ds.get(0).get("userName")+"')]").waitForVisible();
			new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'"+ds.get(0).get("userName")+"')]").click();
		}
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").click();
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'SugarCRM Contact - Mr. "+myContact.get("firstName")+' '+myContact.get("lastName")+"')]").click();

		VoodooUtils.pause(10000); // Allow mail to load
		
		// Check Expected Result
		String textToCheck1 = "SugarCRM Contact - Mr. "+myContact.get("firstName")+' '+myContact.get("lastName");
		new VoodooControl("td", "css", "div#listBottom table > tbody > tr:nth-child(2) > td.displayEmailValue").assertContains(textToCheck1, true);

		// TODO: VOOD-1035
		// VoodooUtils.focusFrame("displayEmailFramePreview");
		// String textToCheck2 = "Administrator has assigned a Contact to "+ds.get(0).get("firstName")+' '+ds.get(0).get("lastName");
		// new VoodooControl("body", "css", "body").assertAttribute("innerHTML", textToCheck2, true);
				
		VoodooUtils.focusDefault();	
		
		// Delete this mail.
		// TODO: VOOD-919 for Library controls for removing email
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").click();
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'SugarCRM Contact - Mr. "+myContact.get("firstName")+' '+myContact.get("lastName")+"')]").click();
		// wait for 'Reply' button which appears only when the mail fully loads
		new VoodooControl("button", "css", "div.displayEmailValue > button:nth-child(1)").waitForVisible(30000);
		// 'Delete' button
		new VoodooControl("button", "xpath", "//*[@id='_blank']/div/div[2]/button[4]").click();
		if (VoodooUtils.isDialogVisible())
				VoodooUtils.acceptDialog();
		VoodooUtils.pause(15000); // Let delete complete. no suitable waitForxxx control could be located
		
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}