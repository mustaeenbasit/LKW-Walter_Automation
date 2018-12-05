package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_31219 extends SugarTest {
	DataSource accData = new DataSource();

	public void setup() throws Exception {
		accData = testData.get(testName);
		FieldSet filterData = testData.get(testName + "_filterData").get(0);
		sugar().accounts.api.create(accData);
		sugar().login();

		// Navigate to Accounts List View and create a custom Filter
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("name", filterData.get("name"), filterData.get("operator"), filterData.get("value"), 1);
		sugar().accounts.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();
	}

	/**
	 * Verify that Global Search should not display custom filters in searched results if a record is searched.
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_31219_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Global search textbox to expand
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.set(testName);
		VoodooUtils.waitForReady();

		// Verify in TypeAhead dropdown "No Results were found" is shown
		sugar().navbar.search.getControl("searchResults").assertEquals(accData.get(0).get("description"), true);

		// Verify No record is displayed in Global Search Bar Results page, that itself verifies Custom filter is not available
		// TODO: CB-252,VOOD-1437
		globalSearchCtrl.append("\uE007");
		sugar().globalSearch.getRow(1).assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}