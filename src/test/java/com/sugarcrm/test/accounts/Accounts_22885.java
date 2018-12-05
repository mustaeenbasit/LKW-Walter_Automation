package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22885 extends SugarTest {
	DataSource accountsData = new DataSource();

	public void setup() throws Exception {
		accountsData = testData.get(testName); 
		sugar().accounts.api.create(accountsData);
		sugar().login();
	}

	/**
	 * Search Account_Verify that search condition is cleared from field of search subpanel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22885_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Accounts" module listview
		sugar().accounts.navToListView();

		//  Enter some data (Account Name) to search subpanel. 
		sugar().accounts.listView.setSearchString(accountsData.get(0).get("name"));

		// Verify that accounts is filtered successfully according to entered values
		sugar().accounts.listView.verifyField(1, "name", accountsData.get(0).get("name"));

		// clear the search 	
		sugar().accounts.listView.clearSearch();

		// Verify that all accounts are shown in listview
		sugar().accounts.listView.verifyField(1, "name", accountsData.get(2).get("name"));
		sugar().accounts.listView.verifyField(2, "name", accountsData.get(1).get("name"));
		sugar().accounts.listView.verifyField(3, "name", accountsData.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}