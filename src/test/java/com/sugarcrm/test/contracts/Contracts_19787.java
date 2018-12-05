package com.sugarcrm.test.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.test.SugarTest;

public class Contracts_19787 extends SugarTest {
	ContractRecord myContractRecord;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);

		FieldSet fs = new FieldSet();
		fs.put("account_name", sugar.accounts.getDefaultData().get("name"));

		//  Create a 'contract' and filling only mandatory fields.
		// TODO VOOD-444 - Support creating relationships via API
		myContractRecord = (ContractRecord)sugar.contracts.create(fs);
	}

	/**
	 * Create Contract - Duplicate_Verify that The selected contract can be duplicated when filling only mandatory fields.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19787_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		myContractRecord.navToRecord();

		// Click "Copy" button in "Contract" detail view.
		sugar.contracts.detailView.copy();

		//  Click on "save" button
		sugar.contracts.editView.save();

		// navigate back to contracts listview
		sugar.contracts.navToListView();
		int row = sugar.contracts.listView.countRows();

		// Verify that The newly duplicated record is displayed correctly in "contract" list view.
		Assert.assertTrue("Number of records in listview equals to 2.", row == 2);
		VoodooUtils.focusDefault();
		sugar.contracts.listView.verifyField(1, "name", sugar.contracts.getDefaultData().get("name"));
		sugar.contracts.listView.verifyField(2, "name", sugar.contracts.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
