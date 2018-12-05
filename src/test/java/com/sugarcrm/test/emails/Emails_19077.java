package com.sugarcrm.test.emails;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_19077 extends SugarTest {
	FieldSet customData;
	FieldSet emailSetupData;
	VoodooControl subjectCtrl;

	public void setup() throws Exception {				
		customData = testData.get(testName).get(0);
		emailSetupData = testData.get("env_email_setup").get(0);

		sugar.login();
		// smtp settings
		sugar.admin.setEmailServer(emailSetupData);

		// Contact record
		sugar.contacts.navToListView();
		sugar.contacts.listView.create();
		sugar.contacts.createDrawer.getEditField("firstName").set(customData.get("firstName"));
		sugar.contacts.createDrawer.getEditField("lastName").set(customData.get("lastName"));

		// TODO: VOOD-866
		new VoodooControl("input", "css", "input.newEmail.input-append").set(customData.get("email_address"));
		new VoodooControl("a", "css", ".btn.addEmail").click();
		new VoodooControl("button", "css", ".btn.btn-edit[data-emailproperty='invalid_email']").click();

		sugar.contacts.createDrawer.save();
		sugar.contacts.listView.clickRecord(1);
		sugar.logout(); // smtp settings to update/reflect
	}

	/**
	 * Verify that strikethrough line are not in the Address Book listview
	 * @throws Exception
	 */
	@Test
	public void Emails_19077_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Login as Admin
		sugar.login();

		// Quick create - Compose Mail
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);

		// BCC - Address Book
		// TODO: VOOD-798
		new VoodooControl("a", "css", ".compose-sender-options div a:nth-of-type(2)").click();
		new VoodooControl("a", "css", "div[data-name='bcc_addresses'] a").click();
		VoodooUtils.waitForReady();

		// Verifying no strike through mail in address book list
		new VoodooControl("table", "css", ".layout_Emails .flex-list-view-content table").assertContains(customData.get("email_address"), false);
		new VoodooControl("a", "css", ".fld_cancel_button.compose-addressbook-headerpane a").click(); // address book cancel 
		new VoodooControl("a", "css", "div[data-voodoo-name='compose'] .fld_cancel_button.detail a").click(); // compose mail cancel 

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}