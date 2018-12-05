package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.ContactRecord;

public class Contacts_26111 extends SugarTest {
	FieldSet contactsRecord;
	ContactRecord myContact;

	public void setup() throws Exception {
		contactsRecord = testData.get(testName).get(0);
		myContact = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify the user can Cancel the inline editing from the record level
	 * action drop down on Contact List View
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_26111_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();

		// TODO: VOOD-662
		sugar().contacts.listView.toggleSidebar();

		// Inline Edit
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.setEditFields(1, contactsRecord);
		sugar().contacts.listView.cancelRecord(1);

		// Assert the original default Work phone value is present
		sugar().contacts.listView.verifyField(1, "phoneWork",
				myContact.get("phoneWork"));
	}

	public void cleanup() throws Exception {}
}
