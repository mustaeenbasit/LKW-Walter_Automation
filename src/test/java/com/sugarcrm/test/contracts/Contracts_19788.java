package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.test.SugarTest;


public class Contracts_19788 extends SugarTest {
	DataSource ds;
	ContractRecord testContract;
	
	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
		ds = testData.get(testName);
		sugar.accounts.api.create();
		// TODO VOOD-444 - Support creating relationships via API
		testContract = (ContractRecord)sugar.contracts.create(ds.get(0));		
	}

	/**
	 * Test Case 19788: Create Contract - Duplicate_Verify that the selected contract can be duplicated when entering special characters
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19788_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		sugar.contracts.detailView.copy();		
		sugar.contracts.editView.save();		
		
		sugar.contracts.navToListView();
		sugar.contracts.listView.verifyField(1, "name", ds.get(0).get("name"));
		sugar.contracts.listView.verifyField(2, "name", ds.get(0).get("name"));
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}