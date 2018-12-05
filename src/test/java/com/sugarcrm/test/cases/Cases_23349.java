package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23349 extends SugarTest {
	CaseRecord myCase;
	FieldSet defaultTask;

	public void setup() throws Exception {
		sugar().login();
		defaultTask = sugar().tasks.getDefaultData();
		sugar().accounts.api.create();
		myCase = (CaseRecord)sugar().cases.api.create(sugar().cases.getDefaultData());
	}

	/**
	 * Test Case 23349: Create Task_Verify that task for case is not created in the Tasks subpanel
	 * when using "Cancel" function.
	 */
	@Test
	public void Cases_23349_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCase.navToRecord();

		StandardSubpanel subTasks = sugar().cases.recordView.subpanels.get("Tasks");
		subTasks.addRecord();

		// Put default data and cancel creating a task
		sugar().tasks.createDrawer.showMore();
		sugar().tasks.createDrawer.setFields(defaultTask);
		sugar().tasks.createDrawer.cancel();

		// but create an object with default task data
		TaskRecord taskRecord = new TaskRecord(defaultTask);

		// Verify the task related to the case hasn't been created
		// TODO: VOOD-908
		subTasks.assertContains(taskRecord.getRecordIdentifier(), false);

		// Verify a task hasn't been created
		sugar().tasks.navToListView();
		sugar().tasks.listView.setSearchString(taskRecord.getRecordIdentifier());
		sugar().tasks.listView.getControl("emptyListViewMsg").waitForVisible();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
