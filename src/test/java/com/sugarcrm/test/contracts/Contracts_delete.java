package com.sugarcrm.test.contracts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.test.SugarTest;

public class Contracts_delete extends SugarTest {
	ContractRecord myContract;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myContract = (ContractRecord)sugar().contracts.api.create();
		sugar().login();

		// Enable Contracts module
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
	}

	@Test
	public void Contracts_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the contract using the UI.
		myContract.delete();

		// Verify the account was deleted.
		sugar().contracts.navToListView();
		assertEquals(VoodooUtils.contains(myContract.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}