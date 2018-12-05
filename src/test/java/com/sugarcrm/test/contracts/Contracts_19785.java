package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19785 extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Create Contract_Verify that creating a contract can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19785_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		//  Go to contracts module.
		sugar.navbar.selectMenuItem(sugar.contracts, "createContract");
		VoodooUtils.focusFrame("bwc-frame");
		sugar.contracts.editView.getEditField("name").set(sugar.contracts.getDefaultData().get("name"));
		sugar.contracts.editView.getEditField("account_name").set(sugar.accounts.getDefaultData().get("name"));
		VoodooUtils.focusDefault();
		
		// Click "Cancel" button.
		sugar.contracts.editView.cancel();
		
		// Search the canceled contract record using contract search panel.
		sugar.contracts.listView.basicSearch(sugar.contracts.getDefaultData().get("name"));
		
		// Verify that No matching contract information is displayed in contract list view.
		Assert.assertTrue("There is no record in the list", sugar.contracts.listView.countRows() == 0);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}