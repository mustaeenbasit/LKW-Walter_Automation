package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class StandardModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void createTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running createTest()...");

		AccountRecord myAccount = (AccountRecord)sugar().accounts.create();
		myAccount.verify();
		assertFalse(myAccount.getGuid().isEmpty());

		VoodooUtils.voodoo.log.info("createTest() test complete.");
	}

	public void cleanup() throws Exception {}
}