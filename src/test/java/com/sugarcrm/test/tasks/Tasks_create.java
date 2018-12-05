package com.sugarcrm.test.tasks;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Tasks_create extends SugarTest {
	
	public void setup() throws Exception {
		sugar.login();
	}

	@Test
	public void Tasks_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		TaskRecord myTask = (TaskRecord)sugar.tasks.create();
		myTask.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}