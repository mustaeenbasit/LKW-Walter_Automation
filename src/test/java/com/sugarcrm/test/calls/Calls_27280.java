package com.sugarcrm.test.calls;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Calls_27280 extends SugarTest {
	FieldSet customData;
	UserRecord chrisUser;
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// chris user, contact, lead, calls record
		chrisUser = (UserRecord) sugar.users.api.create();
		myContact = (ContactRecord)sugar.contacts.api.create();
		myLead = (LeadRecord)sugar.leads.api.create();
		sugar.login();

		// Set up outbound email account
		sugar.admin.setEmailServer(testData.get("env_email_setup").get(0));

		// Add email address for contact record 
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		sugar.contacts.recordView.edit();
		sugar.contacts.recordView.getEditField("emailAddress").set(customData.get("contact_email_address"));
		sugar.contacts.recordView.save();

		// Add email address for leads record
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.edit();
		sugar.leads.recordView.getEditField("emailAddress").set(customData.get("lead_email_address"));
		sugar.leads.recordView.save();
		sugar.logout();
	}

	/**
	 * Verify that Invite emails are sent to the guests when the user clicks the Yes button
	 * @throws Exception 
	 */
	@Ignore("VOOD-1350 & VOOD-1273")
	@Test
	public void Calls_27280_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar.login(sugar.users.getQAUser());
		sugar.calls.navToListView();
		sugar.calls.listView.create();
		sugar.calls.createDrawer.getEditField("name").set(testName);

		// TODO: VOOD-1350
		VoodooControl addInvitee = new VoodooControl("button", "css", ".fld_invitees.edit [data-action='addRow']");
		VoodooControl searchInvitee = new VoodooSelect("a", "css", ".participants-form a").selectWidget.getControl("searchBox");

		// Add contact as invitee
		addInvitee.click();
		searchInvitee.set(myContact.getRecordIdentifier());
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + myContact.getRecordIdentifier() + "')]]").click();

		// Add Lead as invitee
		addInvitee.click();
		searchInvitee.set(myLead.getRecordIdentifier());
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + myLead.getRecordIdentifier() + "')]]").click();

		// Add User as invitee
		addInvitee.click();
		searchInvitee.set(chrisUser.getRecordIdentifier());
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + chrisUser.getRecordIdentifier() + "')]]").click();

		//sugar.calls.createDrawer.saveAndSendInvite();
		new VoodooControl("a", "css", ".btn.dropdown-toggle.btn-primary").click();
		new VoodooControl("span", "css", ".fld_save_invite_button.detail").click();
		sugar.alerts.waitForLoadingExpiration();
		if(sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.getSuccess().closeAlert();

		// Log out from QAuser and log in as Admin.
		sugar.logout();

		// TODO: VOOD-1273
		// redirect to Gmail account (Lead Record)
		VoodooUtils.go(customData.get("gmail_url"));
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("input", "id", "Email").set(customData.get("lead_email_address"));
		new VoodooControl("input", "id", "Passwd").set(customData.get("lead_password"));
		new VoodooControl("input", "id", "signIn").click();
		VoodooUtils.pause(35000); // pause required when login to Gmail account. No library method available

		// Verify receive a call invite subject message and click on that message to open detail mail body content
		VoodooControl mailSubjectCtrl = new VoodooControl("span", "css", "div.Cp > div > table > tbody > tr:nth-child(1) > td.xY.a4W div.xT span:nth-of-type(1)");
		mailSubjectCtrl.assertContains(String.format("%s - %s", customData.get("lead_mail_subject"),testName), true);
		mailSubjectCtrl.click();
		VoodooUtils.pause(5000);

		// TODO: Gmail body content access
		// Call record opens with appropriate link in mail body

		// sign out from gmail account
		new VoodooControl("a", "css", "[title='Account "+customData.get("lead_email_address")+"']").click();
		new VoodooControl("a", "xpath", "//*[@id='gb']/div[1]/div[1]/div/div[3]/div[2]/div[3]/div[2]/a").click();

		// sugar base url 
		String url = VoodooUtils.getGrimoireConfig().getValue("env.base_url");
		VoodooUtils.go(url);
		VoodooUtils.focusDefault();

		// login as admin (needed this) 
		sugar.login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}