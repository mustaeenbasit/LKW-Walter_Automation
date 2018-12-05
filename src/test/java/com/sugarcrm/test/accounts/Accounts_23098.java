package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23098 extends SugarTest {
	StandardSubpanel contactSubpanel;

	public void setup() throws Exception {
		// Create an account and a contact record
		sugar().accounts.api.create();
		ContactRecord myContact = (ContactRecord)sugar().contacts.api.create();

		// Log-in as Admin
		sugar().login();

		// Navigate to the Account's record view and link the existing contact to it
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);
		contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecord(myContact);
	}

	/**
	 * Verify that contact related to this account is viewed correctly.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23098_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		contactSubpanel.scrollIntoViewIfNeeded(false);

		// Click on the contact name in the subpanel
		contactSubpanel.clickRecord(1);

		// Assert that user reached the record view of the contact clicked
		sugar().contacts.recordView.getDetailField("fullName").assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}