package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21031 extends SugarTest {
	FieldSet dashletsData = new FieldSet();
	UserRecord myUser;

	public void setup() throws Exception {
		dashletsData = testData.get(testName).get(0);

		// login
		sugar().login();

		// Create a custom user
		// TODO: VOOD-1200
		myUser = (UserRecord) sugar().users.create();

		// Logout from Admin user and login as the custom user
		sugar().logout();
		myUser.login();

		// Put task's Subject, Description, Due Date and Start Date into field set
		FieldSet taskData = new FieldSet();
		taskData.put("subject", testName);
		taskData.put("description", dashletsData.get("description"));
		taskData.put("date_start_date", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		taskData.put("date_due_date", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));

		// Create a Task record
		sugar().tasks.create(taskData);
		sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Edit Task_Verify that editing a task from detail view can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21031_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to My Dashboard and add Active Task dashlet
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().dashboard.edit();
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(4, 1);

		// TODO: VOOD-960, VOOD-1645
		// Add a dashlet -> select "Active Tasks"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashletsData.get("activeTasks"));
		VoodooUtils.waitForReady(); // Wait Needed
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Save
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Open Record view of any task that is shown in Active Task dashlet
		// TODO: VOOD-960
		VoodooControl activeTaskDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(4) .dashlet-container:nth-of-type(1)");
		VoodooControl recordInActiveTaskDashletCtrl = new VoodooControl("a", "css", activeTaskDashletCtrl.getHookString() + " .tab-content p a:nth-child(2)");
		recordInActiveTaskDashletCtrl.assertEquals(testName, true);
		recordInActiveTaskDashletCtrl.click();
		VoodooUtils.waitForReady();

		// Click "Edit" button and Modify the task
		sugar().tasks.recordView.edit();
		sugar().tasks.recordView.showMore();
		sugar().tasks.recordView.getEditField("subject").set(sugar().tasks.getDefaultData().get("subject"));
		sugar().tasks.recordView.getEditField("description").set(dashletsData.get("updatedDescription"));

		// Click "Cancel" button
		sugar().tasks.recordView.cancel();

		// Verify that the task detail information is displayed as original
		sugar().tasks.recordView.getDetailField("subject").assertEquals(testName, true);
		sugar().tasks.recordView.getDetailField("description").assertEquals(dashletsData.get("description"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}