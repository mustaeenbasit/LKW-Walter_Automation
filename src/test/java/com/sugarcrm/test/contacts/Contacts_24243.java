package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_24243 extends SugarTest {
	ContactRecord contact1;

	public void setup() throws Exception {
		contact1 = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that new action dropdown list in contact detail view page
	 * @throws Exception
	 */
	@Test
	public void Contacts_24243_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contact record view
		contact1.navToRecord();

		// Click action drop down list
		sugar().contacts.recordView.openPrimaryButtonDropdown();

		// Verify the action list in the contacts record view
		sugar().contacts.recordView.getControl("editButton").assertExists(true);  	// Edit
		sugar().contacts.recordView.getControl("deleteButton").assertExists(true);  	// Delete
		sugar().contacts.recordView.getControl("copyButton").assertExists(true);		// Copy

		// TODO: VOOD-738 - Verify remaining options in the action list
		new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='share'] a").assertExists(true); // Share
		new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='manage_subscription_button'] a").assertExists(true); // Manage Subscriptions
		new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='vcard_button'] a").assertExists(true); // Download vCard
		new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='find_duplicates'] a").assertExists(true); // Find Duplicates
		new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='historical_summary_button'] a").assertExists(true);// Historical Summary
		new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='audit_button'] a").assertExists(true);// View Change log

		// Trigger the delete action
		sugar().contacts.recordView.openPrimaryButtonDropdown();
		sugar().contacts.recordView.delete();
		sugar().alerts.confirmAllAlerts();
		sugar().alerts.waitForLoadingExpiration();

		// Verify the contact record is deleted
		sugar().contacts.navToListView();
		sugar().contacts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
