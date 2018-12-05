package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;


public class Accounts_22878 extends SugarTest {
		
	AccountRecord acc1;
	
	public void setup() throws Exception {
		sugar().login();

		// Create new account record with default data
		acc1 = (AccountRecord) sugar().accounts.api.create();
	}
	
	/**
	 * Test Case 22878: Verify that account information is displayed according to the modification after editing
	 * @throws Exception
	 */
	@Test
	public void Accounts_22878_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		
		// Open created account record view and edit it using custom data
		acc1.edit(ds.get(0));
		
		// Verify that account has been edited correctly  
		acc1.verify();		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
