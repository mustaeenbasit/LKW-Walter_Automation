package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21004 extends SugarTest {

	public void setup() throws Exception {
		sugar.tasks.api.create();
		sugar.login();
	}

	/**
	 * Edit Task_Verify that task can be edited from list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21004_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Click "Tasks" link in navigation shortcuts.
		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);
		
		// Click "Edit" icon at the right edge of a task record.
		sugar.tasks.listView.editRecord(1);
		
		// Modify the task.
		sugar.tasks.listView.getEditField(1, "subject").set(testName);
		
		// Click "Save" button
		sugar.tasks.listView.saveRecord(1);
		
		// Verify that task detail information is displayed as modified.
		sugar.tasks.listView.verifyField(1, "subject", testName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}