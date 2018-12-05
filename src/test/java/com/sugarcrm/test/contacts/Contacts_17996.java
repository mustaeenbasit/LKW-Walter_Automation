package com.sugarcrm.test.contacts;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_17996 extends SugarTest {
	FieldSet emailData,emailSetupData;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		emailSetupData = testData.get("env_email_setup").get(0);
		emailData = testData.get(testName+"_email").get(0);
		sugar().login();
		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetupData);
	}

	/**
	 * Verify Recipient "Related To" field is populated in Email Compose from listview
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore("MAR-3038 - Email Id is getting displayed in 'To' field instead of Record Name in Compose Email Form.")
	public void Contacts_17996_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.getEditField(1, "emailAddress").set(emailData.get("userName"));
		sugar().contacts.listView.saveRecord(1);
		// In Contacts module list view, Click on any email address.
		sugar().contacts.listView.getDetailField(1, "emailAddress").click();

		// Verify the email composer page is opened
		sugar().contacts.recordView.composeEmail.assertVisible(true);

		// Verify FullName of the Contact's record is populated in To field.
		// TODO: VOOD-1469
		VoodooControl toAddressCtrl = new VoodooControl("span", "css", "[data-voodoo-name='to_addresses'] .select2-search-choice");
		toAddressCtrl.assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
		sugar().alerts.waitForLoadingExpiration();

		// Mouse over the FullName
		toAddressCtrl.hover();
		sugar().alerts.waitForLoadingExpiration();
		// Verify tooltip displays email address of the contact's record
		// TODO: VOOD-1292
		new VoodooControl("div", "css", ".tooltip-inner").assertEquals(emailData.get("userName"), true);

		// Click on More link
		// TODO: VOOD-843
		new VoodooControl("button", "css", ".btn-link.btn-invisible.more").click();
		// Verify "Related To" field displays Contacts module name; Full name of the Contact record.
		sugar().contacts.recordView.composeEmail.getControl("parentModule").assertContains(sugar().contacts.moduleNameSingular, true);
		sugar().contacts.recordView.composeEmail.getControl("parentRecord").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);

		// Enter Subject & Body of the mail
		sugar().accounts.recordView.composeEmail.getControl("subject").set(emailData.get("subject"));
		sugar().accounts.recordView.composeEmail.addBodyMessage(emailData.get("body"));
		// Click on "Send"
		sugar().accounts.recordView.composeEmail.getControl("sendButton").click();
		sugar().alerts.waitForLoadingExpiration(40000); // Extra wait required for the loading message
		// Open up that sent email from Emails sub panel in Contacts module
		StandardSubpanel emailSubpanel = sugar().contacts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		sugar().contacts.listView.clickRecord(1);
		emailSubpanel.scrollIntoView();
		new VoodooControl("a", "css", ".list.fld_name[data-voodoo-name='name'] a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Contacts field has FullName
		// TODO: VOOD-792, VOOD-1469
		new VoodooControl("td", "css", ".detail.view tr:nth-child(2) td:nth-child(4)").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
		// Verify To field as FullName <emailAddress>
		new VoodooControl("td", "css", ".detail.view tr:nth-child(4) td:nth-child(2)").assertContains(sugar().contacts.getDefaultData().get("fullName")+" <"+emailData.get("userName")+">", true);
		VoodooUtils.focusDefault();
	}

	public void cleanup() throws Exception {}
}
