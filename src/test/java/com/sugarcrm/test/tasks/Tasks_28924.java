package com.sugarcrm.test.tasks;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Tasks_28924 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that Mytask filter is NOT getting displayed by default in the Related section of record view of a Task 
	 * @throws Exception
	 */
	@Test
	public void Tasks_28924_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet filterData = testData.get(testName).get(0);
		
		// Navigating to Tasks module
		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);
		sugar.tasks.listView.create();
		sugar.tasks.createDrawer.getEditField("subject").set(sugar.tasks.getDefaultData().get("subject"));
		sugar.tasks.createDrawer.save();
		sugar.tasks.listView.clickRecord(1);

		// Clicking Related Subpanel filter
		sugar.tasks.recordView.getControl("relatedSubpanelFilter").click();

		// Verifying that MyTasks is not listed in Related Dropdown
		// TODO:VOOD-1463
		new VoodooControl("div", "id", "select2-drop").assertContains(filterData.get("filter"), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
