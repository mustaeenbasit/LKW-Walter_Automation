package com.sugarcrm.test.cases;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.test.SugarTest;

public class Cases_23353 extends SugarTest {
	CaseRecord myCase;
	TaskRecord relTask;
	StandardSubpanel subTasks;

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		relTask = (TaskRecord)sugar().tasks.create();
		myCase = (CaseRecord)sugar().cases.create();

		// Link a task to a case
		myCase.navToRecord();
		subTasks = sugar().cases.recordView.subpanels.get("Tasks");
		subTasks.scrollIntoViewIfNeeded(false);
		subTasks.linkExistingRecord(relTask);
	}

	/**
	 * Test Case 23353: Edit Task_Verify that modification of task for case can be canceled in task detail view.
	 * */
	@Test
	public void Cases_23353_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that the task is linked correctly
		FieldSet taskSubjData = new FieldSet();
		taskSubjData.put("subject", sugar().tasks.getDefaultData().get("subject"));
		subTasks.verify(1, taskSubjData, true);
		// and click name link
		subTasks.clickRecord(1);

		// Verify RecordView of the task is rendered
		sugar().tasks.recordView.getDetailField("subject").waitForVisible();

		// Click Edit button
		sugar().tasks.recordView.edit();
		// update fields
		sugar().tasks.recordView.setFields(testData.get(testName).get(0));
		// and click Cancel
		sugar().tasks.recordView.cancel();

		// Task information is not changed in the subpanel of a case
		myCase.navToRecord();
		subTasks.verify(1, taskSubjData, true);
		// and into its record view.
		relTask.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
