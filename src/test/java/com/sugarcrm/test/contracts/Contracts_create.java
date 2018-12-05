package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.test.SugarTest;

public class Contracts_create extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
		sugar.accounts.api.create();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	@Test
	public void Contracts_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ContractRecord myContract = (ContractRecord) sugar.contracts.create();

		myContract.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
