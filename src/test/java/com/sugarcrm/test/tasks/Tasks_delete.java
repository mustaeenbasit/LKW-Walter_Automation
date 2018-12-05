package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;


import static org.junit.Assert.assertEquals;

public class Tasks_delete extends SugarTest {
	TaskRecord myTask;
	
	public void setup() throws Exception {
		sugar.login();
		myTask = (TaskRecord)sugar.tasks.api.create();
	}

	@Test
	public void Tasks_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the task using the UI.
		myTask.delete();
		
		// Verify the task was deleted.
		sugar.tasks.navToListView();
		assertEquals(VoodooUtils.contains(myTask.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}