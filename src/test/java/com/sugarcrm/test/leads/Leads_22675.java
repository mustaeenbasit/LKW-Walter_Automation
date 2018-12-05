package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Leads_22675 extends SugarTest {
	LeadRecord myLead;
	StandardSubpanel tasksSubpanel;
	FieldSet myTaskData;

	public void setup() throws Exception {
		sugar().login();
		myLead = (LeadRecord) sugar().leads.api.create();
		myTaskData = testData.get(testName).get(0);

	}

	/**
	 * Test Case 22675: In-Line Create Task_Verify that task can be in-line created from Tasks sub-panel for a lead
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_22675_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myLead.navToRecord();
		tasksSubpanel = sugar().leads.recordView.subpanels.get("Tasks");
		tasksSubpanel.addRecord();
		sugar().tasks.createDrawer.setFields(myTaskData);
		sugar().tasks.createDrawer.save();
		VoodooUtils.waitForAlertExpiration();

		// Verify that created task is available in Tasks subpanel
		tasksSubpanel.expandSubpanel();
		tasksSubpanel.assertContains(myTaskData.get("subject"), true);
		tasksSubpanel.assertContains(myTaskData.get("status"), true);

		// Go to created task record view and verify record details
		tasksSubpanel.clickRecord(1);
		sugar().tasks.recordView.showMore();
		sugar().tasks.recordView.getDetailField("subject").assertEquals(myTaskData.get("subject"), true);
		sugar().tasks.recordView.getDetailField("priority").assertEquals(myTaskData.get("priority"), true);
		sugar().tasks.recordView.getDetailField("status").assertEquals(myTaskData.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}