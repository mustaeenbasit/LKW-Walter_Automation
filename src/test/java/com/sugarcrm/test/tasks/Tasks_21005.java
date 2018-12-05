package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21005 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar.tasks.api.create(fs);
		sugar.login();
	}

	/**
	 * Verify that the task's status isn't set back to default value when edit task record.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21005_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Tasks" link in navigation shortcuts.
		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);
		
		// Click the Gear icon and mark the "Status" field on to show it in list view.
		sugar.tasks.listView.toggleHeaderColumn("status");
		
		// Inline edit a task record which status field is filled
		sugar.tasks.listView.editRecord(1);
		sugar.tasks.listView.getEditField(1, "status").scrollIntoViewIfNeeded(false);
		
		// Verify that task's status is not changed. (Not set back to default value)
		sugar.tasks.listView.getEditField(1, "status").assertEquals(fs.get("status"), true);
		
		// save the edited record with same values
		sugar.tasks.listView.saveRecord(1);
		
		// Verify that task's status is not changed. (Not set back to default value)
		sugar.tasks.listView.getDetailField(1, "status").assertEquals(fs.get("status"), true);
		
		// Click agin the Gear icon and mark the "Status" field in it's default state 
		sugar.tasks.listView.toggleHeaderColumn("status");
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}