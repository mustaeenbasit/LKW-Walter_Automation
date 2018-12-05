package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19803 extends SugarTest {
	DataSource ContractData;
	
	public void setup() throws Exception {
		ContractData = testData.get(testName);
		sugar.contracts.api.create(ContractData);
		
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Sort Contract_Verify that Contract can be sorted correctly with special characters, including Chinese 
	 * character and other language characters, such as "' " ; # < > ! = ? \ ( ) </td></tr>"
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19803_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
			
		// Navigate to Contracts Listview and verify default order of records in listview
		sugar.contracts.navToListView();
		
		// Click on 'Name' column title
		// TODO: VOOD-1534 -Need lib support for sorting in BWC module
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl NameColumnCtrl = new VoodooControl("a", "css", "#MassUpdate tr:nth-child(2) th:nth-child(4) div a");
		NameColumnCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// Verify that Contracts can be sorted correctly by selected column
		for (int i = 0; i < ContractData.size(); i++) {
			sugar.contracts.listView.verifyField((i+1), "name", ContractData.get(i).get("name"));
		}
		
		// Click again on 'Name' column title
		// TODO: VOOD-1534 -Need lib support for sorting in BWC module
		VoodooUtils.focusFrame("bwc-frame");
		NameColumnCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// Verify that Contracts can be sorted correctly by selected column
		for (int i = 0; i < ContractData.size(); i++) {
			sugar.contracts.listView.verifyField((8-i), "name", ContractData.get(i).get("name"));
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}