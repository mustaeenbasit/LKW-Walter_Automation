package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22987 extends SugarTest {
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		accountData = testData.get(testName);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Verify that search conditions can be cleared in Account searching
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22987_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts module
		sugar().accounts.navToListView();

		// In search box, type any strings that are actually matching one of account records
		sugar().accounts.listView.setSearchString(accountData.get(0).get("name"));
		VoodooUtils.waitForReady();

		// Verify that the record should populated as per the search string
		sugar().accounts.listView.verifyField(1, "name", accountData.get(0).get("name"));
		Assert.assertTrue("Searched record is not populated", sugar().accounts.listView.countRows() == 1);

		// Clear entering value by cross clicking
		sugar().accounts.listView.clearSearch();
		VoodooUtils.waitForReady();

		// Verify that the search conditions are cleared.The box is empty
		sugar().accounts.listView.getControl("searchFilter").assertContains("", true);
		Assert.assertTrue("search conditions are not cleared", sugar().accounts.listView.countRows() == accountData.size());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}