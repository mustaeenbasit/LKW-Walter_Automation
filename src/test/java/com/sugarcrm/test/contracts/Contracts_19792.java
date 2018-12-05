package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19792 extends SugarTest {
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Edit Contract_Verify that the selected contract can be edited when editing only mandatory fields, 
	 * such as "Contract Name" and "Team" text field.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19792_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		//  Go to contracts module.
		sugar.navbar.navToModule(sugar.contracts.moduleNamePlural);

		sugar.contracts.listView.clickRecord(1);
		//  Click on Edit button in the list view of any existing Contract.
		sugar.contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		// Edit Contract Name, Account name and Teams.
		sugar.contracts.editView.getEditField("name").set(testName);
		sugar.contracts.editView.getEditField("account_name").set(sugar.accounts.getDefaultData().get("name"));
		sugar.contracts.editView.getEditField("teams").set(sugar.users.getQAUser().get("userName"));
		VoodooUtils.focusDefault();

		// Click "Save" button.
		sugar.contracts.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		// Verify the Contract name, Accounts name, and Teams field.
		sugar.contracts.detailView.getDetailField("name").assertEquals(testName, true);
		sugar.contracts.detailView.getDetailField("account_name").assertEquals(sugar.accounts.getDefaultData().get("name"), true);
		sugar.contracts.detailView.getDetailField("teams").assertContains(sugar.users.getQAUser().get("userName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}