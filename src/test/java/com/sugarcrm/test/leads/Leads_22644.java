package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Leads_22644 extends SugarTest {
	TaskRecord myTask;
	StandardSubpanel taskSubpanel;

	public void setup() throws Exception {
		sugar().leads.api.create();
		myTask = (TaskRecord)sugar().tasks.api.create();
		sugar().login();

		// Link task record to lead record
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		taskSubpanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubpanel.linkExistingRecord(myTask);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Edit Task_Verify that editing task related to a lead can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_22644_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet taskData = testData.get(testName).get(0);

		// TODO: 1424 Once resolved, need to verify records via verify() method
		VoodooControl subject = taskSubpanel.getDetailField(1, "subject");
		VoodooControl status = taskSubpanel.getDetailField(1, "status");
		subject.assertEquals(sugar().tasks.getDefaultData().get("subject"), true);
		status.assertEquals(sugar().tasks.getDefaultData().get("status"), true);

		// Editing subject, status fields on task record and cancel
		taskSubpanel.editRecord(1);
		taskSubpanel.getEditField(1, "subject").set(taskData.get("subject"));
		taskSubpanel.getEditField(1, "status").set(taskData.get("status"));
		taskSubpanel.cancelAction(1);

		// Verify the information of the task related to the lead is not changed.
		subject.assertEquals(taskData.get("subject"), false);
		status.assertEquals(taskData.get("status"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}