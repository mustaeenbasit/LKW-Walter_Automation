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

public class Opportunities_24377 extends SugarTest {
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
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
	}

	/**
	 * Test Case 24377: Edit Contract_Verify that the information of contract related to an opportunity
	 * is updated in "Contracts" sub-panel after it is edited in "Contracts" module
	 *
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24377_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		contractsSubpanel = sugar().opportunities.recordView.subpanels.get("Contracts");
		// Open opportunity record view and inline-create a related contract
		myOpp.navToRecord();
		contractsSubpanel.addRecord();
		VoodooUtils.pause(1500);
		sugar().contracts.editView.setFields(contractDS.get(0));
		sugar().contracts.editView.save();
		VoodooUtils.pause(2500);

		// Go to Contracts module, open edit view of the created contract. Edit fields and save
		sugar().contracts.navToListView();
		sugar().contracts.listView.clearSearchForm();
		sugar().contracts.listView.basicSearch(contractDS.get(0).get("name"));
		sugar().contracts.listView.editRecord(1);
		sugar().contracts.editView.setFields(contractDS.get(1));
		sugar().contracts.editView.save();

		// Go back to contracts subpanel of the opportunity and verify related contracts details
		myOpp.navToRecord();
		contractsSubpanel.expandSubpanel();
		new VoodooControl("a", "css", ".fld_name.list div").assertContains((contractDS.get(1).get("name")), true);
		new VoodooControl("a", "css", ".fld_account_name.list div").assertContains((contractDS.get(1).get("account_name")), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}

