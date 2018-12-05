package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24350 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel callsSub;
	DataSource callDS;

	public void setup() throws Exception {
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();

		sugar().login();
		callDS = testData.get(testName);
	}

	/**
	 * Test Case 24350: In-Line Create Call_Verify that call can be cancelled for in-line creating from Calls sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24350_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new call record 
		myOpp.navToRecord();
		callsSub = sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSub.addRecord();
		sugar().calls.createDrawer.getEditField("name").set(callDS.get(0).get("name"));
		sugar().calls.createDrawer.getEditField("status").set(callDS.get(0).get("status"));

		// Click Cancel
		sugar().calls.createDrawer.cancel();

		// Verify that creating was correctly cancelled and call was not created
		callsSub.expandSubpanel();
		callsSub.assertContains(callDS.get(0).get("name"), false);
		callsSub.assertContains(callDS.get(0).get("status"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}