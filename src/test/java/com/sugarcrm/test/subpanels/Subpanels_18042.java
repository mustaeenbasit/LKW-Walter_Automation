package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_18042 extends SugarTest {
	ContactRecord myContact;
	StandardSubpanel contactsSubpanel;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		myContact = (ContactRecord)sugar.contacts.api.create();
		sugar.login();

		// Link a contact to an Account
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		contactsSubpanel = sugar.accounts.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(myContact);
	}

	/**
	 * Verify preview record in subpanel list.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_18042_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click preview button in Contacts record in subpanel.
		contactsSubpanel.scrollIntoView();
		contactsSubpanel.clickPreview(1);

		// Verify Contact preview subpanel is shown correctly, the contact information is correct
		myContact.verifyPreview();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}