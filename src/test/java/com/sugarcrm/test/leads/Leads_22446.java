package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22446 extends SugarTest {
	DataSource myRecords = new DataSource();

	public void setup() throws Exception {
		// Create three lead records and Login as a valid user
		myRecords = testData.get(testName);
		sugar().leads.api.create(myRecords);
		sugar().login();
	}

	/**
	 * Search Leads_Verify that leads with different "Status" can be searched by using advanced search function.
	 * @throws Exception
	 */
	@Test
	public void Leads_22446_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Leads module.
		sugar().leads.navToListView();
		
		// Click "Filter -> Create filter".
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.selectFilterCreateNew();

		// Enter custom filter data.
		FieldSet filterData = testData.get(testName + "_filterData").get(0);
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get("fieldName"), filterData.get("displayName"), filterData.get("matchType1"), myRecords.get(0).get("status"), 1);
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get("fieldName"), filterData.get("displayName"), filterData.get("matchType1"), myRecords.get(1).get("status"), 1);
		VoodooUtils.waitForReady();

		// Verify All the leads matching the selected "Status" condition are displayed in "Leads" list view.
		Assert.assertTrue("Could not find required two records.", sugar().leads.listView.countRows() == 2);
		sugar().leads.listView.getDetailField(1, "fullName").assertEquals(myRecords.get(1).get("fullName"), true);
		sugar().leads.listView.getDetailField(2, "fullName").assertEquals(myRecords.get(0).get("fullName"), true);

		// Enter custom filter data.
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get("fieldName"), filterData.get("displayName"), filterData.get("matchType2"), myRecords.get(0).get("status"), 1);
		VoodooUtils.waitForReady();

		// Verify All the leads matching the selected "Status" condition are displayed in "Leads" list view.
		Assert.assertTrue("Could not find required two records.", sugar().leads.listView.countRows() == 2);
		sugar().leads.listView.getDetailField(1, "fullName").assertEquals(myRecords.get(2).get("fullName"), true);
		sugar().leads.listView.getDetailField(2, "fullName").assertEquals(myRecords.get(1).get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}