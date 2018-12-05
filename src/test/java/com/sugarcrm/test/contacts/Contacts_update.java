package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Contacts_update extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
	}

	@Test
	public void Contacts_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("firstName", "David");

		// Delete the account using the UI.
		myContact.edit(newData);

		// Verify the account was edited.
		myContact.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}