package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.TaskRecord;

public class Tasks_26178 extends SugarTest {
	FieldSet myTestData;
	TaskRecord myTask;

	public void setup() throws Exception {
		myTestData = testData.get("Tasks_26178").get(0);
		sugar.login();
		myTask = (TaskRecord)sugar.tasks.api.create();
		sugar.accounts.api.create();
		sugar.contacts.api.create();
	}

	/**
	 * Verify that the in line edit Cancel works for Task Items List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_26178_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.tasks.navToListView();
		// TODO VOOD-662 Create a Lib Function to move SugarCRM scroll bars
		sugar.tasks.listView.editRecord(1);
		sugar.tasks.listView.setEditFields(1, myTestData);
		sugar.tasks.listView.cancelRecord(1);
		// Verify Record has not been updated
		sugar.tasks.listView.verifyField(1, "subject", myTask.get("subject"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
