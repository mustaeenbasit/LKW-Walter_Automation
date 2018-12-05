package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_24242 extends SugarTest {
	ContactRecord contact1;

	public void setup() throws Exception {
		sugar().login();
		contact1 = (ContactRecord)sugar().contacts.api.create();
	}

	/*
	 * Verify New action dropdown list in contacts list view page
	 */
	@Test
	public void Contacts_24242_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts list view
		sugar().contacts.navToListView();

		// Click checkbox on one contact record in list view
		sugar().contacts.listView.checkRecord(1);

		// Open action drop down list in list view
		sugar().contacts.listView.openActionDropdown();

		// Verify the following actions in the drop down list
		// Note: Email action is not available in the current release: see PM-134
		sugar().contacts.listView.getControl("massUpdateButton").assertExists(true);  // Mass Update
		sugar().contacts.listView.getControl("deleteButton").assertExists(true);  // Delete
		sugar().contacts.listView.getControl("exportButton").assertExists(true);  // Export
		// TODO: VOOD-689
		new VoodooControl("input","css","ul.dropdown-menu:nth-child(3) li:nth-child(2) a").assertExists(true); // Merge
		// TODO: VOOD-528
		new VoodooControl("input","css","ul.dropdown-menu:nth-child(3) li:nth-child(3) a").assertExists(true); // Add to Target List

		// Trigger the delete action to delete the checked record in list view
		sugar().contacts.listView.getControl("deleteButton").click();
		sugar().contacts.listView.confirmDelete();
		VoodooUtils.waitForReady();

		sugar().contacts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
