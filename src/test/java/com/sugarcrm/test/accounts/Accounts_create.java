package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Accounts_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		AccountRecord myAccount = (AccountRecord)sugar().accounts.create();

		myAccount.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}