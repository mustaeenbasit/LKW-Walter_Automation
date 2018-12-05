package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.ListView;
import com.sugarcrm.test.SugarTest;

public class Tasks_21032 extends SugarTest {

	public void setup() throws Exception {
		// Create a task record
		sugar().tasks.api.create();
	}

	/**
	 * Edit Task_Verify that editing a task from list view can be canceled.
	 * @throws Exception
	 */
	@Test
	public void Tasks_21032_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log-In as qaUser
		sugar().login(sugar().users.getQAUser());

		// Click "Tasks" link in navigation shortcuts
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);

		ListView taskListView = sugar().tasks.listView;
		VoodooControl taskNameRecord1 =  taskListView.getDetailField(1, "subject");

		// Click "Edit" icon at the right edge of a task record.
		taskListView.editRecord(1);

		// Modify the task Name
		taskListView.getEditField(1, "subject").set(testName);

		//  Click "Cancel" button
		taskListView.cancelRecord(1);

		// Assert that the record has not been modified
		taskNameRecord1.assertEquals(testName, false);

		// Assert that the task name is the default task name
		taskNameRecord1.assertEquals(sugar().tasks.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}