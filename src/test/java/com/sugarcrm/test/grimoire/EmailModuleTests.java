package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class EmailModuleTests extends SugarTest {
	AccountRecord myAcc;

	public void setup() throws Exception {
		myAcc = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void verifyEmailMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEmailMenuItems()...");

		// Navigation to Email and Email Module Menu Items verification
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		sugar().navbar.selectMenuItem(sugar().emails, "createTemplate");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1077
		new VoodooControl("input", "css", "#name").assertVisible(true);
		VoodooUtils.focusDefault();

		sugar().navbar.selectMenuItem(sugar().emails, "viewTemplates");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1077
		new VoodooControl("input", "css", "#name_basic").assertVisible(true);
		VoodooUtils.focusDefault();

		sugar().navbar.selectMenuItem(sugar().emails, "viewEmails");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1077
		new VoodooControl("div", "css", "#lefttabs").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEmailMenuItems() complete.");
	}

	@Test
	public void composeEmailBWC() throws Exception {
		VoodooUtils.voodoo.log.info("Running composeEmailBWC()...");

		// Setup outgoing email settings
		FieldSet emailSettings = testData.get("env_email_setup").get(0);
		sugar().admin.setEmailServer(emailSettings);

		// Setup incoming email settings
		sugar().emails.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1078
		new VoodooControl("button", "css", "#settingsButton").click();
		new VoodooControl("a", "css", "#accountSettings a").click();
		new VoodooControl("input", "css", "#settingsTabDiv #addButton").click();
		new VoodooControl("a", "css", "#prefill_gmail_defaults_link").click();
		new VoodooControl("input", "css", "#ie_name").set("Test Inbound Email Account");
		new VoodooControl("input", "css", "#email_user").set(emailSettings.get("userName"));
		new VoodooControl("input", "css", "#email_password").set(emailSettings.get("password"));
		new VoodooControl("input", "css", "#trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "css", "#sentFolder").set("[Gmail]/Sent");
		new VoodooControl("input", "css", "#saveButton").click();
		VoodooUtils.pause(1000);

		// Wait for expiration of 3 Messages Window
		sugar().emails.waitForSugarMsgWindow(60000);
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.refresh();
		VoodooUtils.focusDefault();

		QuoteRecord myQuote = (QuoteRecord)sugar().quotes.create();

		// BWC Compose Email
		myQuote.navToRecord();
		BWCSubpanel activitiesSub = (BWCSubpanel)sugar().quotes.detailView.subpanels.get(sugar().emails.moduleNamePlural);
		activitiesSub.composeEmail();
		sugar().quotes.detailView.composeEmail.addCCAddresses();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.detailView.composeEmail.getControl("ccField").assertVisible(true);
		VoodooUtils.focusDefault();
		sugar().quotes.detailView.composeEmail.addBodyMessage("Message body of email, BWC Subpanel...");

		// Close BWC Compose Email
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.detailView.composeEmail.getControl("closeButton").click();

		VoodooUtils.voodoo.log.info("composeEmailBWC() complete.");
	}

	@Test
	public void composeEmailSIDECAR() throws Exception {
		VoodooUtils.voodoo.log.info("Running composeEmailSIDECAR()...");

		// Sidecar Compose Email
		myAcc.navToRecord();
		StandardSubpanel emailSub = (StandardSubpanel)sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSub.composeEmail();
		sugar().accounts.recordView.composeEmail.addBodyMessage("This is the email body message...");
		sugar().accounts.recordView.composeEmail.toggleBCCAddresses();
		VoodooControl bccField = sugar().accounts.recordView.composeEmail.getControl("bccField");

		bccField.assertVisible(true);

		sugar().accounts.recordView.composeEmail.toggleBCCAddresses();
		bccField.assertVisible(false);

		// Cancel Compose Email
		sugar().accounts.recordView.composeEmail.cancel();

		VoodooUtils.voodoo.log.info("composeEmailSIDECAR() complete.");
	}

	@Test
	public void emailFieldTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running emailFieldTest()...");

		sugar().leads.api.create();

		// Navigate to lead record and edit it by adding email address
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		sugar().leads.createDrawer.getEditField("emailAddress").set("admin@sugar.com");
		sugar().leads.recordView.save();

		// Verify email address on the record view
		sugar().leads.recordView.getDetailField("emailAddress").assertEquals("admin@sugar.com", true);

		// Verify email address on the list view
		sugar().leads.navToListView();
		sugar().leads.listView.verifyField(1, "emailAddress", "admin@sugar.com");

		// Update record in the record view
		sugar().leads.listView.toggleSidebar();
		sugar().leads.listView.editRecord(1);
		sugar().leads.listView.getEditField(1, "emailAddress").set("user@yahoo.com");
		sugar().leads.listView.saveRecord(1);
		sugar().leads.listView.toggleSidebar();

		// Verify email address in preview
		sugar().leads.listView.previewRecord(1);
		sugar().previewPane.getPreviewPaneField("emailAddress").assertEquals("user@yahoo.com", true);

		VoodooUtils.voodoo.log.info("emailFieldTest() complete.");
	}

	public void cleanup() throws Exception {}
}