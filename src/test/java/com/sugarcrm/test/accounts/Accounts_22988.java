package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22988 extends SugarTest {
	DataSource accountData = new DataSource();

	public void setup() throws Exception {
		accountData = testData.get(testName);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Search Account_Verify that condition can be still displayed after searching with accounts name 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22988_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts module
		sugar().accounts.navToListView();

		// Type accounts name into search field 
		sugar().accounts.listView.setSearchString(accountData.get(0).get("name"));
		VoodooUtils.waitForReady();

		// Verify that the search condition is still displayed in accounts search field
		sugar().accounts.listView.getControl("searchFilter").assertContains(accountData.get(0).get("name"), true);

		// Verify that matched Account name is displayed in accounts list view
		sugar().accounts.listView.verifyField(1, "name", accountData.get(0).get("name"));
		Assert.assertTrue("Searched record is not populated", sugar().accounts.listView.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}