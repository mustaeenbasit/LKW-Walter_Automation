package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_29842 extends SugarTest {
	TaskRecord linkTask;

	public void setup() throws Exception {
		// Create an Opportunity record
		sugar().opportunities.api.create();

		// Create a Task Record
		linkTask = (TaskRecord) sugar().tasks.api.create();

		// Log-In as admin
		sugar().login();
	}

	/**
	 * Verify Time Dropdown is displayed for the Start Date and Due Date field while inline editing a task record.
	 * @throws Exception
	 */
	@Test
	public void Opportunities_29842_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the Opportunity record view
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.clickRecord(1);

		// Link a task to the Opportunity
		StandardSubpanel taskSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubpanel.linkExistingRecord(linkTask);

		// Edit the Task in the taskSubpanel
		taskSubpanel.editRecord(1);

		// Click on the Time field for start date 
		taskSubpanel.getEditField(1, "date_start_time").click();

		// TODO: VOOD-910 - Need to have a lib to support for the datePicker and timePicker widget in BWC and sidecar modules
		// Assert that Time dropdown should be displayed when clicked on the time field of start date
		new VoodooControl("div", "css", ".fld_date_start.edit .ui-timepicker-wrapper").assertVisible(true);

		// Click on the Time field for due date 
		taskSubpanel.getEditField(1, "date_due_time").click();

		// Assert that Time dropdown should be displayed when clicked on the time field of Due Date.
		new VoodooControl("div", "css", ".fld_date_due.edit .ui-timepicker-wrapper").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}