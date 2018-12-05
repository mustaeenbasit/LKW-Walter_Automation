package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22958 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
	}

	/**
	 * Account Detail - Contracts sub-panel - Create_Verify that a new contract is created by in-line creating 
	 * from "CONTRACTS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22958_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Click "Create" on "Contracts" sub-panel
		StandardSubpanel contractSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		contractSubpanel.scrollIntoViewIfNeeded(false);
		contractSubpanel.addRecord();
		
		// Fill all the fields
		FieldSet defaultContractData = sugar().contracts.getDefaultData();
		sugar().contracts.editView.setFields(defaultContractData);
		
		// Click "Save" button
		sugar().contracts.editView.save();
		
		// Verify related contract is displayed on "CONTRACTS" sub-panel
		contractSubpanel.scrollIntoViewIfNeeded(false);
		contractSubpanel.expandSubpanel();
		contractSubpanel.assertContains(sugar().contracts.getDefaultData().get("name"), true);
		Assert.assertEquals("Row count is not equal to 1 in subpanel", 1, contractSubpanel.countRows());
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}