package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_21124 extends SugarTest {
	DataSource contractsData;
	
	public void setup() throws Exception {
		contractsData = testData.get(testName);
		sugar.contracts.api.create(contractsData);
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Click Cancel button after do pagination action on edit view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_21124_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.contracts.navToListView();
		sugar.contracts.listView.editRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-779 -Need defined control for pagination buttons on BWC record's edit view
		new VoodooControl("button", "css", "span.pagination button[title='Next']").click();
		sugar.alerts.confirmAllWarning();

		new VoodooControl("button", "css", "span.pagination button[title='Previous']").click();
		sugar.alerts.confirmAllWarning();
		VoodooUtils.focusDefault();
	
		// Click "Cancel" button.
		sugar.contracts.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that user navigate to contracts List view.
		sugar.contracts.listView.getControl("selectAllCheckbox").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}