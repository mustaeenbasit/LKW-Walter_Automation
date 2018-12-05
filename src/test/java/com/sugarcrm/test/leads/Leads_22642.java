package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Leads_22642 extends SugarTest {

	public void setup() throws Exception {
		// Initialize test data.
		sugar().leads.api.create();
		sugar().login();	
	}

	/**
	 * Create Task By Full Form_Verify that task is not created for Lead when using "Cancel" function.
	 * @throws Exception
	 */
	@Test
	public void Leads_22642_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click "Leads" tab on navigation bar and click on existing lead in "Leads" list view.
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);
		
		// Click "Create Task" button in "Tasks" sub-panel and fill form.
		StandardSubpanel tasksSubpanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		FieldSet myTaskData = testData.get(testName).get(0);
		tasksSubpanel.addRecord();
		sugar().tasks.createDrawer.setFields(myTaskData);
		sugar().tasks.createDrawer.cancel();

		// Verify there is no new task created for lead in "Tasks" sub-panel.
		tasksSubpanel.expandSubpanel();
		Assert.assertTrue("Number of records not equals to Zero", tasksSubpanel.countRows() == 0);
		tasksSubpanel.assertContains(myTaskData.get("subject"), false);
		tasksSubpanel.assertContains(myTaskData.get("status"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}