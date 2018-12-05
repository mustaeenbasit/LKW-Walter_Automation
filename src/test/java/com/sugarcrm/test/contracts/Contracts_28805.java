package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_28805 extends SugarTest {
	DataSource ContractData;
	
	public void setup() throws Exception {
		ContractData = testData.get(testName);
		sugar.contracts.api.create(ContractData);
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Verify that start date in advanced search of Contracts module is functional
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_28805_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.contracts.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Advance Search
		// Expand "Advance search" section.
		sugar.contracts.listView.getControl("advancedSearchLink").click();
		
		// TODO: VOOD-975
		VoodooControl startDateCtrl = new VoodooControl("input", "id", "range_start_date_advanced");
		VoodooControl searchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		
		for(int i = 0; i < ContractData.size(); i++) {
			startDateCtrl.set(ContractData.get(i).get("date_start"));
			searchCtrl.click();
			VoodooUtils.focusDefault();
			
			// Verify that Contracts records are searched out based on the specified "Start Date" criteria 
			sugar.contracts.listView.verifyField(1, "name", ContractData.get(i).get("name"));
			VoodooUtils.focusFrame("bwc-frame");
		}
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}