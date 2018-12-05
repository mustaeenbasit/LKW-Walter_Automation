package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19799 extends SugarTest {
	DataSource contractsData;
	
	public void setup() throws Exception {
		sugar.login();

		contractsData = testData.get("Contracts_19799");
		sugar.contracts.api.create(contractsData);
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * 19799 Verify that the matching contract can be searched correctly when entering special characters
	 * @throws Exception
	 */
	@Test
	public void Contracts_19799_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.contracts.navToListView();
		
		String contractName;
		for(int i=0; i<contractsData.size(); i++){
			contractName = contractsData.get(i).get("name");
			sugar.contracts.listView.basicSearch(contractName);

			sugar.contracts.listView.verifyField(1, "name", contractName);
			VoodooUtils.focusDefault();
		}
		
		// Clear the search box and search again to return to the original state
		sugar.contracts.listView.basicSearch("");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}