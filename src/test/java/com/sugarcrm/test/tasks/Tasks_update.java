package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;

public class Tasks_update extends SugarTest {
	TaskRecord myTask;

	public void setup() throws Exception {
		sugar.login();
		myTask = (TaskRecord)sugar.tasks.api.create();
	}

	@Test
	public void Tasks_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("date_due_date", "10/01/2023");
		newData.put("status", "In Progress");

		// Edit the task using the UI.
		myTask.edit(newData);
		
		// Verify the task was edited.
		myTask.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}