package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_22876 extends SugarTest {
	
	AccountRecord acc1;
	AccountRecord acc2;
	DataSource ds;
		
	public void setup() throws Exception {
		sugar().login();
				
		// Create account record with standard dataset
		acc1 = (AccountRecord) sugar().accounts.api.create();
	}
	
	/**
	 * Create Account - Copy_Verify that a new account is  created by editing a duplicate record.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22876_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		ds = testData.get(testName);		
		
		// Duplicate account using custom fieldset
		acc2 = (AccountRecord) acc1.copy(ds.get(0));
		
		// Verify that account has been duplicated correctly
		acc2.verify();
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
