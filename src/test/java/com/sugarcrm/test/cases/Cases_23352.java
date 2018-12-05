package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23352 extends SugarTest {
	TaskRecord relTask;
	StandardSubpanel subTasks;

	public void setup() throws Exception {
		CaseRecord myCase = (CaseRecord)sugar().cases.api.create();
		relTask = (TaskRecord)sugar().tasks.api.create();
		sugar().login();

		// Link a task to a case
		myCase.navToRecord();
		subTasks = sugar().cases.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		subTasks.linkExistingRecord(relTask);
	}

	/**
	 * Verify that task detail view is displayed
	 * after clicking the subject name of an existing task in the Tasks subpanel from case record view.
	 * @throws Exception
	 * */
	@Test
	public void Cases_23352_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1100
		// Verify that the task is linked correctly
		FieldSet taskSubject = new FieldSet();
		taskSubject.put("subject", relTask.get("subject"));
		subTasks.verify(1,taskSubject, true);

		// click subject on 1st record
		subTasks.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify RecordView of the task is rendered
		sugar().tasks.recordView.getDetailField("subject").assertVisible(true);
		sugar().tasks.recordView.getDetailField("subject").assertEquals(relTask.get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
