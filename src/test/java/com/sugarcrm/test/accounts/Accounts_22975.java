package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22975 extends SugarTest {
	DataSource ds = new DataSource();
	DataSource filterRecordSet = new DataSource();

	public void setup() throws Exception {
		filterRecordSet = testData.get(testName);
		ds = testData.get(testName + "_record");
		sugar().accounts.api.create(ds);
		sugar().login();

		// Create filter in Account list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		// create a filter
		sugar().accounts.listView.filterCreate.setFilterFields("billingAddressCity", filterRecordSet.get(0).get("display_name"), filterRecordSet.get(0).get("operator"), filterRecordSet.get(0).get("value"), 1);
		sugar().accounts.listView.filterCreate.getControl("filterName").set(filterRecordSet.get(0).get("filter_name"));
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();

		// verify that four records having same city i.e. "Cupertino" according to applied filter
		for (int i = 0; i < ds.size(); i++) {
			sugar().accounts.listView.assertContains(filterRecordSet.get(0).get("value"), true);
		}
	}

	/**
	 * Filter Account_Verify that the filter conditions can be updated for a saved filter.
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_22975_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Update filter condition for already created filter
		VoodooControl currentFilter = sugar().accounts.listView.getControl("searchFilterCurrent");
		currentFilter.click();
		sugar().accounts.listView.filterCreate.setFilterFields("name", filterRecordSet.get(1).get("display_name"), filterRecordSet.get(1).get("operator"), ds.get(1).get("name"), 1);
		VoodooUtils.waitForReady();

		// Verify that one record displayed according to filter
		sugar().accounts.listView.verifyField(1, "name", ds.get(1).get("name"));

		// Remove created filter
		sugar().accounts.listView.filterCreate.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that all four records are display
		for (int i = 0; i < ds.size(); i++) {
			sugar().accounts.listView.assertContains(ds.get(i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}