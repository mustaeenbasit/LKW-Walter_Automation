package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21001 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Create Task_Verify that task can be created with only mandatory fields entered.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21001_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String subjectName = sugar().tasks.getDefaultData().get("subject");

		// Verifying record does not save, without data in fields and close the error alert
		sugar().navbar.selectMenuItem(sugar().tasks, "create"+sugar().tasks.moduleNameSingular);
		sugar().tasks.createDrawer.save();
		sugar().alerts.getError().closeAlert();
		sugar().tasks.createDrawer.cancel();
		sugar().tasks.listView.assertIsEmpty();

		// Only subject name is Required field
		sugar().navbar.clickModuleDropdown(sugar().tasks);
		sugar().tasks.menu.getControl("create"+sugar().tasks.moduleNameSingular).click();
		VoodooUtils.waitForReady();

		VoodooControl subject = sugar().tasks.createDrawer.getEditField("subject");
		subject.assertAttribute("class", "required", true);
		subject.set(subjectName);
		sugar().tasks.createDrawer.save();

		// Verify task is saved and display in listview
		sugar().tasks.listView.verifyField(1, "subject", subjectName);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}