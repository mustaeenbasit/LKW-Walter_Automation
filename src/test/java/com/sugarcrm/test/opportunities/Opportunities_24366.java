package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24366 extends SugarTest {
	OpportunityRecord myOpp;
	AccountRecord myAcct;
	StandardSubpanel contractsSubpanel;
	DataSource contractDS;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myAcct = (AccountRecord) sugar().accounts.api.create();
		contractDS = testData.get(testName);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
	}

	/**
	 * Test Case 24366: In-Line Create Contracts_Verify that contract can be in-line created from Contracts sub-panel for an opportunity
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24366_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		contractsSubpanel = sugar().opportunities.recordView.subpanels.get("Contracts");
		// Open opportunity record view and inline-create a related contract
		myOpp.navToRecord();
		contractsSubpanel.addRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("name").waitForVisible();
		VoodooUtils.focusDefault();
		sugar().contracts.editView.setFields(contractDS.get(0));
		sugar().contracts.editView.save();
		// Verify the contract is successfully created and visible in contracts subpanel of the opportunity
		//myOpp.navToRecord();
		contractsSubpanel.expandSubpanel();
		new VoodooControl("a", "css", ".fld_name.list div").assertContains((contractDS.get(0).get("name")), true);
		new VoodooControl("a", "css", ".fld_account_name.list div").assertContains(myAcct.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}