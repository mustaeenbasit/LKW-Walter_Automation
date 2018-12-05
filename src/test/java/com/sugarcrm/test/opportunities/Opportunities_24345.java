package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24345 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel taskSubpanel;
	DataSource tasksDS;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		tasksDS = testData.get(testName);
	}

	/**
	 * Test Case 24345: In-Line Create Task_Verify that task can be in-line
	 * created from Tasks sub-panel for an opportunity
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24345_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		taskSubpanel = sugar().opportunities.recordView.subpanels.get("Tasks");
		// Open opportunity record view and inline-create a related task
		myOpp.navToRecord();
		taskSubpanel.expandSubpanel();
		taskSubpanel.addRecord();
		sugar().tasks.createDrawer.getEditField("subject").set(tasksDS.get(0).get("subject"));
		sugar().tasks.createDrawer.save();
		VoodooUtils.pause(2000);
		
		// Verify the task is successfully created and visible in tasks subpanel of the opportunity
		myOpp.navToRecord();
		taskSubpanel.expandSubpanel();
		new VoodooControl("a", "css", ".fld_name.list div").assertContains((tasksDS.get(0).get("subject")), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}