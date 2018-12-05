package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.TaskRecord;

public class Tasks_26177 extends SugarTest {
	FieldSet myTestData;
	TaskRecord myTask;

	public void setup() throws Exception {
		myTestData = testData.get("Tasks_26177").get(0);
		sugar.login();
		myTask = (TaskRecord)sugar.tasks.api.create();
		sugar.accounts.api.create();
		sugar.contacts.api.create();
	}

	/**
	 * Verify that the in line edit works for Task Items List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_26177_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.tasks.navToListView();
		// Inline Edit the record and save
		// TODO VOOD-662 Create a Lib Function to move SugarCRM scroll bars
		sugar.tasks.listView.updateRecord(1, myTestData);
		// Verify Record updated
		sugar.tasks.listView.verifyField(1, "subject",
				myTestData.get("subject"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
