package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20211 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify module name in Quick Create form from shortcut sync with updating module name 
	 * @throws Exception
	 */
	@Test
	public void Admin_20211_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.renameModule(sugar().tasks, customData.get("singularLabel"), customData.get("pluralLabel"));
		
		// Click on quick create to select sidecar module, Tasks.
		sugar().navbar.quickCreateAction(sugar().tasks.moduleNamePlural);
		
		// create a task record.
		sugar().tasks.createDrawer.getEditField("subject").set(customData.get("taskName"));
		sugar().tasks.createDrawer.save();

		// Tasks module
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().tasks);

		// Assert that all Tasks should be TKs and all Task should be TK in Task's menu
		sugar().tasks.menu.getControl("createTask").assertContains(customData.get("singularLabel"), true);
		sugar().tasks.menu.getControl("viewTasks").assertContains(customData.get("pluralLabel"), true);
		sugar().tasks.menu.getControl("importTasks").assertContains(customData.get("pluralLabel"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}