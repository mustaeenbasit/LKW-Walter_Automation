package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_29775 extends SugarTest {

	public void setup() throws Exception {
		// Create 5 contact records(test data)
		DataSource contactsData = testData.get(testName);
		sugar().contacts.api.create(contactsData);

		sugar().login();
		// Set ListView items per page to 3
		FieldSet itemsPerPage = testData.get(testName+"_Limit").get(0);
		sugar().admin.setSystemSettings(itemsPerPage); 
	}

	/**
	 * Verify "Clear selection" still appears even when user clicks on "more contacts..."
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contacts_29775_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts > View contacts
		sugar().contacts.navToListView();

		// Click on "Select All" checkbox
		sugar().contacts.listView.toggleSelectAll();

		// Now click on "Select all records" link shown 
		sugar().accounts.listView.getControl("selectedRecordsAlert").click();

		// Click on "More Contacts" 
		sugar().contacts.listView.showMore();

		// Verify Clear selections." message should be displayed even when clicked on "More contacts..."
		sugar().contacts.listView.getControl("clearSelectionsLink").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
