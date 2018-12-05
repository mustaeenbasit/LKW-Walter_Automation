package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24346 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel tasksSubpanel;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
	}

	/**
	 * Test Case 24346: In-Line Create Task_Verify that task can be canceled for in-line creating from Tasks sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24346_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOpp.navToRecord();
		tasksSubpanel = sugar().opportunities.recordView.subpanels.get("Tasks");
		tasksSubpanel.addRecord();

		// Cancel creating task record
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		sugar().tasks.createDrawer.cancel();

		// Verify that creating was correctly cancelled and task was not created
		tasksSubpanel.expandSubpanel();
		tasksSubpanel.assertContains(testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}