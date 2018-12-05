package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21915 extends SugarTest {
	DataSource myRecords = new DataSource();

	public void setup() throws Exception {
		// Create three lead record and Login as a valid user
		myRecords = testData.get(testName);
		sugar().leads.api.create(myRecords);
		sugar().login();
	}

	/**
	 * Save Search filter_Verify that a custom search filter can be saved in Leads module.
	 * @throws Exception
	 */
	@Test
	public void Leads_21915_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Leads" module.
		sugar().leads.navToListView();
		
		// Click "Filter -> Create filter".
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.selectFilterCreateNew();

		// Enter custom filter name and filter data.
		FieldSet filterData = testData.get(testName + "_customData").get(0);
		sugar().leads.listView.filterCreate.setFilterFields(filterData.get("fieldName"), filterData.get("displayName"), filterData.get("matchType"), myRecords.get(0).get("lastName"), 1);
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