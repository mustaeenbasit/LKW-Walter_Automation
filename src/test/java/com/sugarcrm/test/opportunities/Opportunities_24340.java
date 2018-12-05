package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24340 extends SugarTest {
	StandardSubpanel contractsSubPanel;

	public void setup() throws Exception {
		// Opportunity record(s) exist.
		OpportunityRecord myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		ContractRecord myContract = (ContractRecord) sugar().contracts.api.create();
		sugar().login();

		// Enable Contracts sub-panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);

		// Link contract record with a opportunity record
		myOpp.navToRecord();
		contractsSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		contractsSubPanel.scrollIntoViewIfNeeded(false);
		contractsSubPanel.linkExistingRecord(myContract);
	}

	/**
	 * Remove Contract_Verify that contract can be removed from opportunity detail view when using "rem" function.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24340_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that the "Contract" sub-panel have one record of the opportunity recordView.
		Assert.assertTrue("Assert Contracts sub-panel count Rows equals 1 FAILED.", contractsSubPanel.countRows() == 1);

		// Click "remove" button of a contract record in "Contracts" module.
		contractsSubPanel.unlinkRecord(1);

		// Verify that the contract disappears in "Contract" sub-panel of the opportunity.
		Assert.assertTrue("Assert Contracts sub-panel count Rows equals 0 FAILED.", contractsSubPanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}