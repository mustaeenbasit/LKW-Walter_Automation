package com.sugarcrm.test.accounts;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_28984 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Blank filter name should not be created for All sidecar Modules (Module = Accounts)
	 * @throws Exception
	 */
	@Test
	public void Accounts_28984_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet filterData = testData.get(testName).get(0);

		// Create a filter with blank filter name
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		
		// Set filter Fields
		sugar().accounts.listView.filterCreate.setFilterFields("name", filterData.get("columnDisplayName"), 
				filterData.get("operator"), filterData.get("value"), 1);

		// In filter name, Enter a string that only contain spaces
		sugar().accounts.listView.filterCreate.getControl("filterName").set(filterData.get("blankFilterName"));

		// Assert that Save button is disabled i.e not able to create a filter with blank filter name
		assertTrue(sugar().accounts.listView.filterCreate.getControl("saveButton").isDisabled());

		// Create filter with filter name having leading and trailing spaces
		sugar().accounts.listView.filterCreate.getControl("filterName").
		set(filterData.get("filterNameLeadingNtrailingSpaces"));
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();

		// TODO: VOOD-1580 - Need lib support to edit custom filter
		// Assert that the leading and trailing spaces in the filter name are trimmed.
		VoodooControl filterLabel = new VoodooControl("span", "css", ".layout_Accounts .table-cell:nth-child(2) .choice-filter-label");
		filterLabel.assertEquals(filterData.get("filterNameLeadingNtrailingSpaces").trim(), true);

		// Re-Open the Filter to check the filter name in edit view
		filterLabel.click();

		// Assert that the leading and trailing spaces are trimmed from the filter name on the edit view as well
		sugar().accounts.listView.filterCreate.getControl("filterName").
		assertEquals(filterData.get("filterNameLeadingNtrailingSpaces").trim(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}