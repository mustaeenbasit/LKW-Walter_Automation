package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19791 extends SugarTest {
	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Create Contract - Duplicate_Verify that the duplicating a contract can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19791_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Click "contracts" link on top navigation bar.
		sugar.navbar.navToModule(sugar.contracts.moduleNamePlural);
		
		//  Click a link of contract name in "Contract" list view.
		sugar.contracts.listView.clickRecord(1);

		//  Click "Copy" button in "Contract" detail view.
		sugar.contracts.detailView.copy();

		VoodooUtils.focusFrame("bwc-frame");
		// Edit the information of the contract i.e. "Contract Name"
		sugar.contracts.editView.getEditField("name").set(testName);
		VoodooUtils.focusDefault();
		
		// Click "Cancel" button in "Contract" edit view.
		sugar.contracts.editView.cancel();

		sugar.contracts.navToListView();
		
		// Search for the canceled duplicating record.
		sugar.contracts.listView.basicSearch(testName);

		// Verify that the canceled duplicating record is not displayed in "Contract" list view.
		Assert.assertTrue("No contract record named" + testName + "displayed", sugar.contracts.listView.countRows() == 0);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}