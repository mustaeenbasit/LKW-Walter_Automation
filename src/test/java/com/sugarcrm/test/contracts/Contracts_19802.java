package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19802 extends SugarTest {
	DataSource ContractData;
	
	public void setup() throws Exception {
		ContractData = testData.get(testName);
		sugar.contracts.api.create(ContractData);
		
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Sort Contract_Verify that the sort order of "Contract" list can be reversed after clicking the highlighted column.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19802_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
			
		// Navigate to Contracts Listview and verify default order of records in listview
		sugar.contracts.navToListView();
		sugar.contracts.listView.verifyField(1, "name", ContractData.get(1).get("name"));
		sugar.contracts.listView.verifyField(2, "name", ContractData.get(0).get("name"));
		
		// Click on 'Name' column title
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl NameColumnCtrl = new VoodooControl("a", "css", "#MassUpdate tr:nth-child(2) th:nth-child(4) div a");
		NameColumnCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// Verify that sort order is reversed (Ascending order)
		sugar.contracts.listView.verifyField(1, "name", ContractData.get(0).get("name"));
		sugar.contracts.listView.verifyField(2, "name", ContractData.get(1).get("name"));
		
		// Again click on 'Name' column title
		VoodooUtils.focusFrame("bwc-frame");
		NameColumnCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		
		// Verify that again sort order is reversed (Descending order)
		sugar.contracts.listView.verifyField(1, "name", ContractData.get(1).get("name"));
		sugar.contracts.listView.verifyField(2, "name", ContractData.get(0).get("name"));
					
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}