package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

import static org.junit.Assert.assertEquals;

public class Contacts_delete extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
	}

	@Test
	public void Contacts_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the account using the UI.
		myContact.delete();

		// Verify the account was deleted.
		sugar().contacts.navToListView();
		assertEquals(VoodooUtils.contains(myContact.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}