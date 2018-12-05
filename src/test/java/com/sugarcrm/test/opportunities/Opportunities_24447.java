package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24447 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel contractsSubpanel;
	FieldSet defContractData;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		defContractData = sugar().contracts.getDefaultData();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().contracts);
	}

	/**
	 * Test Case 24447: In-Line Create Contract_Verify that contract
	 * can be canceled in-line creating from Contracts sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24447_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		contractsSubpanel = sugar().opportunities.recordView.subpanels.get("Contracts");
		myOpp.navToRecord();
		contractsSubpanel.addRecord();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().contracts.editView.getEditField("name").set(defContractData.get("name"));
		VoodooUtils.focusDefault();
		sugar().contracts.editView.cancel();

		// Verify that creating was correctly cancelled and contract was not created
		contractsSubpanel.expandSubpanel();
		new VoodooControl("div", "css", "div[data-voodoo-name=Contracts] .fld_name.list div").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}