package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21030 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Create Task_Verify that creating a task can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21030_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Create Task" by going to Task Module
		sugar().navbar.selectMenuItem(sugar().tasks, "createTask");

		// Fill all the mandatory fields.
		sugar().tasks.createDrawer.getEditField("subject").set(testName);

		// Click "Cancel" button
		sugar().tasks.createDrawer.cancel();

		// Verify that the task list view is displayed as current page
		// TODO: VOOD-1887 - Uncomment the line written below after this VOOD will resolved
		// sugar().tasks.listView.assertVisible(true);
		sugar().tasks.listView.getControl("createButton").assertVisible(true);
		sugar().tasks.listView.getControl("moduleTitle").assertEquals(sugar().tasks.moduleNamePlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}