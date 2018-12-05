package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Tasks_19759 extends SugarTest {
	FieldSet tasksDefaultData;

	public void setup() throws Exception {
		tasksDefaultData = sugar.tasks.getDefaultData();
		sugar.login();
	}

	/**
	 * Test Case 19759: Check that Call | Meeting | Task could be created from Quick Create (+)
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_19759_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open QuickCreate and click "Create Task"
		sugar.navbar.quickCreateAction(sugar.tasks.moduleNamePlural);

		// Set required fields
		sugar.tasks.createDrawer.showMore();
		sugar.tasks.createDrawer.setFields(tasksDefaultData);
		// Click Save
		sugar.tasks.createDrawer.save();

		// Verify that user is returned to Home module after task is created
		sugar.home.dashboard.assertContains("My Dashboard", true);

		// Verify a task record was created
		TaskRecord mytask = new TaskRecord(tasksDefaultData);
		mytask.verify(tasksDefaultData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}