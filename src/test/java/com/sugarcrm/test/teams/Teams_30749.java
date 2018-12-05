package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_30749 extends SugarTest {
	public void setup() throws Exception {
		// login as a qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify record is saved successfully and accessible from module list view when changing its team member
	 *
	 * @throws Exception
	 */
	@Test
	public void Teams_30749_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teamNameFS = testData.get(testName).get(0);

		// Navigate to Tasks module > Create task.
		sugar().navbar.selectMenuItem(sugar().tasks, "create" + sugar().tasks.moduleNameSingular);
		FieldSet taskData = sugar().tasks.getDefaultData();
		taskData.put("relTeam", teamNameFS.get("teamName"));
		
		// Fill all the required field
		sugar().tasks.createDrawer.getEditField("subject").set(taskData.get("subject"));
		sugar().tasks.createDrawer.getEditField("priority").set(taskData.get("priority"));
		sugar().tasks.createDrawer.showMore();
		sugar().tasks.createDrawer.getEditField("status").set(taskData.get("status"));
		
		// Change teams other than global or logged in user
		sugar().tasks.createDrawer.getEditField("relTeam").set(taskData.get("relTeam"));
		sugar().tasks.createDrawer.getEditField("date_due_date").set(taskData.get("date_due_date"));
		sugar().tasks.createDrawer.getEditField("date_start_date").set(taskData.get("date_start_date"));
		
		// Click on save button
		sugar().tasks.createDrawer.save();

		// Verify that Task record is saved successfully without any error message pop up.
		sugar().alerts.getSuccess().assertVisible(true);

		// Verify that Task record is available in the list view and accessible from the list view.
		sugar().tasks.listView.verifyField(1, "subject", taskData.get("subject"));
		sugar().tasks.listView.clickRecord(1);
		sugar().tasks.recordView.getDetailField("subject").assertEquals(taskData.get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}