package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_20635 extends SugarTest {
	DataSource ds, userDataSource ;
	FieldSet emailSettings;
	UserRecord demoUser;
	
	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName + "_email_settings");
		// Configure outgoing email in admin panal
 		emailSettings = new FieldSet();
 		emailSettings.put("userName", ds.get(0).get("emailAddress"));
 		emailSettings.put("password", ds.get(0).get("password"));
 		emailSettings.put("allowAllUsers", "true");
 		sugar.admin.setEmailServer(emailSettings);
 		
 		// TODO: VOOD-672, Set email settings individually
 		// Go to Emails module
 		sugar.navbar.navToModule("Emails");
 		sugar.alerts.waitForLoadingExpiration();
 		VoodooUtils.focusFrame("bwc-frame");
 		// Click Settings button
 		new VoodooControl("button", "id", "settingsButton").click();
 		// Go to the Mail Accounts tab in the Settings window
 		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
	  
 		//Click Add button in the Mail Account area
 		new VoodooControl("input", "id", "addButton").click();
 		// click on prefill gmail default in  in mail accounts properties window pop up
 		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
 		// fill the the Incoming Email properties and outgoing Email properties
 		new VoodooControl("input", "id", "ie_name").set(ds.get(0).get("userName"));
 		new VoodooControl("input", "id", "email_user").set(ds.get(0).get("emailAddress")); // To allow to fetch email
 		new VoodooControl("input", "id", "email_password").set(ds.get(0).get("password")); // To allow to fetch email
 		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
 		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
 		new VoodooControl("input", "id", "ie_from_addr").set(ds.get(0).get("emailAddress"));
 		VoodooUtils.pause(4000); // Wait for Outgoing SMTP Mail Server
 		//  Click Save Button
 		new VoodooControl("input", "id", "saveButton").click();
 		VoodooUtils.pause(40000); // Let save and check complete at Gmail. No suitable waitForxxx control available.
 		new VoodooControl("input", "css", "#settingsDialog > a").click();
 		
 		VoodooUtils.focusDefault();	
	}

	/**
	 *  Emails module - Edit email account settings
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20635_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-672, Set email settings individually
 		// Go to Emails module
 		sugar.navbar.navToModule("Emails");
 		sugar.alerts.waitForLoadingExpiration();
 		VoodooUtils.focusFrame("bwc-frame");
 		// Click Settings button
 		new VoodooControl("button", "id", "settingsButton").click();
 		
 		// Go to the Mail Accounts tab in the Settings window
 		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
 		
 		// Click to edit button in mail account list
 		new VoodooControl("img", "css", "#inboundAccountsTable > table > tbody.yui-dt-data > tr > td > div > img").click();

 		// filled account details to edit
 		new VoodooControl("input", "id", "ie_name").set(ds.get(1).get("userName"));
 		new VoodooControl("input", "id", "email_user").set(ds.get(1).get("emailAddress")); // To allow to fetch email
 		new VoodooControl("a", "id", "email_password_link").click();
 		new VoodooControl("input", "id", "email_password").set(ds.get(1).get("password")); // To allow to fetch email
 		new VoodooControl("input", "id", "trashFolder").set(ds.get(1).get("trash_folder"));
 		new VoodooControl("input", "id", "ie_from_name").set(ds.get(1).get("from_name"));
 		new VoodooControl("input", "id", "ie_from_addr").set(ds.get(1).get("from_address"));
 		VoodooUtils.pause(4000); // Wait for Outgoing SMTP Mail Server
 		
 		// Click Test Settings button in Incoming Email properties section
 		new VoodooControl("input", "id", "testButton").click();
 		new VoodooControl("div", "id", "testSettingsMsg").waitForVisible(60000);
 		// assert the email connection success message.
 		new VoodooControl("div", "id", "testSettingsMsg").assertContains("Connection completed successfully", true);
 		
 		//  Click Save Button
 		new VoodooControl("input", "id", "saveButton").click();
 		VoodooUtils.pause(40000); // Let save and check complete at Gmail. No suitable waitForxxx control available.
 		
 		// assert updated " Mail Account Name " in accounts list table
 		new VoodooControl("tbody", "css", "#inboundAccountsTable table .yui-dt-data").assertContains("SugarQA3", true);
 		new VoodooControl("input", "css", "#settingsDialog > a").click();
 		VoodooUtils.focusDefault();	
 		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}