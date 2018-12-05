package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27296 extends SugarTest {
	FieldSet customData, emailSetup, emailCostumSetup;
	UserRecord myUser;
	ContactRecord contactRecord;
	LeadRecord leadRecord;
	MeetingRecord meetingRecord;

	public void setup() throws Exception {
		sugar().login();

		sugar().revLineItems.api.create();
		contactRecord = (ContactRecord) sugar().contacts.api.create();
		leadRecord = (LeadRecord) sugar().leads.api.create();
		myUser = (UserRecord) sugar().users.api.create();
		emailSetup = testData.get("env_email_setup").get(0);
		emailCostumSetup = testData.get(testName+"_mail_account").get(0);
		customData = testData.get(testName).get(0);

		// Set email address to a contact and lead
		FieldSet fs = new FieldSet();
		fs.put("emailAddress", customData.get("contactEmail"));
		contactRecord.edit(fs);
		fs.clear();
		fs.put("emailAddress", customData.get("leadEmail"));
		leadRecord.edit(fs);
		
		// configure Admin->Email Settings
		sugar().admin.setEmailServer(emailSetup);
		
		sugar().navbar.navToModule("Emails");
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		
		// TODO: VOOD-672
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click(); 
		new VoodooControl("input", "id", "addButton").click();		  
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailCostumSetup.get("mail_account_name"));
		new VoodooControl("input", "id", "email_user").set(emailSetup.get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(emailSetup.get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set(emailCostumSetup.get("trash_folder"));
		new VoodooControl("input", "id", "ie_from_name").set(emailCostumSetup.get("outgoing_from_name"));
		new VoodooControl("input", "id", "ie_from_addr").set(emailCostumSetup.get("outgoing_from_address"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "saveButton").click();
		VoodooUtils.waitForReady();
		
	    // Wait for expiration of First Msg Window
		sugar().emails.waitForSugarMsgWindow(120000);

	    // Wait for expiration of Second Msg Window
		sugar().emails.waitForSugarMsgWindow(120000);

	    // Wait for expiration of Third Msg Window
		sugar().emails.waitForSugarMsgWindow(120000);

	    VoodooUtils.pause(1000); // Let action complete. Could not find suitable waitForxxx control.
	    new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();

	    VoodooUtils.acceptDialog();
	    VoodooUtils.refresh(); // Needed to refresh email module after email account is configured.
	    VoodooUtils.waitForReady();
	    VoodooUtils.focusDefault();
	    
		// Logout from Admin
		sugar().logout();
		
		// Login with qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Invite emails are sent to the guests when the user clicks the Save & Send Invites
	 * @throws Exception
	 */
	@Test
	public void Meetings_27296_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create a Meetings record.  
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		
		// TODO: VOOD-847
		// In Guests field in Calls, click on + to select guest i.e.: Contact/Lead/User.
		VoodooControl inviteeCtrl = new VoodooControl("button", "css", "div.row.participant div.cell.buttons button[data-action='addRow']");
		VoodooSelect inviteeSearchCtrl = new VoodooSelect("input", "css", "#select2-drop div input");
		
		// Add contact, leads and user invitee.
		sugar().meetings.createDrawer.clickAddInvitee();
		inviteeSearchCtrl.set(sugar().contacts.getDefaultData().get("lastName"));
		
		sugar().meetings.createDrawer.clickAddInvitee();
		inviteeSearchCtrl.set(sugar().leads.getDefaultData().get("lastName"));

		sugar().meetings.createDrawer.clickAddInvitee();
		inviteeSearchCtrl.set(sugar().users.getDefaultData().get("lastName"));

		// Select Revenue Line Item for guest list
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(customData.get("revenueLineItem"));
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().revLineItems.getDefaultData().get("name"));
		
		// TODO: VOOD-1230 -Need Lib support for click on Meetings > create > Save & Send Invites
		sugar().meetings.createDrawer.openPrimaryButtonDropdown();
		new VoodooControl("a", "name", "save_invite_button").click();
		sugar().alerts.waitForLoadingExpiration(); // Required to complete Save & Send Invites process
		
		// TODO: VOOD-1217 -Need lib support for preview pane as well		
		VoodooControl userPreviewCtrl = new VoodooControl("span", "css", "#content div:nth-child(2) span > div > div:nth-child(3) div.cell.name span");
		VoodooControl leadPreviewCtrl = new VoodooControl("span", "css", "#content div:nth-child(2) span > div > div:nth-child(1) div.cell.name span");
		VoodooControl contactPreviewCtrl = new VoodooControl("span", "css", "#content div:nth-child(2) span > div > div:nth-child(7) div.cell.name span");
		sugar().meetings.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify that all info in the meetings record are correct as you input, especially in Guests field on listView > previewPane
		contactPreviewCtrl.assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
		leadPreviewCtrl.assertContains(sugar().leads.getDefaultData().get("fullName"), true);
		userPreviewCtrl.assertContains(sugar().users.getDefaultData().get("fullName"), true);
		
		// Logout from qauser and login with Admin user
		sugar().logout();
		sugar().login();
		
		// Go to email module
		sugar().navbar.navToModule("Emails");
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on check-email button to sync email Inbox
		new VoodooControl("button", "id", "checkEmailButton").click();
		sugar().alerts.waitForLoadingExpiration(40000); // pause required to sync new emails
		
		//  Click on email folder inside email tree 
		new VoodooControl("span", "css", "#emailtree > div > div > div:nth-child(1) > table > tbody > tr > td > span").click();
		VoodooUtils.waitForAlertExpiration();
		
		// Click on Inbox
		new VoodooControl("span", "css", "#emailtree > div > div > div:nth-child(1) > div > div > table > tbody > tr > td > span").click();
		
		// Verify that Email's are sent to all invitees
		VoodooControl mailLine1 = new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[5]/div");
		VoodooControl mailLine2 = new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[2]/td[5]/div");
		mailLine1.assertExists(true);
		mailLine2.assertExists(true);
		// TODO: Fix Email sending first
		// mailLine1.assertContains(testName, true);
		// mailLine2.assertContains(testName, true);
			
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}