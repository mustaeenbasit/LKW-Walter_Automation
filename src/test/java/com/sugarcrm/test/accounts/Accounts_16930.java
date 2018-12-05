package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_16930 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Verify that Account record can be copied
	 * @throws Exception
	 */
	@Test
	public void Accounts_16930_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		AccountRecord acc1 = (AccountRecord)myAccount.copy(testData.get(testName).get(0));
		acc1.verify(); 			

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}