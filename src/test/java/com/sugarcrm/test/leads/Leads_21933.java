package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21933 extends SugarTest {
	DataSource myRecords = new DataSource();

	public void setup() throws Exception {
		// Create three lead records and Login as a valid user
		myRecords = testData.get(testName);
		sugar().leads.api.create(myRecords);
		sugar().login();
	}

	/**
	 * Verify search will return correct result by creating filter
	 * @throws Exception
	 */
	@Test
	public void Leads_21933_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Leads" module.
		sugar().leads.navToListView();
		
		// Click "Filter -> Create filter".
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.selectFilterCreateNew();

		// Enter custom filter data.
		DataSource filterData = testData.get(testName + "_filterData");
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get(0).get("fieldName"), filterData.get(0).get("displayName"), filterData.get(0).get("matchType"), myRecords.get(0).get("accountName").substring(0,7), 1);
		sugar().leads.listView.filterCreate.getControl("addFilterRow01").click();
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get(1).get("fieldName"), filterData.get(1).get("displayName"), filterData.get(1).get("matchType"), myRecords.get(0).get("phoneWork"), 2);
		sugar().leads.listView.filterCreate.getControl("addFilterRow02").click();
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get(2).get("fieldName"), filterData.get(2).get("displayName"), filterData.get(2).get("matchType"), myRecords.get(1).get("status"), 3);
		sugar().leads.listView.filterCreate.getControl("addFilterRow03").click();
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get(2).get("fieldName"), filterData.get(2).get("displayName"), filterData.get(2).get("matchType"), myRecords.get(2).get("status"), 3);
		sugar().leads.listView.filterCreate.getControl("addFilterRow04").click();
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get(3).get("fieldName"), filterData.get(3).get("displayName"), filterData.get(3).get("matchType"), VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), 4);
		
		// Enter custom filter name and save the filter
		sugar().leads.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().leads.listView.filterCreate.save();

		// Reload Leads list view page.
		sugar().leads.navToListView();

		// Verify Filter is applied for leads list. Custom filter stays applied.
		Assert.assertTrue("More than one record found", sugar().leads.listView.countRows() == 1);
		sugar().leads.listView.getDetailField(1, "fullName").assertEquals(myRecords.get(0).get("fullName"), true);

		// Custom filter is applied and its name appears next to "Filter" drop-down.
		// TODO: VOOD-1752 Need lib support for verifying the filter label text  in List View
		new VoodooControl("span", "css", ".choice-filter-label").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}