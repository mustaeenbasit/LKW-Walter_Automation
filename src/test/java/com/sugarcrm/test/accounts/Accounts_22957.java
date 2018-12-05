package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22957 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contracts.api.create();
		sugar().login();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
	}

	/**
	 * Account Detail - Contracts sub-panel - Create_Verify that creating new contract related to the account is 
	 * canceled in full form on "CONTRACTS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22957_execute() throws Exception {
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
		
		// Click "Cancel" button
		sugar().contracts.editView.cancel();
		
		// Verify no matching contract record is displayed on "Contracts" sub-panel
		contractSubpanel.scrollIntoViewIfNeeded(false);
		contractSubpanel.expandSubpanel();
		contractSubpanel.assertContains(sugar().contracts.getDefaultData().get("name"), false);
		Assert.assertEquals("Row count is not equal to 0 in subpanel", 0, contractSubpanel.countRows());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}