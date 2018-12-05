package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19790 extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Verify that the selected contract cannot be duplicated when there are mandatory fields empty.
	 * @throws Exception
	 */
	@Test
	public void Contracts_19790_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		//  Go to contracts module.
		sugar.navbar.navToModule(sugar.contracts.moduleNamePlural);

		// Click a link of contract name in "Contract" list view.
		sugar.contracts.listView.clickRecord(1);

		// Click "Copy"  button in "Contract" detail view.
		sugar.contracts.detailView.copy();

		// Click "Save" button in "Contract" duplicate Copy view.
		sugar.contracts.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that error message "Missing required field name" in "Account Name" field.
		// TODO: VOOD-1588 - Need lib support for asserting required input fields error messages in BWC modules
		new VoodooControl("div", "css", ".required.validation-message").assertContains(testData.get(testName).get(0).get("error_msg"), true);
		VoodooUtils.focusDefault();
		sugar.contracts.editView.cancel();

		// Navigate back to listview
		sugar.contracts.navToListView();

		// Verify no new record is created. 
		Assert.assertTrue("No. of Records not equal to 1 in contracts list view", sugar.contracts.listView.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}