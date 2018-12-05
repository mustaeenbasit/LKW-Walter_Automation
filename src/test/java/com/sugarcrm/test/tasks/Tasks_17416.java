package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Tasks_17416 extends SugarTest {
	TaskRecord myTask;
	
	public void setup() throws Exception {
		myTask = (TaskRecord)sugar.tasks.api.create();
		sugar.login();	
	}

	/**
	 * Verify Edit action on Tasks record view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_17416_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myTask.navToRecord();
		
		//Verify that the default action is set to "Edit" on the top right of the record view
		sugar.tasks.recordView.getControl("editButton").assertVisible(true);		
		sugar.tasks.recordView.edit();
		sugar.tasks.recordView.showMore();
		
		// Verify that entire page is editable except for "Date Created" and "Date Modified"
		sugar.tasks.recordView.getEditField("subject").assertExists(true);
		sugar.tasks.recordView.getEditField("priority").assertExists(true);
		sugar.tasks.recordView.getEditField("status").assertExists(true);
		sugar.tasks.recordView.getEditField("relRelatedToParentType").assertExists(true);
		sugar.tasks.recordView.getEditField("relRelatedToParent").assertExists(true);
		sugar.tasks.recordView.getEditField("description").assertExists(true);
		sugar.tasks.recordView.getEditField("contactName").assertExists(true);
		sugar.tasks.recordView.getEditField("relTeam").assertExists(true);
		sugar.tasks.recordView.getEditField("relAssignedTo").assertExists(true);
		sugar.tasks.recordView.getEditField("date_due_time").assertExists(true);
		sugar.tasks.recordView.getEditField("date_start_time").assertExists(true);
		sugar.tasks.recordView.getEditField("date_due_date").assertExists(true);
		sugar.tasks.recordView.getEditField("date_start_date").assertExists(true);
		
		// TODO VOOD-868: Lib support to verify "Date Created" column
		// Verify that Date Created and Date Modified are not editable
		new VoodooControl("div", "css",".fld_date_entered.edit").assertVisible(false);
		new VoodooControl("div", "css",".fld_date_modified.edit").assertVisible(false);
		
		sugar.tasks.recordView.showLess();
		sugar.tasks.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
