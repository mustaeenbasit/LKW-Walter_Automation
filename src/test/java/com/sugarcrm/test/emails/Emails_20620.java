package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_20620 extends SugarTest {
	DataSource dsUser, dsEmail, emailSetup;
	UserRecord demoUser;
	AccountRecord myAccount;

	VoodooControl emailComposeButtonCtrl;
	VoodooControl emailToCtrl;
	VoodooControl emailSubjectCtrl;
	VoodooControl emailBodyCtrl;
	VoodooControl emailsaveDraftCtrl;
	
	VoodooControl paginatorStartCtrl;
	VoodooControl paginatorStartACtrl;
	VoodooControl paginatorEndCtrl;
	VoodooControl paginatorEndACtrl;
	VoodooControl paginatorPreviousCtrl;
	VoodooControl paginatorPreviousACtrl;
	VoodooControl paginatorNextCtrl;
	VoodooControl paginatorNextACtrl;
	VoodooControl paginatorPage1LabelCtrl;
	VoodooControl paginatorPage1Ctrl;
	VoodooControl paginatorPage2LabelCtrl;
	VoodooControl paginatorPage2Ctrl;
	VoodooControl paginatorPage3LabelCtrl;
	VoodooControl paginatorPage3Ctrl;
	
	VoodooControl selectRelatedToCtrl;
	VoodooControl pickRelatedToRecCtrl;
	VoodooControl searchNameCtrl;
	VoodooControl submitForSearchCtrl;
	VoodooControl selectFirstResultRowCtrl;
	
	public void setup() throws Exception {
		dsUser = testData.get(testName);
		dsEmail = testData.get(testName+"_emails");
		emailSetup = testData.get(testName+"_emailsetup");
 		
		myAccount = (AccountRecord) sugar.accounts.api.create();
		
		// TODO: VOOD-672
		emailComposeButtonCtrl = new VoodooControl("button", "id", "composeButton");
		emailToCtrl = new VoodooControl("input", "css", "input[title='To']");
		emailSubjectCtrl = new VoodooControl("input", "css", "#emailtabs > div > div:nth-child(2) > div > div > div > div > div > div:nth-child(2) > form > table > tbody > tr:nth-child(2) > td > div > table > tbody > tr:nth-child(6) > td.emailUIField > div > input");
		emailBodyCtrl = new VoodooControl("body", "id", "tinymce");
 		emailsaveDraftCtrl = new VoodooControl("button", "css", "#emailtabs > div > div:nth-child(2) > div > div > div > div > div > div:nth-child(2) > form > table > tbody > tr:nth-child(1) > th > table > tbody > tr > td:nth-child(1) > button:nth-child(2)");
 		
		// TODO: VOOD-672
 		// For each pagination control, two HTML elements are used - span and a
 		// On first page, Start and Previous pagination buttons are within "span" and in rest of the pages they are within "a"
 		// On last page, End and Next pagination buttons are within "span" and in rest of the pages they are within "a"
 		paginatorStartCtrl = new VoodooControl("button", "css", "div.pagination span.yui-pg-first button");
 		paginatorStartACtrl = new VoodooControl("button", "css", "div.pagination a.yui-pg-first button");
 		paginatorEndCtrl = new VoodooControl("button", "css", "div.pagination span.yui-pg-last button");
 		paginatorEndACtrl = new VoodooControl("button", "css", "div.pagination a.yui-pg-last button");
 		paginatorPreviousCtrl = new VoodooControl("button", "css", "div.pagination span.yui-pg-previous button");
 		paginatorPreviousACtrl = new VoodooControl("button", "css", "div.pagination a.yui-pg-previous button");
 		paginatorNextCtrl = new VoodooControl("button", "css", "div.pagination span.yui-pg-next button");
 		paginatorNextACtrl = new VoodooControl("button", "css", "div.pagination a.yui-pg-next button");
 		
 		// Page no. pagination controls are displayed as "label" when on the same page, otherwise they are displayed as "a"
 		paginatorPage1LabelCtrl = new VoodooControl("span", "xpath", "//div[@class='pagination']/span[@class='yui-pg-pages']/span[contains(.,'1')]");
 		paginatorPage2LabelCtrl = new VoodooControl("span", "xpath", "//div[@class='pagination']/span[@class='yui-pg-pages']/span[contains(.,'2')]");
 		paginatorPage3LabelCtrl = new VoodooControl("span", "xpath", "//div[@class='pagination']/span[@class='yui-pg-pages']/span[contains(.,'3')]");
 		paginatorPage1Ctrl = new VoodooControl("a", "xpath", "//div[@class='pagination']/span[@class='yui-pg-pages']/a[contains(.,'1')]");
 		paginatorPage2Ctrl = new VoodooControl("a", "xpath", "//div[@class='pagination']/span[@class='yui-pg-pages']/a[contains(.,'2')]");
 		paginatorPage3Ctrl = new VoodooControl("a", "xpath", "//div[@class='pagination']/span[@class='yui-pg-pages']/a[contains(.,'3')]");
 		
 		selectRelatedToCtrl = new VoodooControl("select", "xpath", "//table[@class='list']/tbody/tr[1]/th/table/tbody/tr/td[2]/select");
 		pickRelatedToRecCtrl = new VoodooControl("button", "xpath", "//table[@class='list']/tbody/tr[1]/th/table/tbody/tr/td[3]/button");
		searchNameCtrl = new VoodooControl("input", "id", "name_advanced");
		submitForSearchCtrl = new VoodooControl("input", "id", "search_form_submit");
		selectFirstResultRowCtrl = new VoodooControl("a", "css", "body > table.list.view > tbody > tr:nth-of-type(3) > td:nth-child(1) > a");
 		
 		sugar.login();
		// We need to setup Email in admin and create a demoUser to successfully create draft emails
		
		// Set email settings in admin
		FieldSet emailSet = new FieldSet();
		emailSet.put("userName", emailSetup.get(0).get("emailAddress"));
		emailSet.put("password", emailSetup.get(0).get("password"));
		emailSet.put("allowAllUsers", "true");
		sugar.admin.setEmailServer(emailSet);

		// A demoUser will be used to create dummy emails to check pagination
		demoUser = (UserRecord) sugar.users.create(dsUser.get(0));
		
		sugar.logout();
		
		// Login with demoUser to setup and create draft emails
		sugar.login(demoUser);
		
		// TODO: VOOD-672 for all below controls
		// Set email settings individually
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();

		// Set pagination limit to 10
 		new VoodooControl("select", "id", "showNumInList").set("10");

 		// Email Account Setting
 		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();		
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(dsUser.get(0).get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSetup.get(0).get("emailAddress")); // To allow to fetch email 
		new VoodooControl("input", "id", "email_password").set(emailSetup.get(0).get("password")); // To allow to fetch email 
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "reply_to_addr").set(emailSetup.get(0).get("emailAddress"));
		new VoodooControl("input", "id", "saveButton").click();

		// Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(120000);
		// Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(120000);
		// Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(100000);

		VoodooUtils.pause(3000); // Let action complete. Could not find suitable waitForxxx control.
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();

		VoodooUtils.acceptDialog();

		VoodooUtils.focusDefault();
	}

	/**
	 *  Verify that pagination works correctly in Emails Listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20620_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Emails module
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// Create 25 dummy mails
		for (int i=0; i<25; i++) {
			emailComposeButtonCtrl.click();

			emailToCtrl.set(dsEmail.get(i).get("to"));
			emailSubjectCtrl.set(dsEmail.get(i).get("subject"));

			// Select related to Dropdown - Accounts
			selectRelatedToCtrl.set("Account");
			pickRelatedToRecCtrl.click();
			VoodooUtils.focusWindow(1);
			searchNameCtrl.set(myAccount.getRecordIdentifier());
			submitForSearchCtrl.click();
			selectFirstResultRowCtrl.click();
			VoodooUtils.focusWindow(0);
	 		VoodooUtils.focusFrame("bwc-frame");

			VoodooUtils.focusFrame(1);
	 		emailBodyCtrl.set(dsEmail.get(i).get("body"));

	 		VoodooUtils.focusDefault();
	 		VoodooUtils.focusFrame("bwc-frame");
	 		emailsaveDraftCtrl.click();
		}

		VoodooUtils.focusDefault();

		// Logout and Login with demoUser again to properly populate the Emails inbox
		sugar.logout();
		sugar.login(demoUser);

		// Now check pagination
		// Go to Emails module
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		if (!new VoodooControl("td", "xpath", "//*[@id='emailtree']/div/div/div[2]/div/div[1]/table/tbody/tr/td[contains(.,'My Drafts')]").queryVisible() ) {
			new VoodooControl("tr", "xpath", "//*[@id='emailtree']/div/div/div[contains(.,'My Email')]/table/tbody/tr").click();
		}
		new VoodooControl("tr", "xpath", "//*[@id='emailtree']/div/div/div[2]/div/div[contains(.,'My Drafts')]/table/tbody/tr").click();
		VoodooUtils.pause(2000); // Wait for Emails to load

		// Sort on Subject
		new VoodooControl("a", "css", "#emailGrid > div > table > thead > tr > th:nth-of-type(5) a").click();
		VoodooUtils.pause(2000); // Wait for Emails to sort

		// Check Expected Result
		// Page 1
		paginatorStartCtrl.assertExists(true);
		paginatorEndACtrl.assertExists(true);
		paginatorPreviousCtrl.assertExists(true);
		paginatorNextACtrl.assertExists(true);
		paginatorPage1LabelCtrl.assertExists(true);
		paginatorPage2LabelCtrl.assertExists(false);
		paginatorPage3LabelCtrl.assertExists(false);
		paginatorPage1Ctrl.assertExists(false);
		paginatorPage2Ctrl.assertExists(true);
		paginatorPage3Ctrl.assertExists(true);

		// Check if correct Email appears on first row of Email Listview
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(0).get("subject")+"')]").assertVisible(true);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(10).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(20).get("subject")+"')]").assertVisible(false);

		paginatorNextACtrl.click(); // Goto Page 2
		VoodooUtils.pause(1000); // Wait for Emails to load

		// Page 2
		paginatorStartACtrl.assertExists(true);
		paginatorEndACtrl.assertExists(true);
		paginatorPreviousACtrl.assertExists(true);
		paginatorNextACtrl.assertExists(true);
		paginatorPage1LabelCtrl.assertExists(false);
		paginatorPage2LabelCtrl.assertExists(true);
		paginatorPage3LabelCtrl.assertExists(false);
		paginatorPage1Ctrl.assertExists(true);
		paginatorPage2Ctrl.assertExists(false);
		paginatorPage3Ctrl.assertExists(true);

		// Check if correct Email appears on first row of Email Listview
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(0).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(10).get("subject")+"')]").assertVisible(true);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(20).get("subject")+"')]").assertVisible(false);

		paginatorNextACtrl.click(); // Goto Page 3
		VoodooUtils.pause(1000); // Wait for Emails to load

		// Page 3
		paginatorStartACtrl.assertExists(true);
		paginatorEndCtrl.assertExists(true);
		paginatorPreviousACtrl.assertExists(true);
		paginatorNextCtrl.assertExists(true);
		paginatorPage1LabelCtrl.assertExists(false);
		paginatorPage2LabelCtrl.assertExists(false);
		paginatorPage3LabelCtrl.assertExists(true);
		paginatorPage1Ctrl.assertExists(true);
		paginatorPage2Ctrl.assertExists(true);
		paginatorPage3Ctrl.assertExists(false);

		// Check if correct Email appears on first row of Email Listview
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(0).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(10).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(20).get("subject")+"')]").assertVisible(true);

		paginatorPreviousACtrl.click(); // Goto Previous Page 2
		VoodooUtils.pause(1000); // Wait for Emails to load

		// Page 2
		paginatorStartACtrl.assertExists(true);
		paginatorEndACtrl.assertExists(true);
		paginatorPreviousACtrl.assertExists(true);
		paginatorNextACtrl.assertExists(true);
		paginatorPage1LabelCtrl.assertExists(false);
		paginatorPage2LabelCtrl.assertExists(true);
		paginatorPage3LabelCtrl.assertExists(false);
		paginatorPage1Ctrl.assertExists(true);
		paginatorPage2Ctrl.assertExists(false);
		paginatorPage3Ctrl.assertExists(true);

		// Check if correct Email appears on first row of Email Listview
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(0).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(10).get("subject")+"')]").assertVisible(true);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(20).get("subject")+"')]").assertVisible(false);

		paginatorPreviousACtrl.click(); // Goto Previous Page 1
		VoodooUtils.pause(1000); // Wait for Emails to load

		// Page 1
		paginatorStartCtrl.assertExists(true);
		paginatorEndACtrl.assertExists(true);
		paginatorPreviousCtrl.assertExists(true);
		paginatorNextACtrl.assertExists(true);
		paginatorPage1LabelCtrl.assertExists(true);
		paginatorPage2LabelCtrl.assertExists(false);
		paginatorPage3LabelCtrl.assertExists(false);
		paginatorPage1Ctrl.assertExists(false);
		paginatorPage2Ctrl.assertExists(true);
		paginatorPage3Ctrl.assertExists(true);

		// Check if correct Email appears on first row of Email Listview
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(0).get("subject")+"')]").assertVisible(true);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(10).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(20).get("subject")+"')]").assertVisible(false);

		paginatorEndACtrl.click(); // Goto Last Page
		VoodooUtils.pause(1000); // Wait for Emails to load

		// Page 3
		paginatorStartACtrl.assertExists(true);
		paginatorEndCtrl.assertExists(true);
		paginatorPreviousACtrl.assertExists(true);
		paginatorNextCtrl.assertExists(true);
		paginatorPage1LabelCtrl.assertExists(false);
		paginatorPage2LabelCtrl.assertExists(false);
		paginatorPage3LabelCtrl.assertExists(true);
		paginatorPage1Ctrl.assertExists(true);
		paginatorPage2Ctrl.assertExists(true);
		paginatorPage3Ctrl.assertExists(false);

		// Check if correct Email appears on first row of Email Listview
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(0).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(10).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(20).get("subject")+"')]").assertVisible(true);

		paginatorStartACtrl.click(); // Goto First Page
		VoodooUtils.pause(1000); // Wait for Emails to load

		// Page 1
		paginatorStartCtrl.assertExists(true);
		paginatorEndACtrl.assertExists(true);
		paginatorPreviousCtrl.assertExists(true);
		paginatorNextACtrl.assertExists(true);
		paginatorPage1LabelCtrl.assertExists(true);
		paginatorPage2LabelCtrl.assertExists(false);
		paginatorPage3LabelCtrl.assertExists(false);
		paginatorPage1Ctrl.assertExists(false);
		paginatorPage2Ctrl.assertExists(true);
		paginatorPage3Ctrl.assertExists(true);

		// Check if correct Email appears on first row of Email Listview
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(0).get("subject")+"')]").assertVisible(true);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(10).get("subject")+"')]").assertVisible(false);
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+dsEmail.get(20).get("subject")+"')]").assertVisible(false);

		VoodooUtils.focusDefault();

 		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
