package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_26677 extends SugarTest {
	FieldSet emailSetup, customData;
	LeadRecord leadRecord1, leadRecord2;

	public void setup() throws Exception {
		emailSetup = testData.get(testName).get(0);
		customData = testData.get(testName+"_1").get(0);
		
		FieldSet fs = new FieldSet();
		fs.put("emailAddress", emailSetup.get("userName"));
		fs.put("name", customData.get("account_name"));
		sugar.accounts.api.create(fs);
		
		// Create leads and then edit and add primary email
		fs.clear();
		fs.put("emailAddress", emailSetup.get("userName"));
		leadRecord1 = (LeadRecord) sugar.leads.api.create(fs);
		fs.clear();
		fs.put("emailAddress", emailSetup.get("userName"));
		leadRecord2 = (LeadRecord) sugar.leads.api.create(fs);
		sugar.login();
		
		// configure Admin->Email Settings
		sugar.admin.setEmailServer(emailSetup);
		// Set Inbound email settings
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailSetup.get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSetup.get("userName")); // To allow to fetch email 
		new VoodooControl("input", "id", "email_password").set(emailSetup.get("password")); // To allow to fetch email 
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "reply_to_addr").set(emailSetup.get("userName"));
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
	 * Select multiple recipients to compose an email from listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_26677_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.leads.navToListView();	
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.pause(3000); // This pause is required to click on checkbox
		sugar.leads.listView.toggleSelectAll();
		sugar.leads.listView.openActionDropdown();
		// TODO: VOOD-938
		new VoodooControl("a", "css", ".dropdown-menu a[name='mass_email_button']").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("input", "css", "[data-name='to_addresses'] .fld_to_addresses div ul li input").set(customData.get("account_name"));
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("div", "css", "#select2-drop > ul > li > div").click();
		new VoodooControl("input", "css", "input[name='subject']").click();
		new VoodooControl("input", "css", "input[name='subject']").set(customData.get("subject"));
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("a", "css", "a.btn-primary[name='send_button']").click();		
		sugar.alerts.confirmAllAlerts();
		VoodooUtils.waitForAlertExpiration();
		
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		sugar.alerts.waitForLoadingExpiration();
		
		// assert in sub-panel
		new VoodooControl("tr", "xpath", "//*[@data-voodoo-name='Emails']/ul/li/div[2]/div/table/tbody/tr[contains(.,'"+customData.get("subject")+"')]").assertContains(customData.get("subject"), true);
	  		
		// assert check history dashlet
		// TODO: VOOD-963 & VOOD-960
		VoodooControl dashboardTitle =  new VoodooControl("a", "css", "div.dashboard-pane > div > div > div > div > div > div > h1 > span:nth-child(1) > span > div > a");
		VoodooUtils.voodoo.log.info(">>>>:"+dashboardTitle.getText()+">>");
		if(!dashboardTitle.getText().contains("My Dashboard")) {
			//sugar.dashboard.chooseDashboard("My Dashboard");
			new VoodooControl("a", "css", "[data-type='dashboardtitle'] > span > div > a").click();
			VoodooUtils.waitForAlertExpiration();
			VoodooUtils.pause(8000);// Required to populate more dashboard list
			new VoodooControl("a", "css", "[data-type='dashboardtitle'] > span > div > ul > li").click();
			sugar.alerts.waitForLoadingExpiration();
		}
		new VoodooControl("a", "css", "div.dashlet-tabs.tab3 div.dashlet-tab:nth-child(2)").click();
		new VoodooControl("a", "css", ".dashlet-unordered-list .tab-pane.active ul > li > p > a:nth-child(2)").assertContains(customData.get("subject"), true);
		VoodooUtils.waitForAlertExpiration();
		
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(2);
		sugar.alerts.waitForLoadingExpiration();
		// assert in sub-panel
		new VoodooControl("tr", "xpath", "//*[@data-voodoo-name='Emails']/ul/li/div[2]/div/table/tbody/tr[contains(.,'"+customData.get("subject")+"')]").assertContains(customData.get("subject"), true);
				
		// assert check history dashlet		
		new VoodooControl("a", "css", "div.dashlet-tabs.tab3 div.dashlet-tab:nth-child(2)").click();
		new VoodooControl("a", "css", ".dashlet-unordered-list .tab-pane.active ul > li > p > a:nth-child(2)").assertContains(customData.get("subject"), true);
		VoodooUtils.focusDefault();
		
		// assert into Emails->My Sent Email folder
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("span", "css", "#emailtree > div > div > div > table > tbody > tr > td.ygtvcell.ygtvcontent > span").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.pause(8000);
		// Verify sent email
		new VoodooControl("a", "css", "tbody.yui-dt-data > tr > td.yui-dt-col-subject.yui-dt-sortable.yui-dt-resizeable > div").assertContains(customData.get("subject"), true);
	
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}	
}