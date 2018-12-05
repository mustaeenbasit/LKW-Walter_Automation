package com.sugarcrm.test.emails;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Emails_20819 extends SugarTest {
	FieldSet emailSetupData = new FieldSet();
	FieldSet customData = new FieldSet();
	String currentURL = "";
	VoodooControl gmailEmailInputCtrl, gmailPassInputCtrl, signInCtrl, gmailSettingsCtrl, gmailSaveSettingsCtrl, dropdownCtrl;

	public void setup() throws Exception {
		emailSetupData = testData.get(testName).get(0);
		customData = testData.get(testName+"_email").get(0);
		sugar.login();
		currentURL = VoodooUtils.getUrl();

		// Set up outbound email account
		sugar.admin.setEmailServer(emailSetupData);
		sugar.logout();

		// TODO: VOOD-1052
		// French language settings in Gmail server side.
		VoodooUtils.go(customData.get("gmail_server_url"));
		gmailEmailInputCtrl = new VoodooControl("input", "id", "Email");
		gmailEmailInputCtrl.set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "next").click();;
		gmailPassInputCtrl = new VoodooControl("input", "id", "Passwd");
		gmailPassInputCtrl.set(emailSetupData.get("password"));
		signInCtrl = new VoodooControl("input", "id", "signIn");
		signInCtrl.click();
		gmailSettingsCtrl = new VoodooControl("div", "css", "[gh='s']");
		gmailSettingsCtrl.click();
		dropdownCtrl = new VoodooControl("div", "css", "#ms div");
		dropdownCtrl.click();
		new VoodooControl("option", "css", ".a5p [value='fr']").click();
		gmailSaveSettingsCtrl = new VoodooControl("button", "css", "[guidedhelpid='save_changes_button']");
		gmailSaveSettingsCtrl.click();

		// sign out from gmail account
		new VoodooControl("a", "css", "[title='Compte qa.sugar.qa.79@gmail.com']").click();
		new VoodooControl("a", "xpath", "//*[@id='gb']/div[1]/div[1]/div/div[3]/div[2]/div[3]/div[2]/a").click();
	}

	/**
	 * Special character folder can be monitor.
	 * @throws Exception
	 */
	@Ignore("VOOD-1273: Need Lib support for accessing GMAIL service controls.")
	@Test
	public void Emails_20819_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooUtils.go(currentURL);
		sugar.login();
		sugar.navbar.navToModule(sugar.emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-672, Set email settings individually
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("img", "css", "#outboundAccountsTable td.yui-dt-col-edit div img").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();

		// fill the the Incoming Email properties and outgoing Email properties
		new VoodooControl("input", "id", "ie_name").set(customData.get("name"));
		new VoodooControl("input", "id", "email_user").set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "email_password").set(emailSetupData.get("password"));
		new VoodooControl("input", "id", "mailbox").set(customData.get("inbox"));
		new VoodooControl("input", "id", "trashFolder").set(customData.get("trash"));
		new VoodooControl("input", "id", "sentFolder").set(customData.get("sent_folder"));
		new VoodooControl("input", "id", "ie_from_addr").set(emailSetupData.get("userName"));
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

		// nav to email module
		sugar.navbar.navToModule(sugar.emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-843 Need lib support to handle new email composer UI
		new VoodooControl("button", "id", "composeButton").click(); // click on compose mail button
		VoodooUtils.waitForAlertExpiration(); // required to populate compose form UI data completely
		new VoodooControl("input", "id", "addressTO1").set(customData.get("mail_to"));
		new VoodooControl("input","id","emailSubject1").set(customData.get("subject"));
		VoodooUtils.focusFrame("htmleditor1_ifr"); // focus email body iframe
		new VoodooControl("body", "id", "tinymce").set(customData.get("body"));
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		// click send button in compose mail UI
		new VoodooControl("button", "css", "#composeHeaderTable1 tr:nth-child(1) td:nth-child(1) button:nth-child(1)").click();
		sugar.alerts.waitForLoadingExpiration();

		// click checkemail button to sync email inbox
		new VoodooControl("button", "id", "checkEmailButton").click();

		// TODO: VOOD-743
		sugar.emails.waitForSugarMsgWindow(30000);
		sugar.emails.waitForSugarMsgWindow(30000);

		// Verify Email record appears in the Emails -> My Sent Mail folder
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[contains(.,'My Email')]/table/tbody/tr/td[2]/span").click();
		new VoodooControl("div", "xpath", "//*[@id='emailtree']/div/div/div[contains(.,'My Email')]/div/div[contains(.,'My Sent Email')]").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr").assertContains(customData.get("subject"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}