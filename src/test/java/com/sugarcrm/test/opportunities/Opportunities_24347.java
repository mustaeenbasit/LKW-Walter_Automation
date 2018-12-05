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

public class Opportunities_24347 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel meetsSubpanel;
	DataSource meetDS;

	public void setup() throws Exception {
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		meetDS = testData.get(testName);
		sugar().login();
	}

	/**
	 * Test Case 24347: In-Line Create Meeting_Verify that meeting can be in-line created from Meetings sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24347_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open opportunity record view and inline-create a related meeting
		myOpp.navToRecord();
		meetsSubpanel = sugar().opportunities.recordView.subpanels.get("Meetings");
		meetsSubpanel.addRecord();
		sugar().meetings.createDrawer.setFields(meetDS.get(0));
		sugar().meetings.createDrawer.save();

		// Verify the meeting is successfully created and visible in the meetings subpanel of the opportunity
		meetsSubpanel.expandSubpanel();
		meetsSubpanel.verify(1, meetDS.get(0), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}