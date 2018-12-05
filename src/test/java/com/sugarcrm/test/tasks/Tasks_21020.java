package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21020 extends SugarTest {
	FieldSet tasksData = new FieldSet();

	public void setup() throws Exception {
		FieldSet profileSettingData = testData.get(testName).get(0);
		tasksData = new FieldSet();
		tasksData.put("date_due_date", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().tasks.api.create(tasksData);
		tasksData.clear();

		// Login as a valid user
		sugar().login();

		// Go to My profile and change the default date format to another one, such as YYYY/MM/DD
		sugar().users.setPrefs(profileSettingData);
	}

	/**
	 * Create Task_Verify that the task's date format is displayed according to the settings in My Accounts.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21020_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Tasks module
		sugar().tasks.navToListView();

		// Define controls for Date Created, Date Modified and Due Date in list view 
		// TODO: VOOD-597 and VOOD-868
		VoodooControl modifiedDataCtrl = new VoodooControl("span", "css", ".fld_date_modified.list");
		VoodooControl createdDataCtrl = new VoodooControl("span", "css", ".fld_date_entered.list");
		VoodooControl dueDataCtrl = sugar().tasks.listView.getDetailField(1, "date_due_date");

		String todaysDateWithUpdatedDateFormat = VoodooUtils.getCurrentTimeStamp("yyyy/MM/dd");
		// Verify that all the date format of the task is displayed as settings
		modifiedDataCtrl.assertContains(todaysDateWithUpdatedDateFormat, true);
		createdDataCtrl.assertContains(todaysDateWithUpdatedDateFormat, true);
		dueDataCtrl.assertContains(todaysDateWithUpdatedDateFormat, true);

		// Create another task with all fields filled
		// (Creating a Task from UI as well, so that we can verify the impact of changes done in the profile setting on both existing and newly created record) 
		tasksData.put("subject", testName);
		tasksData.put("date_due_date", todaysDateWithUpdatedDateFormat);
		sugar().tasks.create(tasksData);
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// Verify that all the date format of the task is displayed as settings
		modifiedDataCtrl.assertContains(todaysDateWithUpdatedDateFormat, true);
		createdDataCtrl.assertContains(todaysDateWithUpdatedDateFormat, true);
		dueDataCtrl.assertContains(todaysDateWithUpdatedDateFormat, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}