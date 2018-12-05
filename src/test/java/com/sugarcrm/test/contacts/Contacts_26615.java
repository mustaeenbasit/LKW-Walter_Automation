package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_26615 extends SugarTest {
	DataSource contactData = new DataSource();
	FieldSet customData = new FieldSet();
	FieldSet systemSettings = new FieldSet();
	VoodooControl entryPerPageCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		contactData = testData.get(testName + "_name");
		sugar().contacts.api.create(contactData);
		sugar().login();

		systemSettings.put("maxEntriesPerPage", customData.get("numberOfEntries"));
		sugar().admin.setSystemSettings(systemSettings);
	}

	/**
	 * Test verifies that clicking on the "more records" link shows more
	 * records.
	 */
	@Test
	public void Contacts_26615_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select All records in contacts list view
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		VoodooControl selectedRecords = sugar().contacts.listView.getControl("selectedRecordsAlert");
		selectedRecords.assertContains(customData.get("alertMessage1"), true);

		// Click on "More Contacts" link
		sugar().contacts.listView.showMore();
		sugar().contacts.listView.toggleSelectAll();

		// Verifying more records are appearing on the list
		selectedRecords.assertContains(customData.get("alertMessage2"), true);

		// Click on "More Contacts" link
		sugar().contacts.listView.showMore();
		sugar().contacts.listView.toggleSelectAll();

		// Verifying more records are appearing on the list
		selectedRecords.assertContains(customData.get("alertMessage3"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
