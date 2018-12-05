package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22676 extends SugarTest{

	public void setup() throws Exception {
		// Create Lead and Login to system as valid user.
		sugar().leads.api.create();
		sugar().login(sugar().users.getQAUser());
		}

	/**
	 * In-Line Create Task_Verify that task can be canceled for in-line creating from Activities sub-panel for a lead.
	 * @throws Exception
	 */
	@Test
	public void Leads_22676_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to detail view for a lead.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		// Click "Create" button in Tasks sub-panel.
		StandardSubpanel taskSubpanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubpanel.addRecord();
		sugar().tasks.createDrawer.setField("subject", testName);
		
		// Click "Cancel" button.
		sugar().tasks.createDrawer.cancel();
		
		// Verify the task is not created in Tasks sub-panel.
		Assert.assertTrue("Tasks subpanel contains record(s).", taskSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
