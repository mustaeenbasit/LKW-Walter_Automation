package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_26813 extends SugarTest {
	DataSource emailSetup, emailCostumSetup;

	public void setup() throws Exception {
		emailSetup = testData.get(testName);
		emailCostumSetup = testData.get(testName+"_1");
		sugar.login();		

		// configure Admin->Email Settings
		sugar.admin.setEmailServer(emailSetup.get(0));
		
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		
		// TODO: VOOD-672
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click(); 
		new VoodooControl("input", "id", "addButton").click();		  
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailCostumSetup.get(0).get("mail_account_name"));
		new VoodooControl("input", "id", "email_user").set(emailSetup.get(0).get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(emailSetup.get(0).get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set(emailCostumSetup.get(0).get("trash_folder"));
		new VoodooControl("input", "id", "ie_from_name").set(emailCostumSetup.get(0).get("outgoing_from_name"));
		new VoodooControl("input", "id", "ie_from_addr").set(emailCostumSetup.get(0).get("outgoing_from_address"));
		VoodooUtils.waitForAlertExpiration();// here need this due to outgoing mail server dropdown take time to load
		
		new VoodooControl("input", "id", "saveButton").click();
	    // Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

	    // Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

	    // Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

	    VoodooUtils.pause(1000); // Let action complete. Could not find suitable waitForxxx control.
	    new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
	    VoodooUtils.acceptDialog();
	    VoodooUtils.refresh(); // Needed to refresh email module after email account is configured.
	    VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Date Ranges In Emails Module Date Search Should Be Inclusive
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_26813_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
	
		// email module  
		sugar.navbar.navToModule("Emails");
		VoodooUtils.focusFrame("bwc-frame");		
		for(int i = 0; i < 2; i++) { // Fixed for two times only			
			VoodooUtils.voodoo.log.info("loop i value: "+i + " ;");
			// TODO: VOOD-843 Need lib support to handle new email composer UI
			sugar.alerts.waitForLoadingExpiration();
			new VoodooControl("button", "css", "#content > div > table > tbody > tr > td > button:nth-child(2)").click();  // click on compose mail button
			VoodooUtils.waitForAlertExpiration(); // required to populate compose form UI data completely
			// required to populate compose form UI data completely
			sugar.emails.waitForSugarMsgWindow(30000);
			
			new VoodooControl("input", "id", "addressTO"+(i+1)).set( emailSetup.get(0).get("userName")); 
			new VoodooControl("input","id","emailSubject"+(i+1)).set(emailCostumSetup.get(i).get("subject"));
			VoodooUtils.focusFrame("htmleditor"+(i+1)+"_ifr"); // focus email body iframe
			new VoodooControl("body", "id", "tinymce").set(emailCostumSetup.get(i).get("bodyText"));
			sugar.alerts.waitForLoadingExpiration();
			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");
			// click send button in compose mail UI
			new VoodooControl("button", "css", "#composeHeaderTable"+(i+1)+"  tr:nth-child(1)  td:nth-child(1) button:nth-child(1)").click();			
		}
		
		VoodooUtils.waitForAlertExpiration();
		// click checkemail button to sync email inbox
		new VoodooControl("button", "id", "checkEmailButton").click();
		// Wait for expiration of sync new emails
		VoodooUtils.pause(50000); // pause required to sync new emails

		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").waitForVisible(); // no suitable css selector available
		//  click on email folder inside email tree 
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").click(); // no suitable css selector available
		VoodooUtils.waitForAlertExpiration();
		// click on inbox
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span").click(); // no suitable css selector available
		// right click on email inside email inbox to import one email record to Sugar
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[4]/div").rightClick(); // no suitable css selector available
		// click on "Import to Sugar" option in Context manu
		new VoodooControl("a", "css", "#emailContextMenu > div.bd > ul > li:nth-child(3) > a").click();
		VoodooUtils.pause(20000); // pause required to import mail to sugar folder
	    
		// click to "Import to Sugar" button 
		new VoodooControl("button", "css", "#importDialog > div.ft > span > span > span > button").click();
		VoodooUtils.waitForAlertExpiration();
		// close the message 
		new VoodooControl("button", "css", "#sugarMsgWindow > div.ft > span > span > span > button").click();
		VoodooUtils.waitForAlertExpiration();
		// right click on email inside email inbox to import one email record to Sugar
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[2]/td[4]/div").rightClick(); // no suitable css selector available
		// click on "Import to Sugar" option in Context manu
		new VoodooControl("a", "css", "#emailContextMenu > div.bd > ul > li:nth-child(3) > a").click();
		VoodooUtils.pause(20000); // pause required to import mail to sugar folder
	    
		// click to "Import to Sugar" button
	    VoodooControl importDialog = new VoodooControl("button", "css", "#importDialog > div.ft > span > span > span > button");
	    importDialog.waitForVisible(3000);
	    importDialog.click();
		VoodooUtils.waitForAlertExpiration();
		// close the message 
		new VoodooControl("button", "css", "#sugarMsgWindow > div.ft > span > span > span > button").click();

		// click on email tab
		new VoodooControl("li", "id", "tree").click();
		VoodooControl myEmailCtrl = new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[2]/table/tbody/tr/td[2]/span"); // xpath used because no suitable CSS selector available 
		myEmailCtrl.waitForVisible(1000);
		// click on My Email folder 
		myEmailCtrl.click();
		// assert imported email in My Email 
		new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[2]/td[5]/div").assertContains(emailCostumSetup.get(0).get("subject"), true);
		new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[5]/div").assertContains(emailCostumSetup.get(1).get("subject"), true);
		
		// TODO: VOOD-1051
		// click on search tab  
		new VoodooControl("li", "css", "#searchTab").click();
		// click on "More" option in search window
		new VoodooControl("a", "css", "#advancedSearchTable > tbody > tr.toggleClass.visible-search-option > td:nth-child(1) > a").click();
		VoodooControl dateFromCtrl= new VoodooControl("img", "id", "searchDateFrom_trigger");
		dateFromCtrl.click();
		VoodooControl todayDateCtl = new VoodooControl("a", "id", "callnav_today");
		todayDateCtl.click();
		VoodooControl closeFromDateCtrl = new VoodooControl("a", "css", "#container_searchDateFrom_trigger > a");
		closeFromDateCtrl.click();
		VoodooControl dateToCtrl = new VoodooControl("img", "id", "searchDateTo_trigger");
		dateToCtrl.click();
		todayDateCtl.click();
		VoodooControl closeToDateCtrl = new VoodooControl("a", "css", "#container_searchDateTo_trigger > a");
		closeToDateCtrl.click();
		VoodooControl advSearchCtrl= new VoodooControl("input", "id", "advancedSearchButton");
		advSearchCtrl.click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("div", "css", "#emailGrid > div.yui-dt-bd").assertContains(emailCostumSetup.get(0).get("subject"), true);
		new VoodooControl("div", "css", "#emailGrid > div.yui-dt-bd").assertContains(emailCostumSetup.get(1).get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}	
}
