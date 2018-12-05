package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22884 extends SugarTest {
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		accountData = testData.get(testName);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Search Account_Verify that corresponding account records are displayed in account list view with advanced search condition.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22884_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Accounts" module. 
		sugar().accounts.navToListView();

		// Make filters for all available fields for Account module for one existed Account record
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();

		// set "exactly matches" filter for 'Name' field
		sugar().accounts.listView.filterCreate.setFilterFields("name", "Name", "exactly matches", accountData.get(1).get("name"), 1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify that corresponding account record are displayed on account list view.
		sugar().accounts.listView.verifyField(1, "name", accountData.get(1).get("name"));

		// "starts with" filter for 'City' field
		sugar().accounts.listView.filterCreate.setFilterFields("billingAddressCity", "City", "starts with", accountData.get(0).get("billingAddressCity").substring(0,3), 1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify that corresponding account record are displayed on account list view.
		sugar().accounts.listView.verifyField(1, "billingAddressCity", accountData.get(0).get("billingAddressCity"));
		sugar().accounts.listView.verifyField(2, "billingAddressCity", accountData.get(0).get("billingAddressCity"));

		// "exactly match" filter for 'City' field
		sugar().accounts.listView.filterCreate.setFilterFields("billingAddressCity", "City", "exactly matches", accountData.get(1).get("billingAddressCity"), 1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify that corresponding account record are displayed on account list view.
		sugar().accounts.listView.verifyField(1, "billingAddressCity", accountData.get(1).get("billingAddressCity"));

		// "starts with" filter for 'Office Phone' field
		sugar().accounts.listView.filterCreate.setFilterFields("workPhone", "Office Phone", "starts with", accountData.get(0).get("workPhone").substring(0,3), 1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify that corresponding account record are displayed on account list view.
		sugar().accounts.listView.verifyField(1, "workPhone", accountData.get(0).get("workPhone"));
		sugar().accounts.listView.verifyField(2, "workPhone", accountData.get(0).get("workPhone"));

		// "exactly match" filter for 'Country' field
		sugar().accounts.listView.filterCreate.setFilterFields("billingAddressCountry", "Country", "exactly matches", accountData.get(0).get("billingAddressCountry"), 1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify that corresponding account record are displayed on account list view.
		sugar().accounts.listView.verifyField(1, "billingAddressCountry", accountData.get(0).get("billingAddressCountry"));
		sugar().accounts.listView.verifyField(2, "billingAddressCountry", accountData.get(0).get("billingAddressCountry"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}