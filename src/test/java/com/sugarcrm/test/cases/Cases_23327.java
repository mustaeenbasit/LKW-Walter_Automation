package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23327 extends SugarTest {
	FieldSet createFilter = new FieldSet();

	public void setup() throws Exception {
		createFilter = testData.get(testName+"_filterData").get(0);
		DataSource cases = testData.get(testName);
		sugar().cases.api.create(cases);
		sugar().login();

		sugar().cases.navToListView();
		// Create a Filter for status field on value DUPLICATE
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterCreateNew();
		sugar().cases.listView.filterCreate.setFilterFields("status",createFilter.get("filterField"),createFilter.get("operator"),createFilter.get("filterBy"),1);
		sugar().cases.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().cases.listView.filterCreate.save();
	}

	/**
	 * Saved Filter_Verify that saved filter can be selected at Cases list view.
	 * @throws Exception
	 */
	@Test
	public void Cases_23327_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Applying the default filter before selecting the saved custom filter
		sugar().cases.listView.openFilterDropdown();
		sugar().cases.listView.selectFilterAll();
		VoodooUtils.waitForReady();

		sugar().cases.listView.selectFilter(testName);
		// Asserting that after applying the filter, only those records are listed in the list view for which the Status is Duplicate
		int dupCasesAfterFilterApplied = sugar().cases.listView.countRows();
		for(int j = 1; j <= dupCasesAfterFilterApplied; j++) {
			sugar().cases.listView.verifyField(j, "status", createFilter.get("filterBy"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
