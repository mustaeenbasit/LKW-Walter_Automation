package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_28164 extends SugarTest {
	AccountRecord myAccount;
	FieldSet filterData = new FieldSet();
	FieldSet accData = new FieldSet();

	public void setup() throws Exception {
		filterData = testData.get(testName).get(0);
		accData = new FieldSet();
		for (int i = 1; i <= 3; i++) {
			if (i == 3) {
				accData.put("name", "Account" + i);
				accData.put("workPhone", filterData.get("workPhone"));
			} else
				accData.put("name", "testAccount" + i);

			myAccount = (AccountRecord) sugar().accounts.api.create(accData);
		}
		sugar().login();
	}

	/**
	 * Verify that main filter should work properly when sub filter is being created/updated
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_28164_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts 
		sugar().accounts.navToListView();

		// Enter first letter of already created account into the main search filter
		sugar().accounts.listView.getControl("searchFilter").set(myAccount.getRecordIdentifier().substring(0, 1));
		// Verify all accounts starting with A and with an office phone starting with "7" are displayed
		sugar().accounts.listView.verifyField(1, "name", myAccount.getRecordIdentifier());

		sugar().accounts.listView.openFilterDropdown();
		// Click "create" under Filters drop down
		sugar().accounts.listView.selectFilterCreateNew();

		// set "Start with" operator
		sugar().accounts.listView.filterCreate.setFilterFields("workPhone", filterData.get("filterName"), filterData.get("operator1"), filterData.get("value"), 1);
		VoodooUtils.waitForReady();

		// Verify all accounts starting with A and with an office phone starting with "7" are displayed
		sugar().accounts.listView.verifyField(1, "name", myAccount.getRecordIdentifier());

		// Change "Start with" to "is"
		sugar().accounts.listView.filterCreate.setFilterFields("workPhone", filterData.get("filterName"), filterData.get("operator2"), filterData.get("value"), 1);
		VoodooUtils.waitForReady();
		// Verify No results are displayed
		sugar().accounts.listView.assertIsEmpty();

		// Again enter first letter of already created account into the main search filter
		sugar().accounts.listView.getControl("searchFilter").set(myAccount.getRecordIdentifier().substring(0, 1));
		sugar().accounts.listView.openFilterDropdown();
		// Click "create" under Filters drop down
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("workPhone", filterData.get("filterName"), filterData.get("operator1"), filterData.get("value"), 1);
		VoodooUtils.waitForReady();

		// Click on "-"
		sugar().accounts.listView.filterCreate.getControl("removeFilterRow01").click();
		VoodooUtils.waitForReady();

		// Verify All accounts starting with A are displayed in the list
		sugar().accounts.listView.verifyField(1, "name", myAccount.getRecordIdentifier());

		// Again enter first letter of already created account into the main search filter
		sugar().accounts.listView.getControl("searchFilter").set(myAccount.getRecordIdentifier().substring(0, 1));

		sugar().accounts.listView.openFilterDropdown();
		// Click "create" under Filters drop down
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("workPhone", filterData.get("filterName"), filterData.get("operator1"), filterData.get("value"), 1);
		VoodooUtils.waitForReady();

		// click on reset button
		sugar().accounts.listView.filterCreate.getControl("resetButton").click();
		VoodooUtils.waitForReady();

		// Verify All accounts starting with A are displayed in the list
		sugar().accounts.listView.verifyField(1, "name", myAccount.getRecordIdentifier());

		// Again enter first letter of already created account into the main search filter
		sugar().accounts.listView.getControl("searchFilter").set(myAccount.getRecordIdentifier().substring(0, 1));

		sugar().accounts.listView.openFilterDropdown();
		// Click "create" under Filters drop down
		sugar().accounts.listView.selectFilterCreateNew();
		sugar().accounts.listView.filterCreate.setFilterFields("workPhone", filterData.get("filterName"), filterData.get("operator1"), filterData.get("value"), 1);
		VoodooUtils.waitForReady();

		// click on cancel button
		sugar().accounts.listView.filterCreate.cancel();
		VoodooUtils.waitForReady();

		// Verify All accounts starting with A are displayed in the list
		sugar().accounts.listView.verifyField(1, "name", myAccount.getRecordIdentifier());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}