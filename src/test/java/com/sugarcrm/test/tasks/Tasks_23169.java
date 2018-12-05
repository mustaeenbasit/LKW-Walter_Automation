package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Tasks_23169 extends SugarTest {
	FieldSet defaultTask,taskSubpanelData = new FieldSet();

	public void setup() throws Exception {
		sugar.accounts.api.create();
		defaultTask = sugar.tasks.getDefaultData();
		taskSubpanelData.put("subject", defaultTask.get("subject"));
		taskSubpanelData.put("status", "Completed");

		// Login
		sugar.login();
	}

	/**
	 * Test Case 23169: Verify that a closed task is created by clicking "Close and Create New"
	 * @throws Exception
	 */
	@Test
	public void Tasks_23169_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Account record
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// In Tasks sub panel, click "Create Task" (+ sign)
		StandardSubpanel subTasks = sugar.accounts.recordView.subpanels.get(sugar.tasks.moduleNamePlural);
		subTasks.addRecord();

		// Fill all fields for the task. Save and View.
		sugar.tasks.createDrawer.showMore();
		sugar.tasks.createDrawer.setFields(defaultTask);

		// Save and navigate to the record
		sugar.tasks.createDrawer.save();
		sugar.alerts.getSuccess().closeAlert();
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);

		// Click "Close and Create New" button and then "Cancel" button
		sugar.tasks.recordView.openPrimaryButtonDropdown();
		// TODO: VOOD-607
		new VoodooControl("a", "css", ".fld_record-close-new a").click();
		// Close "Task closed" alert
		sugar.alerts.getSuccess().closeAlert();
		sugar.tasks.createDrawer.showMore();
		sugar.tasks.createDrawer.setFields(defaultTask);
		sugar.tasks.createDrawer.cancel();

		// And go the same account's detail view
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// Only one closed task is displayed on Tasks sub-panel, status is "Completed"
		
		// TODO: SC-4692 - Expanded Records in the subpanel is getting collapsed when navigated back to same record view
		subTasks.scrollIntoViewIfNeeded(false);
		subTasks.expandSubpanel();
		subTasks.scrollIntoViewIfNeeded(false);

		// TODO: VOOD-1424 - Make StandardSubpanel.verify() verify specified value is in correct column.
		subTasks.getDetailField(1, "status").assertEquals("Completed", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}