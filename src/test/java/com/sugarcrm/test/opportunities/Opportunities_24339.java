package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContractRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24339 extends SugarTest {
	ContractRecord myContract;

	public void setup() throws Exception {
		// Opportunity records and contract records exist.
		sugar().opportunities.api.create();
		myContract = (ContractRecord) sugar().contracts.api.create();
		sugar().login();

		// Enable Contracts sub-panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
	}

	/**
	 * Verify that "Contract Search" function works successfully in the window which pops up after clicking "Select" button in "Contracts" sub-panel.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24339_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity recordView
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel contractsSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().contracts.moduleNamePlural);
		contractsSubPanel.scrollIntoViewIfNeeded(false);
		contractsSubPanel.getControl("expandSubpanelActions").click();
		VoodooUtils.waitForReady();

		// Click into the "Link existing Record" button in "Contracts" sub-panel.
		contractsSubPanel.getControl("linkExistingRecord").click();
		VoodooUtils.waitForReady();

		// Search with text
		sugar().contracts.searchSelect.search(myContract.getRecordIdentifier());

		// TODO: VOOD-1573
		// Verify that the contracts matching the search conditions are displayed in "Contract List" sub-panel of the popup window.
		new VoodooControl("tr", "css", ".layout_Contracts.drawer.active div[data-voodoo-type='view'] tbody tr:nth-of-type(1)").assertContains(myContract.getRecordIdentifier(), true);

		// Select searched record
		sugar().contracts.searchSelect.selectRecord(1);
		sugar().contracts.searchSelect.link();

		// Verify that the "Contract" sub-panel have one record of the opportunity recordView.
		Assert.assertTrue("Assert Contracts sub-panel count Rows equals 1 FAILED.", contractsSubPanel.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}