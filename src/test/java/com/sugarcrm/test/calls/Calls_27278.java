package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27278 extends SugarTest {
	FieldSet emailSetupData;
	FieldSet customData;
	UserRecord chrisUser;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		emailSetupData = testData.get("env_email_setup").get(0);

		// chris user, contact, lead record
		chrisUser = (UserRecord) sugar.users.api.create();
		sugar.contacts.api.create();
		sugar.leads.api.create();
		sugar.login();

		// Set up outbound email account
		sugar.admin.setEmailServer(emailSetupData);

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
	 * Verify that the user is not asked if he/she wants to send invitee emails when the user clicks the Save button while create a new call.
	 * @throws Exception
	 * 
	 */
	@Test
	public void Calls_27278_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar.login(sugar.users.getQAUser());
		
		// Call create
		sugar.calls.navToListView();
		sugar.calls.listView.create();
		sugar.calls.createDrawer.getEditField("name").set(sugar.calls.getDefaultData().get("name"));

		// TODO: VOOD-1221
		VoodooControl addInviteeCtrl = new VoodooControl("i", "css", ".fld_invitees.edit i.fa.fa-plus");
		VoodooControl searchRecordCtrl = new VoodooSelect("input", "css", "#select2-drop div input");

		// Add Contact as invitee
		addInviteeCtrl.click();
		searchRecordCtrl.set(sugar.contacts.getDefaultData().get("lastName"));

		// Add Lead as invitee
		addInviteeCtrl.click();
		searchRecordCtrl.set(sugar.leads.getDefaultData().get("lastName"));

		// Add Chris user as invitee
		addInviteeCtrl.click();
		searchRecordCtrl.set(chrisUser.get("lastName"));

		// Save call record
		sugar.calls.createDrawer.save();

		// Verify that there is no such pop up appears asking user "Do you want to Save & Send Invites?" 
		sugar.alerts.getAlert().assertContains(customData.get("no_alert_msg_for_invitee"), false);
		sugar.alerts.getWarning().assertVisible(false);

		// Verify that  a new Call is saved 
		sugar.calls.listView.verifyField(1, "name", sugar.calls.getDefaultData().get("name"));

		// TODO: Btw, clean up does automatically
		// Log out from QAuser and log in as Admin. if we don't add these lines, basecleanup error throw 
		sugar.logout();
		sugar.login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}