package com.sugarcrm.test.cases;

import org.junit.Assert;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23355 extends SugarTest {
	CaseRecord myCase;
	TaskRecord relTask;
	StandardSubpanel subTasks;

	public void setup() throws Exception {
		myCase = (CaseRecord)sugar().cases.api.create();
		relTask = (TaskRecord)sugar().tasks.api.create();
		sugar().login();

		// Link a task to a case
		myCase.navToRecord();
		subTasks = sugar().cases.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		subTasks.scrollIntoViewIfNeeded(false);
		subTasks.linkExistingRecord(relTask);
	}

	/**
	 * Verify that deleted task isn't displayed in Tasks subpanel
	 * @throws Exception
	 * */
	@Test
	public void Cases_23355_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1424
		// Verify that the task is linked correctly
		subTasks.getDetailField(1, "subject").assertEquals(relTask.get("subject"), true);

		// click on subject
		subTasks.clickRecord(1);

		// Verify RecordView of the task is rendered
		sugar().tasks.recordView.getDetailField("subject").assertEquals(relTask.get("subject"), true);

		// Delete task
		sugar().tasks.recordView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Open the case record view again
		myCase.navToRecord();

		// Verify that a task is not displayed in the Tasks subpanel in the case record view.
		subTasks.scrollIntoViewIfNeeded(false);
		Assert.assertTrue("The subpanel is not empty", subTasks.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
