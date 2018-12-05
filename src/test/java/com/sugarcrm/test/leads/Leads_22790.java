package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_22790 extends SugarTest {
	DataSource usersDS = new DataSource();
	UserRecord myFirstUser, mySecondUser;
	FieldSet emailSetupData = new FieldSet();
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		usersDS = testData.get(testName + "_users");
		emailSetupData = testData.get("env_email_setup").get(0);
		customFS = testData.get(testName).get(0);
		sugar().login();
		sugar().admin.setEmailServer(emailSetupData);

		// TODO: VOOD-1200 - Once resolved users should create via API
		// Create 2 non-admin users. <member1>, <member2>.
		FieldSet member1 = usersDS.get(0);
		FieldSet member2 = usersDS.get(1);
		// Change member2's email in user profile to a real gmail email to
		// actually receive and check the test email
		member2.put("emailAddress", emailSetupData.get("userName"));
		myFirstUser = (UserRecord)sugar().users.create(member1);
		mySecondUser = (UserRecord)sugar().users.create(member2);
		sugar().logout();

		// Log in as first user
		sugar().login(myFirstUser);
		FieldSet fs = new FieldSet();
		fs.put("advanced_nameFormat", customFS.get("advance_name_format"));
		sugar().users.setPrefs(fs);
		sugar().logout(); // logout from first user

		// Login as second user
		sugar().login(mySecondUser);

		// Set name format
		sugar().users.setPrefs(fs); // fs reused from last setting

		// Set email settings individually
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1078
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSetupData.get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(emailSetupData.get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set(customFS.get("trash_mail"));
		new VoodooControl("input", "id", "sentFolder").set(customFS.get("sent_mail"));
		new VoodooControl("input", "id", "reply_to_addr").set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "saveButton").click();

		VoodooUtils.waitForReady();

		// Wait for expiration of First Msg Window
		sugar().emails.waitForSugarMsgWindow(60000);

		// Wait for expiration of Second Msg Window
		sugar().emails.waitForSugarMsgWindow(60000);

		// Wait for expiration of Third Msg Window
		sugar().emails.waitForSugarMsgWindow(60000);

		VoodooUtils.pause(3000); // Let action complete. Could not find suitable waitForxxx control.

		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.refresh(); // Needed to refresh email module after email account is configured.
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		sugar().logout(); // log out from second user
	}
	/**
	 * Notification Email_Verify that the name format in notification email
	 * for lead is consistent with the form
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_22790_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as first user
		sugar().login(myFirstUser);

		// Now create a Lead record as myFirstUser and assign it to mySecondUser
		FieldSet fs = sugar().leads.getDefaultData();
		fs.put("relAssignedTo", usersDS.get(1).get("lastName"));
		LeadRecord myLead = (LeadRecord)sugar().leads.api.create(fs);
		myLead.edit(fs);
		sugar().logout(); // log out from first user

		// Now login as mySecondUser and check if the email received for
		// Lead assignment contains name of mySecondUser in the local format
		sugar().login(mySecondUser);
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1070, VOOD-1077, VOOD-1083
		new VoodooControl("button", "id", "checkEmailButton").click();
		VoodooControl inboxControl = (new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]"));
		if (!inboxControl.queryExists()) {
			new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'" + emailSetupData.get("userName") + "')]").click();
		}
		inboxControl.click();
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'" + myLead.get("lastName") + " " + myLead.get("firstName") + " " + myLead.get("salutation") + "')]").click();
		VoodooUtils.pause(15000); // Let mail load completely, pause needed

		// Check Expected Result
		String textToCheck1 = String.format("%s %s %s %s", customFS.get("sugarcrm_lead"), myLead.get("lastName"), myLead.get("firstName"), myLead.get("salutation"));
		new VoodooControl("td", "css", "div#listBottom table tbody tr:nth-child(2) td.displayEmailValue").assertContains(textToCheck1, true);

		// TODO: VOOD-1035 - Not able to focus on displayEmailFramePreview
		// VoodooUtils.focusDefault();
		// VoodooUtils.focusFrame("displayEmailFramePreview");
		// VoodooUtils.pause(3000);
		// String textToCheck2 = ds.get(0).get("lastName")+' '+ds.get(0).get("firstName")+" has assigned a Lead to "+ds.get(1).get("lastName")+' '+ds.get(1).get("firstName");
		// String textToCheck3 = "Name: "+myLead.get("lastName")+' '+myLead.get("firstName")+" Mr";
		// new VoodooControl("body", "css", "body").waitForVisible();
		// new VoodooControl("body", "css", "body").assertContains(textToCheck2, true);
		// new VoodooControl("body", "css", "body").assertContains(textToCheck3, true);

		// Delete this mail
		new VoodooControl("button", "xpath", "//*[@id='_blank']/div/div[2]/button[4]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}