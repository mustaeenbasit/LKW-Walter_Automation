package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17036 extends SugarTest {
	DataSource ds;
	AccountRecord accountToCheck;	
		
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		sugar().accounts.create(ds.get(0));
		accountToCheck = (AccountRecord)sugar().accounts.create(ds.get(1));
	}

	/**
	 * Test Case 17036: Verify no duplicate is shown if billing city or shipping city not match while creating a new account
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17036_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Verify that duplicate found panel is not shown
		new VoodooControl("span", "css", ".duplicate_count").assertExists(false);
		
		// Verify that new account was created successfully		
		accountToCheck.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}