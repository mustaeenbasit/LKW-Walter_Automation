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

public class Opportunities_24349 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel callsSubpanel;
	DataSource callDS;

	public void setup() throws Exception {
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		callDS = testData.get(testName);

		sugar().login();
	}

	/**
	 * Test Case 24349: In-Line Create Call_Verify that call can be in-line created from Calls sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24349_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open opportunity record view and inline-create a related call
		myOpp.navToRecord();
		callsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.addRecord();
		sugar().calls.createDrawer.setFields(callDS.get(0));
		sugar().calls.createDrawer.save();

		// Verify the call is successfully created and visible in calls subpanel of the opportunity
		callsSubpanel.expandSubpanel();
		callsSubpanel.verify(1, callDS.get(0), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}