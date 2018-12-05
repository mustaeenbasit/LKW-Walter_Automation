package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19775 extends SugarTest {
	FieldSet fs;
	
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);		
		sugar.accounts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
	}

	/**
	 * Create Contract_Verify that a contract can be created when filling special characters in all fields, such as "' " ; # < > ! = ? \ ( )".
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19775_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		//  Go to contracts module.
		// Create Contract module using special characters in all fields, such as ;#<>!=?\()
		sugar.navbar.selectMenuItem(sugar.contracts, "createContract");
		VoodooUtils.focusFrame("bwc-frame");
		sugar.contracts.editView.getEditField("name").set(fs.get("name"));
		sugar.contracts.editView.getEditField("reference_code").set(fs.get("name"));
		sugar.contracts.editView.getEditField("account_name").set(sugar.accounts.getDefaultData().get("name"));
		sugar.contracts.editView.getEditField("description").set(fs.get("name"));
		VoodooUtils.focusDefault();
		
		// Click "save" button.
		sugar.contracts.editView.save();
		
		VoodooUtils.focusFrame("bwc-frame");
		// Verify that the newly created record is displayed correctly in "Contract" detail view.
		sugar.contracts.detailView.getDetailField("name").assertEquals(fs.get("name"), true);
		sugar.contracts.detailView.getDetailField("reference_code").assertEquals(fs.get("name"), true);
		sugar.contracts.detailView.getDetailField("description").assertEquals(fs.get("name"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}