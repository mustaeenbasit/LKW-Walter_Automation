package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_17545 extends SugarTest {

	ContactRecord contact1;

	public void setup() throws Exception {
		sugar().login();
		contact1 = (ContactRecord)sugar().contacts.api.create();
	}

	/*
	 * Verify what actions are in the drop down in contact listview
	 */

	@Test
	public void Contacts_17545_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts list view
		sugar().contacts.navToListView();

		// Click on action drop down button at the end of record.
		sugar().contacts.listView.openRowActionDropdown(1);

		// Verify the action drop down list included edit, follow and delete actions.
		sugar().contacts.listView.getControl("edit01").assertExists(true);
		sugar().contacts.listView.getControl("follow01").assertExists(true);
		sugar().contacts.listView.getControl("delete01").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
