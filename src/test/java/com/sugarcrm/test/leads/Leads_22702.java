package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_22702 extends SugarTest {
	DataSource leadData = new DataSource();

	public void setup() throws Exception {
		// Creating multiple lead records with different status  
		leadData = testData.get(testName);
		sugar().leads.api.create(leadData);
		sugar().login();
	}

	/**
	 * Search Leads_Verify that leads with different "Status" can be searched by using search filter.
	 * @throws Exception
	 */
	@Test 
	public void Leads_22702_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Leads list view and open filter create window 
		sugar().leads.navToListView();
		sugar().leads.listView.openFilterDropdown();
		sugar().leads.listView.selectFilterCreateNew();

		// Create custom filter for "is any of" operator
		// TODO: VOOD-1478
		FieldSet filterData = testData.get(testName + "_filter").get(0);
		new VoodooSelect("span", "css", ".search-filter .filter-definition-container  span.select2-chosen").set(filterData.get("filterName"));
		VoodooSelect operator = new VoodooSelect("div", "css",".fld_filter_row_operator div");
		operator.set(filterData.get("operator1"));
		VoodooControl value = new VoodooControl("input", "css", ".select2-search-field input");
		value.set(filterData.get("value1"));
		VoodooControl selectResult = new VoodooControl("li", "css", ".select2-results li");
		selectResult.click();
		value.set(filterData.get("value2"));
		selectResult.click();
		VoodooUtils.waitForReady();

		// Verify the data in Leads list view 
		Assert.assertTrue("Record count is not equal to 2 in leads list view", sugar().leads.listView.countRows() == 2);
		VoodooControl firstValueListView = sugar().leads.listView.getDetailField(1, "status");
		firstValueListView.assertEquals(leadData.get(4).get("status"), true);
		VoodooControl secondValueListView = sugar().leads.listView.getDetailField(2, "status");
		secondValueListView.assertEquals(leadData.get(1).get("status"), true);
		sugar().leads.listView.filterCreate.reset();

		// Create custom filter for "is not any of" operator
		operator.set(filterData.get("operator2"));
		value.set(filterData.get("value1"));
		selectResult.click();
		value.set(filterData.get("value2"));
		selectResult.click();
		value.set(filterData.get("value3"));
		selectResult.click();
		VoodooUtils.waitForReady();

		// Verify the data that should be visible in leads list view
		Assert.assertTrue("Record count is not equal to 3 in leads list view", sugar().leads.listView.countRows() == 3);
		firstValueListView.assertEquals(leadData.get(3).get("status"), true);
		secondValueListView.assertEquals(leadData.get(2).get("status"), true);
		sugar().leads.listView.getDetailField(3, "status").assertEquals(leadData.get(0).get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}