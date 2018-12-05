package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23516 extends SugarTest {
	FieldSet customData = new FieldSet();
	FieldSet contactData = new FieldSet();

	public void setup() throws Exception {
		// Multiple contact records from api
		sugar().contacts.api.create();
		// from CSV
		customData = testData.get(testName).get(0);
		contactData = new FieldSet();
		contactData.put("firstName", customData.get("firstName"));
		contactData.put("lastName", customData.get("lastName"));
		sugar().contacts.api.create(contactData);
		sugar().login();
	}

	/**
	 * Verify that contacts can be searched via filter
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23516_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();
		sugar().contacts.listView.filterCreate.setFilterFields("firstName", customData.get("filter_field1"), customData.get("operator"), customData.get("firstName"), 1);
		sugar().contacts.listView.filterCreate.clickAddRow(1);
		sugar().contacts.listView.filterCreate.setFilterFields("lastName", customData.get("filter_field2"), customData.get("operator"), customData.get("lastName"), 2);
		sugar().contacts.listView.filterCreate.save();

		// Expected Result "The contacts matching the searching conditions are displayed."
		sugar().contacts.listView.verifyField(1, "fullName", customData.get("firstName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
