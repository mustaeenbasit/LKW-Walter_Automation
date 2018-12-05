package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17696 extends SugarTest {
	VoodooControl currentFilter;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user can edit an user defined filter 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17696_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		FieldSet filterData = ds.get(0);
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();

		// Set filter Fields
		sugar().accounts.listView.filterCreate.setFilterFields("name", filterData.get("columnDisplayName"), filterData.get("operator"), filterData.get("value"), 1);
		sugar().accounts.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();

		// Select above created Filter
		currentFilter = sugar().accounts.listView.getControl("searchFilterCurrent");
		currentFilter.click();
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.setFilterFields("name", filterData.get("columnDisplayName"), ds.get(1).get("operator"), filterData.get("value"), 1);
		sugar().accounts.listView.filterCreate.save();

		// Verify changes are saved in the filter
		// TODO: VOOD-1478
		currentFilter.click();
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css", ".fld_filter_row_operator").assertEquals(ds.get(1).get("operator"), true);
		sugar().accounts.listView.filterCreate.cancel();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}