package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Contacts_26112 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().login();
		myContact = (ContactRecord) sugar().contacts.api.create();
	}

	/**
	 * Verify the user can preview a record on the Contact list view
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_26112_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.previewRecord(1);
		myContact.verifyPreview();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
