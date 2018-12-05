package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */

public class Leads_22643 extends SugarTest {
	StandardSubpanel tasksSubpanel;

	public void setup() throws Exception {
		sugar().leads.api.create();
		TaskRecord myTask = (TaskRecord)sugar().tasks.api.create();
		sugar().login();

		// Task records which are created for a lead exist.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		tasksSubpanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		tasksSubpanel.linkExistingRecord(myTask);
	}

	/**
	 * Edit Task_Verify that task related to a lead can be modified when using "Edit" function.
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_22643_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "edit" link for a task record in tasks sub-panel.
		tasksSubpanel.editRecord(1);

		// Modify fields of the task 
		tasksSubpanel.getEditField(1, "subject").set(testName);
		FieldSet customData = testData.get(testName).get(0);
		tasksSubpanel.getEditField(1, "status").set(customData.get("status"));
		tasksSubpanel.getEditField(1, "date_start_date").set(customData.get("date_start_date"));
		tasksSubpanel.getEditField(1, "date_start_time").set(customData.get("date_start_time"));
		tasksSubpanel.getEditField(1, "date_due_date").set(customData.get("date_due_date"));
		tasksSubpanel.getEditField(1, "date_due_time").set(customData.get("date_due_time"));
		tasksSubpanel.getEditField(1, "relAssignedTo").set(customData.get("relAssignedTo"));

		// Save
		tasksSubpanel.saveAction(1);

		// Verify the information of the modified task is displayed correctly.
		tasksSubpanel.getDetailField(1, "status").assertEquals(customData.get("status"), true);
		tasksSubpanel.getDetailField(1, "date_start_date").assertEquals(customData.get("date_start_date") + " " + customData.get("date_start_time"), true);
		tasksSubpanel.getDetailField(1, "date_due_date").assertEquals(customData.get("date_due_date") + " " + customData.get("date_due_time"), true);
		tasksSubpanel.getDetailField(1, "relAssignedTo").assertEquals(customData.get("relAssignedTo"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}