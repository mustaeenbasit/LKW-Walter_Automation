package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_20853 extends SugarTest {
	DataSource ds,emailSetupData;
	FieldSet emailSettings;
	
	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		emailSetupData = testData.get(testName + "_smtp_settings");
		
		// setup smtp settings
		emailSettings = new FieldSet();
		emailSettings.put("userName", emailSetupData.get(0).get("userName"));
		emailSettings.put("password", emailSetupData.get(0).get("password"));
		emailSettings.put("allowAllUsers", emailSetupData.get(0).get("allowAllUsers"));
		sugar.admin.setEmailServer(emailSettings);
		
		// TODO: VOOD-672, Set email settings individually
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set("SugaMail");
		new VoodooControl("input", "id", "email_user").set(emailSetupData.get(0).get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set( emailSetupData.get(0).get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "ie_from_addr").set( emailSetupData.get(0).get("userName"));
		VoodooUtils.pause(3000);
		new VoodooControl("input", "id", "saveButton").click();
		// TODO: VOOD-1052
		VoodooUtils.pause(50000); // taking too much time to complete.No suitable waitForxxx control available.
		new VoodooControl("input", "css", "#settingsDialog > a").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Date From and Date Until work properly in smaller date range in imported Emails search.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20853_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// nav to email module  
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-843 Need lib support to handle new email composer UI
		new VoodooControl("button", "id", "composeButton").click();  // click on compose mail button
		VoodooUtils.waitForAlertExpiration(); // required to populate compose form UI data completely
		new VoodooControl("input", "id", "addressTO1").set( ds.get(0).get("userName")); 
		new VoodooControl("input","id","emailSubject1").set(ds.get(0).get("subject"));
		VoodooUtils.focusFrame("htmleditor1_ifr"); // focus email body iframe
		new VoodooControl("body", "id", "tinymce").set(ds.get(0).get("bodyText"));
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		// click send button in compose mail UI
		new VoodooControl("button", "css", "#composeHeaderTable1  tr:nth-child(1)  td:nth-child(1) button:nth-child(1)").click();
		VoodooUtils.waitForAlertExpiration(); // wait for email send action to complete   

		// click checkemail button to sync email inbox
		new VoodooControl("button", "id", "checkEmailButton").click();
		// TODO: VOOD-1052
		VoodooUtils.pause(30000); // pause required to sync new emails
		
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
		VoodooUtils.waitForAlertExpiration();
		// click to "Import to Sugar" button 
		new VoodooControl("button", "css", "#importDialog > div.ft > span > span > span > button").click();
		VoodooUtils.waitForAlertExpiration();
		// close the message 
		new VoodooControl("button", "css", "#sugarMsgWindow > div.ft > span > span > span > button").click();
		
		// click on email tab
		new VoodooControl("li", "id", "tree").click();
		VoodooControl myEmailCtrl = new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[2]/table/tbody/tr/td[2]/span"); // xpath used because no suitable CSS selector available 
		myEmailCtrl.waitForVisible();
		// click on My Email folder 
		myEmailCtrl.click();
		// assert imported email in My Email 
		new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[5]/div").assertContains(ds.get(0).get("subject"), true);

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
		new VoodooControl("div", "css", "#emailGrid > div.yui-dt-bd").assertContains(ds.get(0).get("subject"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}