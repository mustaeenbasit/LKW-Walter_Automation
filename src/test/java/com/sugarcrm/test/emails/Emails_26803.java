package com.sugarcrm.test.emails;

import com.sugarcrm.candybean.datasource.FieldSet;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Emails_26803 extends SugarTest {
	StandardSubpanel emailSub;
	FieldSet sendEmail = new FieldSet();

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		DataSource contactRecords = testData.get(testName + "_createContact");
		sendEmail = testData.get(testName + "_sendEmail").get(0);

		sugar().contacts.api.create(contactRecords);
		sugar().login();

		//configure admin->Email Settings
		sugar().admin.setEmailServer(emailSetup);

		sugar().contacts.navToListView();

		// TODO: VOOD-444 - Support creating relationships via API.
		// Add email address to every contact
		for (int i = 1; i <= contactRecords.size(); i++) {
			sugar().contacts.listView.editRecord(i);
			sugar().contacts.listView.getEditField(i, "emailAddress").set(sendEmail.get("email"));
			sugar().contacts.listView.saveRecord(i);
		}
	}

	/**
	 * Verify pagination works correctly in Email Contacts.
	 *
	 * @throws Exception
	 */
	@Test
	public void Emails_26803_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// In Email sub panel, create an Email.
		emailSub = sugar().contacts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSub.composeEmail();

		// VOOD-843 - Need library support for email composer UI
		new VoodooControl("span", "css", "div.record div span.select2-chosen").waitForVisible();
		new VoodooControl("i", "css", "a[data-name='to_addresses'] i").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("span", "css", "div[data-voodoo-name='compose-addressbook-filter'] .select2-choice-type").click();

		// Select Contacts module, select more than 5 Contacts.
		// xpath is needed to select module from dropdown
		new VoodooControl("li", "xpath", "//*[@class='select2-results']/li[contains(.,'Contacts')]").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", ".toggle-all").click();
		new VoodooControl("a", "css", "[name='done_button']").click();

		new VoodooControl("input", "css", "[name='subject']").set(sendEmail.get("subject"));
		VoodooUtils.focusFrame("mce_0_ifr");
		VoodooControl emailBodyCtrl = new VoodooControl("body", "id", "tinymce");
		emailBodyCtrl.waitForVisible();
		emailBodyCtrl.set(sendEmail.get("body"));
		VoodooUtils.focusDefault();

		// click on send email button
		new VoodooControl("span", "css", "[data-voodoo-name = 'send_button']").click();
		sugar().alerts.waitForLoadingExpiration();

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		VoodooUtils.waitForReady();
		emailSub.expandSubpanel();

		// Navigate back to the Contact record->Email record
		new VoodooControl("a", "css", "[data-voodoo-name='name'] a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify pagination in Contact record->Email record
		new VoodooControl("span", "css", "#subpanel_contacts_snip .pagination span.pageNumbers").assertContains("(1 - 5 of 6+)", true);

		// click on next pagination arrow
		new VoodooControl("img", "css", "#list_subpanel_contacts_snip .pagination td table tr td:nth-child(2) button:nth-child(4) img").click();

		// Verify Email Contacts list mores to next page, the contacts are appearing in the next page
		new VoodooControl("span", "css", "#subpanel_contacts_snip .pagination .pageNumbers").assertContains("(6 - 8 of 8)", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}