package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22883 extends SugarTest {
	DataSource accountsData = new DataSource();

	public void setup() throws Exception {
		accountsData = testData.get(testName);
		sugar().accounts.api.create(accountsData);
		sugar().login();
	}

	/**
	 * Search Account_Verify that corresponding account records are displayed in account list view by basic search condition.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22883_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Accounts" module. 
		sugar().accounts.navToListView();

		// Go to listview and use search field 
		sugar().accounts.listView.setSearchString(accountsData.get(0).get("name"));

		// Verify that corresponding account record are displayed on account list view.
		sugar().accounts.listView.verifyField(1, "name", accountsData.get(0).get("name"));

		// clear the search field
		sugar().accounts.listView.clearSearch();

		// Verify that all account record are displayed on account list view again.
		sugar().accounts.listView.verifyField(1, "name", accountsData.get(2).get("name"));
		sugar().accounts.listView.verifyField(2, "name", accountsData.get(1).get("name"));
		sugar().accounts.listView.verifyField(3, "name", accountsData.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}