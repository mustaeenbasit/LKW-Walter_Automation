package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22886 extends SugarTest {
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		accountData = testData.get(testName);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Search Account_Verify that search condition is cleared from created filter in search subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_22886_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Accounts" module.
		sugar().accounts.navToListView();

		// Create filter with any valid conditions in search subpanel of accounts listview
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("name", "Name", "exactly matches", sugar().accounts.getDefaultData().get("name"), 1);
		sugar().accounts.listView.assertIsEmpty();

		// Click "Cancel" button to cancel the applied filter
		sugar().accounts.listView.filterCreate.cancel();

		// Verify that all accounts are shown in listview
		sugar().accounts.listView.verifyField(1, "name", accountData.get(2).get("name"));
		sugar().accounts.listView.verifyField(2, "name", accountData.get(1).get("name"));
		sugar().accounts.listView.verifyField(3, "name", accountData.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}