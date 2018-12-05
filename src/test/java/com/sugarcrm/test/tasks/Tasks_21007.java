package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Tasks_21007 extends SugarTest {
	TaskRecord myTaskRecord;
	
	public void setup() throws Exception {
		myTaskRecord = (TaskRecord)sugar.tasks.api.create();
		sugar.login();
	}

	/**
	 * Delete Task_Verify that task can be deleted from detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21007_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to a task's detail view.
		myTaskRecord.navToRecord();
		
		// Click "Delete" button 
		sugar.tasks.recordView.delete();
		
		// Click "Confirm" button in pop-up dialog box.
		sugar.alerts.getWarning().confirmAlert();
		
		sugar.tasks.navToListView();
		
		// Verify that the deleted task is not displayed in the list.
		sugar.tasks.listView.assertIsEmpty();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}