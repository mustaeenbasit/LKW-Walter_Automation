package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;

public class Tasks_26179 extends SugarTest {
	TaskRecord myTask;

	public void setup() throws Exception {
		myTask = (TaskRecord)sugar().tasks.api.create();
		sugar().login();
	}

	/**
	 * Verify Navigation and display of the Task Preview pane
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_26179_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet myTestData = testData.get(testName).get(0);
		sugar().tasks.navToListView();
		sugar().tasks.listView.previewRecord(1);
		myTask.verifyPreview(myTestData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}