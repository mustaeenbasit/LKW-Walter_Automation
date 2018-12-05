package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_20755 extends SugarTest {
	DataSource ds, userDataSource, emailSettingsDS, messageDs ;
	FieldSet emailSettings;
	UserRecord user1,user2;
	
	public void setup() throws Exception {
		sugar.login();
		
		ds = testData.get(testName + "_team");
		userDataSource = testData.get(testName + "_user");
		emailSettingsDS = testData.get(testName + "_email_settings" );
		messageDs = testData.get(testName + "_data");
		
		// Configure outgoing email in admin panel
 		emailSettings = new FieldSet();
 		emailSettings.put("userName", emailSettingsDS.get(0).get("emailAddress"));
 		emailSettings.put("password", emailSettingsDS.get(0).get("password"));
 		emailSettings.put("allowAllUsers", "true");
 		sugar.admin.setEmailServer(emailSettings);
 		
 		// create two demo users i.e. user1, user2
		user1 = (UserRecord) sugar.users.create(userDataSource.get(0));
		sugar.alerts.waitForLoadingExpiration();
		user2 = (UserRecord) sugar.users.create(userDataSource.get(1));
		sugar.alerts.waitForLoadingExpiration();
		
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// navigate to teams module 
		sugar.admin.adminTools.getControl("teamsManagement").click();
		
		// create a new team for user1
		// TODO: VOOD-518 lib support for all controls of team widget
		new VoodooControl("button", "css", "li[data-module='Teams'] button[data-toggle='dropdown']").click();
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_TEAM']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "name", "name").set(ds.get(0).get("name"));
		new VoodooControl("input", "css", "#btn_save").click();
		sugar.alerts.waitForLoadingExpiration();
		// click to select button to select a demo user 
		VoodooControl selectUserCtrl = new VoodooControl("a", "id", "team_memberships_select_button");
		selectUserCtrl.click();
		VoodooUtils.focusWindow(1);
		// search the user
		VoodooControl userNameCtrl = new VoodooControl("input", "id", "user_name_advanced");
		userNameCtrl.set(userDataSource.get(0).get("userName") );
		// click search button 
		VoodooControl searchCtrl = new VoodooControl("input", "id", "search_form_submit");
		searchCtrl.click();
		// select the user
		VoodooControl userClickCtrl = new VoodooControl("a", "css", "#MassUpdate > table.list.view > tbody > tr.oddListRowS1 > td:nth-child(3) > a");
		userClickCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		
		// create a new team for user2
		new VoodooControl("button", "css", "li[data-module='Teams'] button[data-toggle='dropdown']").click();
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_TEAM']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "name", "name").set(ds.get(1).get("name"));
		new VoodooControl("input", "css", "#btn_save").click();
		selectUserCtrl.click();
		VoodooUtils.focusWindow(1);
		// search the user
		userNameCtrl.set(userDataSource.get(1).get("userName") );
		// search the user
		searchCtrl.click();
		// select the user
		userClickCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
	}

	/**
	 * Mass update Assign info for email
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20755_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		sugar.logout();
		
		// login with user1
		sugar.login(user1);
		
		// TODO: VOOD-672 Need Lib support for Email settings
		// Set email settings individually
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailSettingsDS.get(0).get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSettingsDS.get(0).get("emailAddress")); // To allow to fetch email 
		new VoodooControl("input", "id", "email_password").set(emailSettingsDS.get(0).get("password")); // To allow to fetch email 
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "reply_to_addr").set(emailSettingsDS.get(0).get("emailAddress"));
		new VoodooControl("input", "id", "saveButton").click();
		
		// Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		// Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		// Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		VoodooUtils.pause(3000); // Let action complete. Could not find suitable waitForxxx control.
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();

		VoodooUtils.acceptDialog();

		// TODO: VOOD-843 Need lib support to handle new email composer UI
		VoodooControl composeButtonCtrl = new VoodooControl("button", "id", "composeButton");
		
		// send two mails 
		for (int i = 0; i < emailSettingsDS.size(); i++) {
			composeButtonCtrl.click();  // click on compose mail button
			VoodooUtils.waitForAlertExpiration(); // required to populate compose form UI data completely
			new VoodooControl("select", "id", "data_parent_type" +(i+1)).set("-none-");
			new VoodooControl("input", "id", "addressTO"+(i+1)).set( emailSettingsDS.get(i).get("emailAddress")); 
			new VoodooControl("input","id","emailSubject"+(i+1)).set(emailSettingsDS.get(i).get("subject"));
			VoodooUtils.focusFrame("htmleditor"+(i+1)+"_ifr"); // focus email body iframe
			new VoodooControl("body", "id", "tinymce").set(emailSettingsDS.get(i).get("bodyText"));
			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");
			
			// click send button in compose mail UI
			new VoodooControl("button", "css", "#composeHeaderTable"+(i+1)+" tr:nth-child(1)  td:nth-child(1) button:nth-child(1)").click();
			VoodooUtils.waitForAlertExpiration();
		}
		// click checkemail button to sync email inbox
		new VoodooControl("button", "id", "checkEmailButton").click();
		VoodooUtils.pause(30000); // pause required to sync new emails
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").waitForVisible();
		
		//  click on email folder inside email tree 
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").click();
		VoodooUtils.waitForAlertExpiration();
		
		// click on inbox folder
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span").click();
		
		// Select emails and clicking by right mouse button select 'Import to Sugar'  
		for (int i = 0; i < emailSettingsDS.size(); i++) {
			// right click on email inside email inbox to import one email record to Sugar
			new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr["+(i+1)+"]/td[4]/div").rightClick();
			// click on "Import to Sugar" option in Context manu
			new VoodooControl("a", "css", "#emailContextMenu > div.bd > ul > li:nth-child(3) > a").click();
			VoodooUtils.waitForAlertExpiration(); 
			// remove Global primary team  
			new VoodooControl("button", "id", "remove_team_name_collection_0").click();
			
			// click to select a team
			new VoodooControl("button", "css", "#lineLabel_team_name > td:nth-child(1) > span > button.button.firstChild").click();
			VoodooUtils.focusWindow(1);
			// search team1  
			new VoodooControl("input", "id", "team_name_input").set(ds.get(1).get("name"));
			
			// search the team
			new VoodooControl("input", "id", "search_form_submit").click();
			new VoodooControl("a", "css", "#MassUpdate > table.list.view > tbody > tr.oddListRowS1 > td:nth-child(2) > a").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusFrame("bwc-frame");
			// set team1 as a primary team 
			new VoodooControl("input", "id", "primary_team_name_collection_0").click();
			
			// select user2 into Assigned To field 
			new VoodooControl("input", "css", "[name ='btn_assigned_user_name']").click();
			VoodooUtils.focusWindow(1);
			new VoodooControl("input", "id", "user_name_advanced").set(userDataSource.get(1).get("userName"));
			new VoodooControl("input", "id", "search_form_submit").click();
			new VoodooControl("a", "css", "body > table.list.view > tbody > tr.oddListRowS1 > td:nth-child(1) > a").click();
			VoodooUtils.focusWindow(0);
			VoodooUtils.focusFrame("bwc-frame");
			
			// click to "Import to Sugar" button 
			new VoodooControl("button", "css", "#importDialog > div.ft > span > span > span > button").click();
			VoodooUtils.pause(20000);
			
			// Assert popup message appears with Status title: info like 'Message No 1, Status: Import Passed'
			new VoodooControl("div", "css", "#sugarMsgWindow > div.bd").assertContains(messageDs.get(0).get("message_data"), true);
			
			// close the message 
			new VoodooControl("button", "css", "#sugarMsgWindow > div.ft > span > span > span > button").click();
		}
		VoodooUtils.focusDefault();	
		// logout user1
		sugar.logout();
		
		// login as user2
		sugar.login(user2);
		
		// Go to Email module 
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		
		VoodooUtils.focusFrame("bwc-frame");
		//  click on My Emails folder inside email tree 
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div/table/tbody/tr/td[2]/span").click();
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div/table/tbody/tr/td[2]/span").click();
		VoodooUtils.waitForAlertExpiration();
		// Check imported emails in 'My Emails' box
		new VoodooControl("div", "id", "emailGrid").assertContains(messageDs.get(0).get("mail_sub_one"), true);
		new VoodooControl("div", "id", "emailGrid").assertContains(messageDs.get(0).get("mail_sub_two"), true);
		
		VoodooUtils.focusDefault();	
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 	