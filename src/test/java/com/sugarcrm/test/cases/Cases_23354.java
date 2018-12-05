package com.sugarcrm.test.cases;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23354 extends SugarTest {
	CaseRecord myCase;
	TaskRecord relTask;
	FieldSet customData, defaultTaskData, taskSubpanelData = new FieldSet();
	StandardSubpanel subTasks;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		defaultTaskData = sugar().tasks.getDefaultData();
		taskSubpanelData.put("subject", defaultTaskData.get("subject"));
		taskSubpanelData.put("status", defaultTaskData.get("status"));
		taskSubpanelData.put("date_due_date", defaultTaskData.get("date_due_date"));
		taskSubpanelData.put("date_start_date", defaultTaskData.get("date_start_date"));

		sugar().login();
		sugar().accounts.api.create();
		myCase = (CaseRecord)sugar().cases.create();
		relTask = (TaskRecord)sugar().tasks.create();

		// Link a task to a case
		myCase.navToRecord();
		subTasks = sugar().cases.recordView.subpanels.get("Tasks");
		subTasks.scrollIntoViewIfNeeded(false);
		subTasks.linkExistingRecord(relTask);
	}

	/**
	 * Test Case 23354: Edit Task_Verify that inline modification of task in subpanel of a case record can be canceled.
	 * */
	@Test
	public void Cases_23354_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit related task inline
		subTasks.editRecord(1);
		// TODO: VOOD-503
		new VoodooSelect("div", "css", ".subpanel [data-voodoo-name='status'] div.select2").set(customData.get("status"));
		new VoodooControl("input", "css", "[data-voodoo-name='name'] input").set(customData.get("subject"));
		new VoodooControl("input", "css", "[data-voodoo-name='date_start'] input.datepicker").set(customData.get("date_start_date"));
		new VoodooControl("input", "css", "[data-voodoo-name='date_due'] input.datepicker").set(customData.get("date_due_date"));
		// and Cancel
		subTasks.cancelAction(1);

		// Verify that task information is not changed in the subpanel of a case
		subTasks.verify(1, taskSubpanelData, true);
		// and into its record view.
		relTask.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
