package com.sugarcrm.test.meetings;

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
public class Meetings_27291 extends SugarTest {
	FieldSet customData;
	UserRecord chrisUser;
	ContactRecord myContact;
	LeadRecord myLead;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// chris user, contact, lead, meetings record
		chrisUser = (UserRecord) sugar().users.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().meetings.api.create();
		sugar().login();

		// Set up outbound email account
		sugar().admin.setEmailServer(testData.get("env_email_setup").get(0));

		// Add email address for contact record 
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("emailAddress").set(customData.get("contact_email_address"));
		sugar().contacts.recordView.save();

		// Add email address for leads record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		sugar().leads.recordView.getEditField("emailAddress").set(customData.get("lead_email_address"));
		sugar().leads.recordView.save();
		sugar().logout();
	}

	/**
	 * Verify that the user is asked if he/she wants to send invitee emails when the user clicks the Save button while editing an existing meeting
	 * @throws Exception 
	 */
	@Ignore("VOOD-1238 & VOOD-1273")
	@Test
	public void Meetings_27291_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("name").set(testName);

		// Add contact as invitee
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myContact);

		// Add Lead as invitee
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(myLead);

		// Add User as invitee
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(chrisUser);

		// TODO: VOOD-1238
		// sugar().meetings.recordView.saveAndSendInvite();
		new VoodooControl("a", "css", ".btn.dropdown-toggle.btn-primary").click();
		new VoodooControl("span", "css", ".fld_save_invite_button.detail").click();
		sugar().alerts.waitForLoadingExpiration(); // tried waitForLoadingExpiration upto 90000 page crash
		VoodooUtils.pause(45000); // required for above line not working

		// Verify new meeting saved with new changes
		sugar().meetings.recordView.getDetailField("name").assertEquals(testName, true);

		// TODO: VOOD-1350 (Need more appropriate controls for invitee list)
		VoodooControl inviteeCtrl = sugar().meetings.recordView.getControl("invitees");
		inviteeCtrl.assertContains(myContact.getRecordIdentifier(), true);
		inviteeCtrl.assertContains(myLead.getRecordIdentifier(), true);
		inviteeCtrl.assertContains(chrisUser.getRecordIdentifier(), true);

		// Log out from QAuser and log in as Admin.
		sugar().logout();

		// TODO: VOOD-1273
		// redirect to Gmail account (Lead Record)
		VoodooUtils.go(customData.get("gmail_url"));
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "id", "Email").set(customData.get("lead_email_address"));
		new VoodooControl("input", "id", "Passwd").set(customData.get("lead_password"));
		new VoodooControl("input", "id", "signIn").click();
		VoodooUtils.pause(35000); // pause required when login to Gmail account. No library method available

		// Verify receive a meeting invite subject message and click on that message to open detail mail body content
		VoodooControl mailSubjectCtrl = new VoodooControl("span", "css", "div.Cp > div > table > tbody > tr:nth-child(1) > td.xY.a4W div.xT span:nth-of-type(1)");
		mailSubjectCtrl.assertContains(String.format("%s - %s", customData.get("lead_mail_subject"),testName), true);
		mailSubjectCtrl.click();
		VoodooUtils.pause(5000);

		// TODO: Gmail body content access
		// meeting record opens with appropriate link in mail body

		// sign out from gmail account
		new VoodooControl("a", "css", "[title='Account "+customData.get("lead_email_address")+"']").click();
		new VoodooControl("a", "xpath", "//*[@id='gb']/div[1]/div[1]/div/div[3]/div[2]/div[3]/div[2]/a").click();

		// sugar base url 
		String url = VoodooUtils.getGrimoireConfig().getValue("env.base_url");
		VoodooUtils.go(url);
		VoodooUtils.focusDefault();

		// login as admin (needed this) 
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}