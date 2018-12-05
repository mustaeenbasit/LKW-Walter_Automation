package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22974 extends SugarTest {
	DataSource customDS;

	public void setup() throws Exception {
		customDS = testData.get(testName+"_record");
		sugar().accounts.api.create(customDS);
		sugar().login();

		// Create filter in Account list view for several accounts
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("name", "Name", "starts with", customDS.get(0).get("name").substring(0, 4), 1);
		sugar().accounts.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();
	}

	/**
	 * Verify that the saved filter is deleted from Filter drop down list
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22974_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify, the results are shown as per the saved "Filter".
		sugar().accounts.listView.verifyField(1, "name", customDS.get(1).get("name"));
		sugar().accounts.listView.verifyField(2, "name", customDS.get(0).get("name"));

		// TODO: VOOD-1580
		new VoodooControl("span", "css", "[data-voodoo-name='Accounts'] .choice-filter-label").click();
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.delete();
		sugar().accounts.listView.confirmDelete();

		// Verify that all results are displayed after deleted saved "Filter".
		for(int i = customDS.size(), j = 0; i > 0; i--, j++)
			sugar().accounts.listView.verifyField(i, "name", customDS.get(j).get("name"));

		// TODO: VOOD-1580
		// Verify that the saved filter is deleted from "Filter" drop down list.
		sugar().accounts.listView.openFilterDropdown();
		new VoodooControl("li", "css", ".search-filter-dropdown .select2-results li:nth-child(2)").assertEquals(testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}