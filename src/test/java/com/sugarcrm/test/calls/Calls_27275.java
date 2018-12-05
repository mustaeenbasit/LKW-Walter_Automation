package com.sugarcrm.test.calls;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Calls_27275 extends SugarTest {
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
		sugar.calls.api.create();
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
	 * Verify that the user is not asked if he/she wants to send invitee emails when the user clicks the "Save & Send Invites" button while editing an existing call.
	 * @throws Exception 
	 */
	@Ignore("VOOD-1238 & VOOD-1273")
	@Test
	public void Calls_27275_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar.login(sugar.users.getQAUser());
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("name").set(testName);

		// Add contact as invitee
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(myContact);

		// Add Lead as invitee
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(myLead);

		// Add User as invitee
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(chrisUser);

		// TODO: VOOD-1238
		// sugar.calls.recordView.saveAndSendInvite();
		new VoodooControl("a", "css", ".btn.dropdown-toggle.btn-primary").click();
		new VoodooControl("span", "css", ".fld_save_invite_button.detail").click();
		sugar.alerts.waitForLoadingExpiration(); // tried waitForLoadingExpiration upto 90000 page crash
		VoodooUtils.pause(45000); // required for above line not working

		// Verify that there is no such pop up appears asking user "Do you want to Save & Send Invites?" when clicked on "Save & Send Invites" button 
		sugar.alerts.getAlert().assertContains(customData.get("no_alert_msg_for_invitee"), false);
		sugar.alerts.getWarning().assertVisible(false);

		// Log out from QAuser and log in as Admin.
		sugar.logout();

		// TODO: VOOD-1273
		// redirect to Gmail account
		VoodooUtils.go(customData.get("gmail_url"));
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("input", "id", "Email").set(customData.get("lead_email_address"));
		new VoodooControl("input", "id", "Passwd").set(customData.get("lead_password"));
		new VoodooControl("input", "id", "signIn").click();
		VoodooUtils.pause(35000); // pause required when login to Gmail account. No library method available

		// Assert the mail subject 
		VoodooControl mailSubjectCtrl = new VoodooControl("span", "css", "div.Cp > div > table > tbody > tr:nth-child(1) > td.xY.a4W div.xT span:nth-of-type(1)");
		System.out.println("Mail Subject::"+mailSubjectCtrl.getText()+"CSV:"+String.format("%s - %s", customData.get("lead_mail_subject"),testName));
		mailSubjectCtrl.assertContains(String.format("%s - %s", customData.get("lead_mail_subject"),testName), true);
		mailSubjectCtrl.click();
		sugar.alerts.waitForLoadingExpiration();

		// TODO: Gmail body content access

		// sign out from gmail account
		new VoodooControl("a", "css", "[title='Account "+customData.get("lead_email_address")+"']").click();
		new VoodooControl("a", "xpath", "//*[@id='gb']/div[1]/div[1]/div/div[3]/div[2]/div[3]/div[2]/a").click();
		// sugar base url 
		String url = VoodooUtils.getGrimoireConfig().getValue("env.base_url");
		VoodooUtils.go(url);
		VoodooUtils.focusDefault();

		// login as admin
		sugar.login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}