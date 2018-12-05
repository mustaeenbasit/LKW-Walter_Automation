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

public class Opportunities_24348 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel meetSub;
	DataSource meetDS;

	public void setup() throws Exception {
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();

		sugar().login();
		meetDS = testData.get(testName);
	}

	/**
	 * Test Case 24348: In-Line Create Meeting_Verify that meeting can be canceled for in-line creating from Meetings sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24348_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity record
		myOpp.navToRecord();
		meetSub = sugar().opportunities.recordView.subpanels.get("Meetings");

		// Click "Create Meeting"  in Meetings sub-panel
		meetSub.addRecord();
		sugar().meetings.createDrawer.getEditField("name").set(meetDS.get(0).get("name"));
		sugar().meetings.createDrawer.getEditField("status").set(meetDS.get(0).get("status"));

		// Click "Cancel"
		sugar().meetings.createDrawer.cancel();

		// Verify the meeting is cancelled and not visible in meetings subpanel of the opportunity
		meetSub.expandSubpanel();
		meetSub.assertContains(meetDS.get(0).get("name"), false);
		meetSub.assertContains(meetDS.get(0).get("status"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}