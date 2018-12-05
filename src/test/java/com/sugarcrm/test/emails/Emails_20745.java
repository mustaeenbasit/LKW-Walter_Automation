package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_20745  extends SugarTest {
	DataSource ds, userDataSource ;
	FieldSet emailSettings;
	UserRecord demoUser;
	
	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		userDataSource = testData.get(testName+ "_1");
		// Configure outgoing email in admin panal
 		emailSettings = new FieldSet();
 		emailSettings.put("userName", userDataSource.get(0).get("emailAddress"));
 		emailSettings.put("password", userDataSource.get(0).get("password"));
 		emailSettings.put("allowAllUsers", "true");
 		sugar.admin.setEmailServer(emailSettings);
 		demoUser = (UserRecord) sugar.users.create(userDataSource.get(0));
		sugar.alerts.waitForLoadingExpiration();
		sugar.logout();
		sugar.login(demoUser);
	}

	/**
	 *  Add mail account in Emails module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20745_execute() throws Exception {
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
	  // assert the title of mail account 
	  new VoodooControl("td", "css", "tr:nth-child(1) tr:nth-child(1) tr:nth-child(2) > td").assertContains("Set up Mail Accounts to view incoming Emails from your Email accounts", true);
	  
	  //Click Add button in the Mail Account area
	  new VoodooControl("input", "id", "addButton").click();
	  // assert incoming email section in mail accounts properties window pop up
	  new VoodooControl("h4", "css", "#ieAccount > table:nth-child(7) > tbody > tr:nth-child(1) > td:nth-child(1) > h4").assertEquals("Incoming Email", true);
	  // assert outgoing email section in mail accounts properties window pop up	  
	  new VoodooControl("h4", "css", "#ieAccount > table:nth-child(8) > tbody > tr:nth-child(1) > td > h4").assertEquals("Outgoing Email", true);
	  // click on prefill gmail default in  in mail accounts properties window pop up
	  new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
	  // fill the the Incoming Email properties and outgoing Email properties
	  new VoodooControl("input", "id", "ie_name").set(ds.get(0).get("userName"));
	  new VoodooControl("input", "id", "email_user").set(ds.get(0).get("emailAddress")); // To allow to fetch email
	  new VoodooControl("input", "id", "email_password").set(ds.get(0).get("password")); // To allow to fetch email
	  new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
	  new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
	  new VoodooControl("input", "id", "ie_from_addr").set(ds.get(0).get("emailAddress"));
	  // Click Test Settings button in Incoming Email properties section
	  new VoodooControl("input", "id", "testButton").click();
	  new VoodooControl("div", "id", "testSettingsMsg").waitForVisible(60000);
	  // assert the email connection success message.
	  new VoodooControl("div", "id", "testSettingsMsg").assertContains("Connection completed successfully", true);
	  //  Click Save Button
	  new VoodooControl("input", "id", "saveButton").click();
	  VoodooUtils.pause(40000); // Let save and check complete at Gmail. No suitable waitForxxx control available.
	  // assert mail accounts list table and is_active checkbox is checked ?
	  new VoodooControl("input", "css",".yui-dt-checkbox").assertAttribute("class", "yui-dt-checkbox", true);				
	  new VoodooControl("input", "css", "#settingsDialog > a").click();
	  VoodooUtils.focusDefault();	 

	  VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}