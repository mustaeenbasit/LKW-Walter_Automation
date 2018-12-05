package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_29958 extends SugarTest {
	VoodooControl relRelatedToModuleCtrl, relRelatedToValueCtrl;

	public void setup() throws Exception {
		FieldSet tasksName = new FieldSet();
		tasksName.put("subject", testName);

		// Create an Account, a Note and a Task record
		sugar().accounts.api.create();
		sugar().notes.api.create();
		sugar().tasks.api.create(tasksName);

		// Login as an admin user
		sugar().login();

		// Navigates to Notes record view and relate the Account record
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();
		sugar().notes.recordView.showMore();
		relRelatedToModuleCtrl = sugar().notes.recordView.getEditField("relRelatedToModule");
		relRelatedToValueCtrl = sugar().notes.recordView.getEditField("relRelatedToValue");
		relRelatedToModuleCtrl.set(sugar().accounts.moduleNameSingular);
		relRelatedToValueCtrl.set(sugar().accounts.getDefaultData().get("name"));
		sugar().notes.recordView.save();
	}

	/**
	 * Verify that updating 'Notes' subject with same name as of Related 'Tasks' shows task name after Save.
	 * @throws Exception
	 */
	@Test
	public void Notes_29958_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to Notes record view (Make sure the record already has got Related to field filled)
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);

		// Click to edit 'Notes' Subject field and enter existing 'Task Name'
		sugar().notes.recordView.edit();
		sugar().notes.recordView.getEditField("subject").set(testName);

		// Click to edit 'Related To' field. Select Tasks module from the drop down and select existing task record to relate
		relRelatedToModuleCtrl.set(sugar().tasks.moduleNameSingular);
		relRelatedToValueCtrl.set(testName);

		// Click Save button
		sugar().notes.recordView.save();

		// Verify that the user must find 'task1' in 'Related To' field getting saved successfully in Notes
		sugar().notes.recordView.getDetailField("subject").assertEquals(testName, true);
		sugar().notes.recordView.getDetailField("relRelatedToModule").assertEquals(sugar().tasks.moduleNameSingular, true);
		sugar().notes.recordView.getDetailField("relRelatedToValue").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}