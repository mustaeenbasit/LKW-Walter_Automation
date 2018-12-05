package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_29861 extends SugarTest {
	DataSource contactData = new DataSource();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		contactData = testData.get(testName);
		sugar().contacts.api.create(contactData);
		sugar().login();
	}

	/**
	 * Verify user navigates back to all records after closing filter pill
	 * @throws Exception
	 */
	@Test
	public void Contacts_29861_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Editing a Contact record to add account name
		sugar().contacts.navToListView();
		sugar().contacts.listView.sortBy("headerFullname", true);
		sugar().contacts.listView.clickRecord(2);
		sugar().contacts.recordView.edit();
		String accName = sugar().accounts.getDefaultData().get("name");
		sugar().contacts.recordView.getEditField("relAccountName").set(accName);
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.recordView.save();

		// Applying Filter in Contacts's list view
		sugar().contacts.navToListView();
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();

		// Set filter Fields
		FieldSet filterData = testData.get(testName + "_filterData").get(0);
		sugar().contacts.listView.filterCreate.setFilterFields("relAccountName", filterData.get("columnDisplayName"), filterData.get("operator"), accName, 1);

		// Asserting the records in Contact's list view after applying the filter
		Assert.assertTrue("Incorrect no. of records present in list view with filter", sugar().contacts.listView.countRows() == 1);
		String fullName = String.format("%s %s", contactData.get(1).get("firstName") , contactData.get(1).get("lastName"));
		sugar().contacts.listView.verifyField(1, "fullName",fullName);
		sugar().contacts.listView.verifyField(1, "relAccountName", accName);

		// Canceling the filter in list view
		sugar().contacts.listView.filterCreate.cancel();
		VoodooUtils.waitForReady();

		// Asserting the count of records after canceling the filter in contacts list view
		Assert.assertTrue("Incorrect no. of records present in list view without filter", sugar().contacts.listView.countRows() == 5);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}