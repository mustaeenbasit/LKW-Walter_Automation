package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_26684 extends SugarTest {
	FieldSet emailSetupData;
	DataSource leadData;
	UserRecord qaUser;

	public void setup() throws Exception {
		emailSetupData = testData.get("env_email_setup").get(0);
		leadData = testData.get(testName);
		qaUser =  new UserRecord(sugar.users.getQAUser());
		sugar.login();

		// smtp settings		
		sugar.admin.setEmailServer(emailSetupData);

		// Admin Logout, QAUser Login
		sugar.logout();
		qaUser.login();

		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-563 -need lib support for user profile edit page
		// Verify text "Sugar Email Client" 
		new VoodooControl("td", "css", "#email_options_link_type td:nth-of-type(2)").assertEquals("Sugar Email Client", true);
		VoodooUtils.focusDefault();

		// Lead Records - Create and Save
		sugar.leads.navToListView();
		sugar.leads.listView.create();

		// TODO: VOOD-707 & VOOD-999
		VoodooControl emailInputCtrl = new VoodooControl("input", "css", ".newEmail.input-append");
		VoodooControl addEmailCtrl = new VoodooControl("a", "css", ".btn.addEmail");
		VoodooControl optoutCtrl = new VoodooControl("button", "css", ".btn.btn-edit[data-emailproperty='opt_out']");
		VoodooControl invalidCtrl = new VoodooControl("button", "css", ".btn.btn-edit[data-emailproperty='invalid_email']");
		VoodooControl opt2Ctrl = new VoodooControl("butto", "css", ".fld_email.edit div:nth-child(2) .btn-toolbar button:nth-child(2)");

		for(int i=0; i< leadData.size(); i++) {
			sugar.leads.createDrawer.getEditField("firstName").set(leadData.get(i).get("firstName"));
			sugar.leads.createDrawer.getEditField("lastName").set(leadData.get(i).get("lastName"));
			if(i==0) {
				// email 1 - primary + opt-out
				emailInputCtrl.set(leadData.get(i).get("email1"));
				addEmailCtrl.click();
				optoutCtrl.click();		
			} else if(i==1) {
				// email 1 - primary + invalid
				// email 2 - opt-out
				// email3 - valid
				emailInputCtrl.set(leadData.get(i).get("email1"));
				addEmailCtrl.click();
				invalidCtrl.click();
				emailInputCtrl.set(leadData.get(i).get("email2"));
				addEmailCtrl.click();
				opt2Ctrl.click();
				emailInputCtrl.set(leadData.get(i).get("email3"));
			} else if(i==3) {
				// email 1 - primary 
				// email 2 - valid
				emailInputCtrl.set(leadData.get(i).get("email1"));
				addEmailCtrl.click();
				emailInputCtrl.set(leadData.get(i).get("email2"));
			}
			sugar.leads.createDrawer.save();
			sugar.leads.listView.create();
		}
		sugar.leads.createDrawer.cancel();
	}

	/**
	 * Quick email composer respect valid email
	 * @throws Exception
	 */
	@Test
	public void Emails_26684_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		leadData = testData.get(testName);

		// TODO: VOOD-843
		VoodooControl toEmailAddressCtrl = new VoodooControl("span", "css", ".fld_to_addresses.edit div ul li:nth-of-type(1) div span");
		VoodooControl emptyAddressCtrl = new VoodooControl("input", "css", ".fld_to_addresses.edit div ul li:nth-of-type(1) input");

		// TODO: VOOD-999
		VoodooControl cancelCtrl = new VoodooControl("a", "css", "div[data-voodoo-name='compose'] .fld_cancel_button.detail a");

		sugar.leads.navToListView();

		// Lead4 record
		sugar.leads.listView.clickRecord(1);
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);

		// Verifying primary address is populated in TO address field
		toEmailAddressCtrl.assertAttribute("data-title", leadData.get(3).get("email1"), true);
		cancelCtrl.click();
		VoodooUtils.waitForReady();

		// Next Record i.e Lead3
		sugar.leads.recordView.gotoNextRecord();
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);

		// Verifying empty address is in TO address field
		emptyAddressCtrl.assertAttribute("value", "", true);
		cancelCtrl.click();
		VoodooUtils.waitForReady();

		// Next Record i.e Lead2
		sugar.leads.recordView.gotoNextRecord();
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);

		// Verifying email address3 is populated in TO address field
		toEmailAddressCtrl.assertAttribute("data-title", leadData.get(1).get("email3"), true);
		cancelCtrl.click();
		VoodooUtils.waitForReady();

		// Next Record i.e Lead1
		sugar.leads.recordView.gotoNextRecord();
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);

		// Verifying empty address is in TO address field
		emptyAddressCtrl.assertAttribute("value", "", true);
		cancelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}