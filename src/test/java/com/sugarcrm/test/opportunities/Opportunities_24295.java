package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24295 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Full Form Create Task_Verify that the task is not created in full form mode for opportunity when using 
	 * "Cancel" function.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24295_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel taskSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		
		// Click "Create Task" button in "Tasks" sub-panel
		taskSubpanel.addRecord();
		
		// Cancel creating the task for the opportunity
		sugar().tasks.createDrawer.cancel();
		
		// Verify that there is no new task created in "Tasks" sub-panel
		int recordCount = taskSubpanel.countRows();
		Assert.assertTrue("Record exist in Task subpanel", recordCount == 0);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}