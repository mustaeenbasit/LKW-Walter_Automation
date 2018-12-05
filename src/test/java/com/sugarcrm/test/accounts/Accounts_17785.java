package com.sugarcrm.test.accounts;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Accounts_17785 extends SugarTest {
	DataSource ds = new DataSource();
	List<Record> accountRecords = new ArrayList<Record>();

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		accountRecords = sugar().accounts.api.create(ds);
		sugar().accounts.navToListView();

		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("billingAddressCity", "City", "exactly matches", ds.get(0).get("billingAddressCity"), 1);
		sugar().accounts.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().accounts.listView.filterCreate.save();

		// Change to another filter such as "All Accounts" from the "Filter" drop down
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterAll();
	}

	/**
	 * Verify that clicking on a custom filter doesn't reveal the menu
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_17785_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// From the "Filter" drop down click back to custom filter
		sugar().accounts.listView.selectFilter(testName);
		// Verify that the custom filter displays the proper result set.
		sugar().accounts.listView.verifyField(1, "name", accountRecords.get(0).getRecordIdentifier());
		// Verify that it does not display the filter menu (fields that are displayed when you create the new filter).
		sugar().accounts.listView.filterCreate.getControl("filterName").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
