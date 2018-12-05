package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_26694 extends SugarTest {
	FieldSet emailSettings;
	DataSource ds;
	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.login();
		sugar.accounts.api.create();

		// configure email settings in admin module
		emailSettings = new FieldSet();
		emailSettings.put("userName", ds.get(0).get("userName"));
		emailSettings.put("password", ds.get(0).get("password"));
		emailSettings.put("allowAllUsers", ds.get(0).get("allowAllUsers"));
		sugar.admin.setEmailServer(emailSettings);
		sugar.alerts.waitForLoadingExpiration();

		// TODO: VOOD-672, Set email settings individually
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set("SugaMail");
		new VoodooControl("input", "id", "email_user").set(ds.get(0).get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set( ds.get(0).get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "ie_from_addr").set( ds.get(0).get("userName"));
		VoodooUtils.pause(4000);
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
		VoodooUtils.focusDefault();
	}

	/**
	 * "Related To" field search in Emails hit correct results
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_26694_execute() throws Exception {
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
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// click send button in compose mail UI
		new VoodooControl("button", "css", "#composeHeaderTable1  tr:nth-child(1)  td:nth-child(1) button:nth-child(1)").click();
		VoodooUtils.waitForAlertExpiration(); // wait for email send action to complete   

		// click checkemail button to sync email inbox
		new VoodooControl("button", "id", "checkEmailButton").click();
		VoodooUtils.waitForAlertExpiration(); // pause required to sync new emails

		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").waitForVisible();

		//  click on email folder inside email tree 
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").click();
		VoodooUtils.waitForAlertExpiration();

		// click on inbox
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span").click();

		// right click on email inside email inbox to import one email record to Sugar
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[4]/div").rightClick();

		// click on "Import to Sugar" option in Context manu
		new VoodooControl("a", "css", "#emailContextMenu > div.bd > ul > li:nth-child(3) > a").click();
		VoodooUtils.waitForAlertExpiration(); 
		new VoodooControl("input", "id" , "parent_type").click();

		// relate  "Account" to selected email 
		new VoodooControl("option", "css", "#parent_type > option:nth-child(2)").click();
		new VoodooControl("input", "id" , "change_parent").click();
		VoodooUtils.pause(1000); // need 1 sec. wait to make test running more consistently on faster connection machines
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "tr.oddListRowS1 > td:nth-child(1) > a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		// click to "Import to Sugar" button 
		new VoodooControl("button", "css", "#importDialog > div.ft > span > span > span > button").click();
		VoodooUtils.waitForAlertExpiration();
		// close the message 
		new VoodooControl("button", "css", "#sugarMsgWindow > div.ft > span > span > span > button").click();

		// click on search tab  
		new VoodooControl("li", "css", "#searchTab").click();
		// click on "More" option in search window
		new VoodooControl("a", "css", "#advancedSearchTable > tbody > tr.toggleClass.visible-search-option > td:nth-child(1) > a").click();

		// TODO: VOOD-1038  need lib support for related to field in Emails search view
		// select "Account" used as "Related to"  
		new VoodooControl("select", "id", "data_parent_type_search").click();
		new VoodooControl("option", "css", "#data_parent_type_search > option:nth-child(2)").click();
		new VoodooControl("a", "css", "#advancedSearchTable > tbody > tr:nth-child(10) > td > a").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "tr.oddListRowS1 > td:nth-child(1) > a").click();
		VoodooUtils.pause(1000); // need 1 sec. wait to make test running more consistently on faster connection machines
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// click on advance search button to search out the correct email
		new VoodooControl("input", "id", "advancedSearchButton").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[5]/div").assertContains(ds.get(0).get("subject"), true);
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
