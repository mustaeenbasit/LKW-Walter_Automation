package com.sugarcrm.test.emails;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_18267 extends SugarTest {
	FieldSet customData;
	FieldSet emailSetupData;
	VoodooControl subjectCtrl;

	public void setup() throws Exception {				
		customData = testData.get(testName).get(0);
		emailSetupData = testData.get("env_email_setup").get(0);
		sugar.login();
	}

	/**
	 * Verify that Sugar email client is popup when "Email Client" is set to "Sugar Email Client"
	 * @throws Exception
	 */
	@Test
	public void Emails_18267_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		// smtp settings
		sugar.admin.setEmailServer(emailSetupData);
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-563
		// Verifying sugar email client
		new VoodooControl("td", "css", "#email_options_link_type td:nth-of-type(2)").assertEquals(customData.get("mail_client"), true);
		VoodooUtils.focusDefault();
		sugar.logout(); // for smtp settings configuration update/reflect

		// Login as Admin
		sugar.login();
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);

		// Verifying mail client is open with FROM address field
		// TODO: VOOD-798
		VoodooControl toAddressCtrl = new VoodooControl("span", "css", ".fld_to_addresses.edit div ul li:nth-of-type(1) div span");
		new VoodooControl("span", "css", "#drawers div.record div:nth-child(1) fieldset div:nth-child(1) span.select2-chosen").assertContains(customData.get("admin_email_address"), true);
		VoodooControl cancelCtrl = new VoodooControl("a", "css", "div[data-voodoo-name='compose'] .fld_cancel_button.detail a");
		cancelCtrl.click();

		// Contact record
		sugar.contacts.navToListView();
		sugar.contacts.listView.create();
		sugar.contacts.createDrawer.getEditField("firstName").set(customData.get("firstName"));
		sugar.contacts.createDrawer.getEditField("lastName").set(customData.get("lastName"));

		// TODO: VOOD-866
		new VoodooControl("input", "css", "input.newEmail.input-append").set(customData.get("email_address"));

		sugar.contacts.createDrawer.save();
		sugar.contacts.listView.clickRecord(1);
		sugar.contacts.recordView.getDetailField("emailAddress").click();

		// Verifying existing contact record with name and email address in TO field
		String fullName = String.format("%s %s", customData.get("firstName"),customData.get("lastName"));
		toAddressCtrl.assertEquals(fullName, true);
		toAddressCtrl.assertAttribute("data-title", customData.get("email_address"), true);
		cancelCtrl.click();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}