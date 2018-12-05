package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Tasks_21006 extends SugarTest {
	TaskRecord myTaskRecord;
	
	public void setup() throws Exception {
		myTaskRecord = (TaskRecord)sugar.tasks.api.create();
		sugar.login();
	}

	/**
	 * Duplicate Task_Verify that task can be duplicated.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21006_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to a task's detail view.
		myTaskRecord.navToRecord();
		
		// Click "Copy" button.
		sugar.tasks.recordView.copy();
		
		// Modify the task.
		sugar.tasks.createDrawer.getEditField("subject").set(testName);
		
		// Click "Save" button.
		sugar.tasks.createDrawer.save();
		
		sugar.tasks.navToListView();
		
		// Verify that The duplicated task detail information is displayed as modified.
		sugar.tasks.listView.verifyField(1, "subject", testName);
		sugar.tasks.listView.verifyField(1, "relRelatedToParent", sugar.tasks.getDefaultData().get("subject"));
		sugar.tasks.listView.verifyField(2, "subject", sugar.tasks.getDefaultData().get("subject"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}