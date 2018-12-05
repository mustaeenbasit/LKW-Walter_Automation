package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23515 extends SugarTest {
	DataSource contactData = new DataSource();

	public void setup() throws Exception {
		contactData = testData.get(testName);
		sugar().contacts.api.create(contactData);
		sugar().login();
	}

	/**
	 * Verify that contacts can be searched by basic search conditions
	 * @throws Exception
	 */
	@Test
	public void Contacts_23515_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Contacts list view and search with last name
		sugar().contacts.navToListView();
		sugar().contacts.listView.setSearchString(contactData.get(0).get("lastName"));

		// Verifying the correct results are populated after searching with last name
		Assert.assertTrue("The number of records in contact list view is not equal to 3", sugar().contacts.listView.countRows() == 3);
		for (int i = 1; i <= contactData.size(); i++) {
			sugar().contacts.listView.getDetailField(i, "fullName").assertContains(contactData.get(0).get("lastName"), true);
		}

		// Search with first name and verifying the correct results are populated after searching with first name
		sugar().contacts.listView.setSearchString(contactData.get(1).get("firstName"));
		Assert.assertTrue("The number of records in contacts list view is not equal to 1", sugar().contacts.listView.countRows() == 1);
		sugar().contacts.listView.getDetailField(1, "fullName").assertContains(contactData.get(1).get("firstName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}