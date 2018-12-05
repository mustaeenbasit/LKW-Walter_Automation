package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.sugar.records.AccountRecord;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24376 extends SugarTest {
	OpportunityRecord myOpp;
	ContractRecord myContract;
	AccountRecord myAccount;
	StandardSubpanel contractsSubpanel;

	public void setup() throws Exception {
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myContract = (ContractRecord) sugar().contracts.api.create();
		myAccount = (AccountRecord) sugar().accounts.api.create();

		sugar().login();

		sugar().navbar.navToAdminTools();
		// Enable Contracts Subpanel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
		// Link contract with opportunity
		contractsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		myOpp.navToRecord();
		contractsSubpanel.scrollIntoView();
		contractsSubpanel.linkExistingRecord(myContract);

		// Add account_name field value for contract record
		contractsSubpanel.scrollIntoView();
		contractsSubpanel.editRecord(1);
		// TODO: VOOD-503
		new VoodooSelect("a", "css", ".fld_account_name.edit a").set(myAccount.getRecordIdentifier());
		contractsSubpanel.saveAction(1);
	}

	/**
	 * Test Case 24376: Edit Contract_Verify that no information of the contract record is changed when clicking save
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24376_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open opportunity record 
		myOpp.navToRecord();

		// Click on "Edit" of a contract record in "Contracts" sub-panel
		contractsSubpanel.scrollIntoView();
		contractsSubpanel.editRecord(1);
		// Click on "Save"
		contractsSubpanel.saveAction(1);

		// Verify the contract is not changed
		new VoodooControl("a", "css", ".fld_name.list div").assertContains(myContract.get("name"), true);
		new VoodooControl("a", "css", ".fld_account_name.list div").assertContains(myAccount.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}