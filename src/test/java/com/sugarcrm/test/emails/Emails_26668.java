package com.sugarcrm.test.emails;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_26668 extends SugarTest {
	FieldSet myContactData;
	FieldSet emailSetupData;

	public void setup() throws Exception {				
		sugar.login();

		// smtp settings
		emailSetupData = testData.get(testName + "_smtp_settings").get(0);
		sugar.admin.setEmailServer(emailSetupData);
		sugar.logout();

		// Login as QAUser
		UserRecord qauser =  new UserRecord(sugar.users.getQAUser());
		qauser.login();

		// Contact Record
		myContactData = testData.get(testName).get(0);
		sugar.contacts.navToListView();
		sugar.contacts.listView.create();
		sugar.contacts.createDrawer.getEditField("firstName").set(myContactData.get("firstName"));
		sugar.contacts.createDrawer.getEditField("lastName").set(myContactData.get("lastName"));
		sugar.contacts.createDrawer.getEditField("emailAddress").set(myContactData.get("email_address"));
		sugar.contacts.createDrawer.save();
		sugar.contacts.listView.clickRecord(1);
	}

	/**
	 * Send email from email link should list the email in Emails->My Sent Email folder
	 * @throws Exception
	 */
	@Test
	public void Emails_26668_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Click on email
		sugar.contacts.recordView.getDetailField("emailAddress").click();

		sugar.contacts.recordView.composeEmail.getControl("subject").set(myContactData.get("subject_msg"));
		VoodooUtils.focusFrame("mce_0_ifr");
		sugar.contacts.recordView.composeEmail.getControl("body").set(myContactData.get("body_msg"));
		VoodooUtils.focusDefault();
		sugar.contacts.recordView.composeEmail.getControl("sendButton").click();
		VoodooUtils.waitForReady();

		// Emails module
		sugar.emails.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO:VOOD-792
		// Verifying, email in my Sent Folder
		// Using xPath to find the specific folder 
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'My Email')]").click();
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'My Sent Email')]").click();
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[5]").assertEquals(myContactData.get("subject_msg"), true);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[7]").assertContains(myContactData.get("email_address"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}