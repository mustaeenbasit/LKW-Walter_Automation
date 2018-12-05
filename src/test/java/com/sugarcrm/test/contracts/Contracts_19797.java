package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19797 extends SugarTest {
	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Edit Contract_Verify that the editing contract can be canceled
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19797_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Go to Contracts detail view
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		
		// Click "Edit" button
		sugar.contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Edit "Contract Name"	
		sugar.contracts.editView.getEditField("name").set(testName);
		VoodooUtils.focusDefault();
		
		// Click "Cancel" button
		sugar.contracts.editView.cancel();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that canceled editing contract is not displayed on the detail view page
		sugar.contracts.detailView.getDetailField("name").assertEquals(testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}