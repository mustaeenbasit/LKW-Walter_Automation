package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_24119 extends SugarTest {
	DataSource searchData = new DataSource();
	DataSource filterData = new DataSource();

	public void setup() throws Exception {
		searchData = testData.get(testName);
		filterData = testData.get(testName+"_filters");
		sugar().contacts.api.create(searchData);
		sugar().accounts.api.create();
		sugar().login();

		// Navigate to Contact Record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();

		// Linking an Account to a Contact Record
		FieldSet fs = new FieldSet();
		fs.put("relAccountName", searchData.get(0).get("relAccountName"));
		sugar().contacts.recordView.setFields(fs);
		sugar().alerts.getWarning().confirmAlert();

		// Setting Email in an contact Record
		sugar().contacts.recordView.getEditField("emailAddress").set(searchData.get(0).get("emailAddress"));
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify that contact search conditions can be Reset
	 * @throws Exception
	 */
	@Test
	public void Contacts_24119_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating filter
		sugar().contacts.navToListView();
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();
		for (int i = 0; i < filterData.size(); i++){
			sugar().contacts.listView.filterCreate.setFilterFields(filterData.get(i).get("filterName"), filterData.get(i).get("displayFilter"), filterData.get(i).get("operator"), filterData.get(i).get("value"), 1);
			sugar().contacts.listView.filterCreate.reset();
		}

		// Verify that all available records are displayed as all filters have been reset
		Assert.assertEquals("Total no. of Records not equal to 3", 3, sugar().contacts.listView.countRows());

		// Verify that all available records are displayed as all filters have been reset
		for (int i = 1; i <= searchData.size(); i++){
			sugar().contacts.listView.verifyField(i, "fullName", (searchData.get(searchData.size()-i).get("firstName")) + " "+ (searchData.get(searchData.size()-i).get("lastName")));
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
