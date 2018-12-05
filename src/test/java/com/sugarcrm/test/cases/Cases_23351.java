package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23351 extends SugarTest {
	FieldSet customData;
	StandardSubpanel subTasks;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().cases.api.create();
		TaskRecord myTask = (TaskRecord)sugar().tasks.api.create();
		sugar().login();

		// Link a task to a case
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		subTasks = sugar().cases.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		subTasks.scrollIntoViewIfNeeded(false);
		subTasks.linkExistingRecord(myTask);
	}

	/**
	 * Edit Task_Verify that the information of a task for case can be modified
	 * when using "Edit" function in the Tasks sub-panel.
	 *
	 */
	@Test
	public void Cases_23351_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit related task inline and Save
		FieldSet editedData = new FieldSet();
		editedData.put("subject", customData.get("subject"));
		editedData.put("status", customData.get("status"));
		editedData.put("date_start_date", customData.get("date_start_date"));
		editedData.put("date_due_date", customData.get("date_due_date"));
		subTasks.editRecord(1, editedData);

		// TODO: VOOD-1424
		// Verify that task is updated
		//subTasks.verify(1, customData, true);
		subTasks.getDetailField(1, "subject").assertEquals(customData.get("subject"), true);
		subTasks.getDetailField(1, "status").assertEquals(customData.get("status"), true);
		subTasks.getDetailField(1, "date_start_date").assertContains(customData.get("date_start_date"), true);
		subTasks.getDetailField(1, "date_due_date").assertContains(customData.get("date_due_date"), true);


		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
