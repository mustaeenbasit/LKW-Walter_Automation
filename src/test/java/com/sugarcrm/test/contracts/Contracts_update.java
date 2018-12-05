package com.sugarcrm.test.contracts;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.ContractRecord;

public class Contracts_update extends SugarTest {
	ContractRecord myContract;
	
	public void setup() throws Exception {
		sugar.login();
		sugar.accounts.api.create();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
		
		//TODO This should work when vood-645 is fixed. Problem entering account related field.
		myContract = (ContractRecord)sugar.contracts.create();
	}

	@Test
	public void Contracts_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "International Business Machines, Inc.");
		newData.put("status", "In Progress");

		// Edit the contract using the UI.
		myContract.edit(newData);
		
		// Verify the contract was edited.
		myContract.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}