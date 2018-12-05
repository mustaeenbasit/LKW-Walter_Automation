package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_22037 extends SugarTest {
	DataSource accountsData;
	AccountRecord firstAccount, secondAccount, thirdAccount;
	
	public void setup() throws Exception {
		accountsData = testData.get(testName);
		
		// TODO: VOOD-597 -Need lib support for date created fields
		// Once VOOD-597 will be resolved, we can create record with different date
		firstAccount = (AccountRecord) sugar().accounts.api.create(accountsData.get(0));
		secondAccount = (AccountRecord) sugar().accounts.api.create(accountsData.get(1));
		thirdAccount = (AccountRecord) sugar().accounts.api.create(accountsData.get(2));
		
		sugar().login();
	}

	/**
	 * Verify that ListViews are sorted by Date Created by default
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22037_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to listview and verify that ListView is sorted by “Date Created” descending by default
		// TODO: VOOD-597
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", thirdAccount.get("name"));
		sugar().accounts.listView.verifyField(2, "name", secondAccount.get("name"));
		sugar().accounts.listView.verifyField(3, "name", firstAccount.get("name"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}