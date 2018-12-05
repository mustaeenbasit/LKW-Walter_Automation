package com.sugarcrm.test.emails;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Emails_26689 extends SugarTest {
	public void setup() throws Exception {
		FieldSet emailSetupData = testData.get("env_email_setup").get(0);
		UserRecord qauser=  new UserRecord(sugar().users.getQAUser());

		// smtp settings
		sugar().login();
		sugar().admin.setEmailServer(emailSetupData);
		sugar().logout();

		// Login as QAUser
		qauser.login();

		// Set email settings individually
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1078
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSetupData.get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(emailSetupData.get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "reply_to_addr").set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "saveButton").click();

		// Wait for expiration of 3 messages Window
		sugar().emails.waitForSugarMsgWindow(60000);
		sugar().emails.waitForSugarMsgWindow(60000);
		sugar().emails.waitForSugarMsgWindow(60000);

		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.refresh(); // Needed to refresh email module after email account is configured.
		VoodooUtils.focusDefault();
	}

	/**
	 * Sent/archived email should appear in Emails sub panel
	 * @throws Exception
	 */
	@Test
	public void Emails_26689_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("firstName").set(customData.get("firstName"));
		sugar().contacts.createDrawer.getEditField("lastName").set(customData.get("lastName"));
		sugar().contacts.createDrawer.getEditField("emailAddress").set(customData.get("from_address"));
		sugar().contacts.createDrawer.save();
		sugar().contacts.listView.clickRecord(1);

		// Click on email-subpanel
		StandardSubpanel emailSub = sugar().contacts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSub.composeEmail();

		VoodooControl subjectFieldCtrl = sugar().contacts.recordView.composeEmail.getControl("subject");
		subjectFieldCtrl.set(customData.get("subject_msg"));
		sugar().accounts.recordView.composeEmail.addBodyMessage(customData.get("body_msg"));
		sugar().contacts.recordView.composeEmail.getControl("sendButton").click();
		VoodooUtils.waitForReady();

		// Verifying record in email-subpanel
		emailSub.assertContains(customData.get("subject_msg"), true);

		// TODO: VOOD-963 & VOOD-960
		VoodooControl dashboardTitle =  new VoodooControl("a", "css", "[data-voodoo-name*='dashboard-headerpane'] [data-voodoo-name='name'] a");
		if(!dashboardTitle.queryContains("My Dashboard", true))
			sugar().dashboard.chooseDashboard("My Dashboard");

		// TODO: VOOD-798 -Lib support in create/verify Archive Email from History Dashlet
		new VoodooControl("a", "css", "li.row-fluid.sortable:nth-child(2) span.btn-group.dashlet-toolbar a").click();

		// TODO: VOOD-999
		new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-797 -Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		new VoodooControl("input", "css", ".fld_from_address input").set(customData.get("from_address"));
		VoodooUtils.waitForReady();

		subjectFieldCtrl.set(customData.get("subject_msg"));
		VoodooUtils.focusFrame(0);
		new VoodooControl("body", "css", "#tinymce").set(customData.get("body_msg"));
		VoodooUtils.focusDefault();
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		sugar().alerts.confirmAllAlerts();
		VoodooUtils.waitForReady();

		// TODO: VOOD-738 -Need to add a lib support for the actions in the record view
		// Verifying record in email-subpanel (Archive)
		emailSub.expandSubpanel();
		emailSub.assertContains(customData.get("subject_msg"), true);
		emailSub.assertContains(customData.get("archive_status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}