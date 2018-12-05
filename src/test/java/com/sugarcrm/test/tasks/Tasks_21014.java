package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21014 extends SugarTest {

	public void setup() throws Exception {
		sugar().tasks.api.create();
		sugar().login();
	}

	/**
	 * Mass Update Task_Verify that task can be deleted by "Mass Update"
	 * function.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21014_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Tasks" link in navigation shortcuts.
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);

		// Select task record by checking the check box in front of the record.
		sugar().tasks.listView.checkRecord(1);

		// Open Action drop down
		sugar().tasks.listView.openActionDropdown();

		// Click "Delete" button on panel Action drop down.
		sugar().tasks.listView.delete();

		sugar().alerts.getWarning().confirmAlert();

		// Verify that the selected tasks is not displayed in the list.
		sugar().tasks.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}