package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Emails_26678 extends SugarTest {
	DataSource emailSetup, sendEmail, leadsRecord;
	ContactRecord myContact;
	LeadRecord leadRecord1, leadRecord2;
	StandardSubpanel contactEmailSub, leadEmailSub1, leadEmailSub2;
	VoodooControl inputSubject, inputBody, sendBtn, subjectEmailSubpanel, historicalSummaryBtn,
						cancelBtn,sentStatus,emailTo,MyEmailCntrl,MySentEmailCntrl,emailSubject;
	
	public void setup() throws Exception {
		sendEmail = testData.get(testName+"_sendEmail");
		emailSetup = testData.get(testName+"_emailSetup");
		leadsRecord = testData.get(testName+"_leadsRecord");
		
		sugar.login();
		
		// configure admin->Email Settings
		sugar.admin.setEmailServer(emailSetup.get(0));
		
		// Create Contact and Lead records.
		myContact = (ContactRecord)sugar.contacts.api.create();
		leadRecord1 = (LeadRecord)sugar.leads.api.create(leadsRecord.get(0));
		leadRecord2 = (LeadRecord)sugar.leads.api.create(leadsRecord.get(1));
		
		// Set Primary email Optout
		leadRecord1.navToRecord();
		sugar.leads.recordView.edit();
		sugar.leads.createDrawer.getEditField("emailAddress").set(leadsRecord.get(0).get("emailAddress"));
		sugar.leads.createDrawer.getEditField("lastName").click();
		new VoodooControl("a", "css", ".btn.addEmail").click();
		new VoodooControl("button", "css", ".btn.btn-edit[data-emailproperty='opt_out']").click();
		sugar.leads.recordView.save();
		sugar.alerts.waitForLoadingExpiration();
		
		// Set Primary email Invalid
		leadRecord2.navToRecord();
		sugar.leads.recordView.edit();
		sugar.leads.createDrawer.getEditField("emailAddress").set(leadsRecord.get(1).get("emailAddress"));
		sugar.leads.createDrawer.getEditField("lastName").click();
		new VoodooControl("a", "css", ".btn.addEmail").click();
		new VoodooControl("button", "css", ".btn.btn-edit[data-emailproperty='invalid_email']").click();
		sugar.leads.recordView.save();
		sugar.alerts.waitForLoadingExpiration();
		sugar.logout();
		
		sugar.login();
	}

	/**
	 * Compose email with record that has no primary email or Primary email is either invalid or optout.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_26678_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Send and verify sent email in contacts module
		myContact.navToRecord();
		contactEmailSub = sugar.contacts.recordView.subpanels.get(sugar.emails.moduleNamePlural);
		contactEmailSub.composeEmail();
		
		// Verify "Required" appears in To field
		new VoodooControl("input", "css", "[name='to_addresses']").assertAttribute("placeholder", "Required");
		
		// VOOD-843 - Need library support for email composer UI
		new VoodooControl("button", "css", ".layout_Emails .btn-link.more").click(); // Clicking "show more" link.
		
		// Verify, in "Related to", field, the Contacts full name is appearing.
		new VoodooControl("div", "css", ".record-cell div.span7 div").assertContains(sugar.contacts.getDefaultData().get("fullName"), true);

		new VoodooControl("input", "css", ".fld_to_addresses.edit div ul li input").set(sendEmail.get(0).get("to"));
		emailTo = new VoodooControl("div", "css", ".select2-result-label");
		emailTo.waitForVisible();
		emailTo.click();
		inputSubject = new VoodooControl("input", "css", "[name='subject']");
		inputSubject.set(sendEmail.get(0).get("subject"));
		VoodooUtils.pause(3000); // pause needed to let body visible.
		VoodooUtils.focusFrame("mce_0_ifr");
		inputBody = new VoodooControl("body", "id", "tinymce");
		inputBody.set(sendEmail.get(0).get("body"));
		VoodooUtils.focusDefault();
		sendBtn = new VoodooControl("a", "css", "[name='send_button']");
		sendBtn.click();
		sugar.alerts.waitForLoadingExpiration();
		sugar.alerts.getSuccess().closeAlert();
		
		// Verify newly composed email is displayed correctly on the Record view in Emails subpanel
		// TODO: VOOD-609
		contactEmailSub.expandSubpanel();
		contactEmailSub.assertContains(sendEmail.get(0).get("subject"), true);
		contactEmailSub.assertContains("Sent", true);
		
		// Verify status of sent email in Historical Summary page.
		sugar.contacts.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-965
		historicalSummaryBtn = new VoodooControl("a", "css" , "[name='historical_summary_button']");
		historicalSummaryBtn.click();
		sugar.alerts.waitForLoadingExpiration();
		sentStatus = new VoodooControl("div", "css", "[data-original-title='Sent'].field_status_sent");
		sentStatus.assertEquals("Sent", true);
		cancelBtn = new VoodooControl("a", "css", "span.fld_cancel_button.history-summary-headerpane a");
		cancelBtn.click();
		
		// Verify Email record appears in the Emails -> My Sent Mail folder 
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1077
		MyEmailCntrl = new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'My Email')]");
		MySentEmailCntrl = new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'My Sent Email')]");
		emailSubject = new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]");
		MyEmailCntrl.click();
		MySentEmailCntrl.click();
		emailSubject.assertContains(sendEmail.get(0).get("subject"), true);
		VoodooUtils.focusDefault();
		
		// Send and verify sent email in Leads (Optout email) module
		leadRecord1.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		leadEmailSub1 = sugar.leads.recordView.subpanels.get(sugar.emails.moduleNamePlural);
		leadEmailSub1.getControl("composeEmail").click();
		sugar.alerts.waitForLoadingExpiration();
				
		// VOOD-843 - Need library support for email composer UI
		new VoodooControl("input", "css", ".fld_to_addresses.edit div ul li input").set(sendEmail.get(0).get("to"));
		emailTo.waitForVisible();
		emailTo.click();
		inputSubject.set(sendEmail.get(1).get("subject"));
		VoodooUtils.pause(3000); // pause needed to let body visible.
		VoodooUtils.focusFrame("mce_7_ifr");
		inputBody.set(sendEmail.get(1).get("body"));
		VoodooUtils.focusDefault();

		// click send email button
		sendBtn.click();
		sugar.alerts.waitForLoadingExpiration();
		sugar.alerts.getSuccess().closeAlert();
		
		// Verify newly composed email is displayed correctly on the Record view in Emails subpanel
		// TODO: VOOD-609
		leadEmailSub1.expandSubpanel();
		leadEmailSub1.assertContains(sendEmail.get(1).get("subject"), true);
		leadEmailSub1.assertContains("Sent", true);
		
		// Verify status of sent email in Historical Summary page.
		sugar.leads.recordView.openPrimaryButtonDropdown();
		historicalSummaryBtn.click();
		sugar.alerts.waitForLoadingExpiration();
		sentStatus.assertEquals("Sent", true);
		cancelBtn.click();
		
		// Verify Email record appears in the Emails -> My Sent Mail folder 
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		MyEmailCntrl.click();
		MySentEmailCntrl.click();
		emailSubject.assertContains(sendEmail.get(1).get("subject"), true);
		VoodooUtils.focusDefault();
				
		// Send and verify sent email in Leads (Invalid email) module
		leadRecord2.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		leadEmailSub2 = sugar.leads.recordView.subpanels.get(sugar.emails.moduleNamePlural);
		leadEmailSub2.getControl("composeEmail").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// VOOD-843 - Need library support for email composer UI
		new VoodooControl("input", "css", ".fld_to_addresses.edit div ul li input").set(sendEmail.get(0).get("to"));
		emailTo.waitForVisible();
		emailTo.click();
		inputSubject.set(sendEmail.get(2).get("subject"));
		VoodooUtils.pause(3000); // pause needed to let body visible.
		VoodooUtils.focusFrame("mce_8_ifr");
		inputBody.set(sendEmail.get(2).get("body"));
		VoodooUtils.focusDefault();

		// click send email button
		sendBtn.click();
		sugar.alerts.waitForLoadingExpiration();
		sugar.alerts.getSuccess().closeAlert();
		
		// Verify newly composed email is displayed correctly on the Record view in Emails subpanel
		leadEmailSub2.expandSubpanel();
		leadEmailSub2.assertContains(sendEmail.get(2).get("subject"), true);
		leadEmailSub2.assertContains("Sent", true);
		
		// Verify status of sent email in Historical Summary page.
		sugar.leads.recordView.openPrimaryButtonDropdown();
		historicalSummaryBtn.click();
		sugar.alerts.waitForLoadingExpiration();
		sentStatus.assertEquals("Sent", true);
		cancelBtn.click();
		
		// Verify Email record appears in the Emails -> My Sent Mail folder 
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		MyEmailCntrl.click();
		MySentEmailCntrl.click();
		emailSubject.assertContains(sendEmail.get(2).get("subject"), true);
		VoodooUtils.focusDefault();
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}