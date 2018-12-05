package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21109 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();	
	}

	/**
	 * Activities-Create Task-Verified that warning message is displayed when creating task with start date later than due date.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_21109_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar.navbar.selectMenuItem(sugar.tasks, "createTask");
		// Enter valid subject in "Subject" text field.
		sugar.tasks.createDrawer.getEditField("subject").set(testName);
		// Enter dates in "Start Date" and "Due Date" fields, make sure start date is later than due date, s
		sugar.tasks.createDrawer.getEditField("date_start_date").set(customData.get("startDate"));
		sugar.tasks.createDrawer.getEditField("date_start_time").click(); // to close datepicker
		sugar.tasks.createDrawer.getEditField("date_due_date").set(customData.get("dueDate"));
		// Click "Save" button
		sugar.tasks.createDrawer.save();

		// Verify Warning message is displayed under "Start Date" field to prompt that start date should be earlier than due date, and the task is not saved.
		sugar.alerts.getError().closeAlert();
		// TODO: VOOD-1292
		new VoodooControl("span", "css", ".error-tooltip.add-on").assertAttribute("data-original-title", customData.get("warningText"));
		// Cancel
		sugar.tasks.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}