package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Contacts_26110 extends SugarTest {
	FieldSet contactsRecord;

	public void setup() throws Exception {
		contactsRecord = testData.get(testName).get(0);
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that the in line edit works for Contacts List View
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_26110_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();

		// TODO: VOOD-662
		sugar().contacts.listView.toggleSidebar();

		// In-line Edit the record and save
		sugar().contacts.listView.updateRecord(1, contactsRecord);

		// Verify Record updated
		sugar().contacts.listView.verifyField(1, "phoneWork",
				contactsRecord.get("phoneWork"));
		sugar().contacts.listView.verifyField(1, "title",
				contactsRecord.get("title"));
	}

	public void cleanup() throws Exception {}
}
