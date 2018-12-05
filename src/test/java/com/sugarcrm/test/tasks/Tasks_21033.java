package com.sugarcrm.test.tasks;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21033 extends SugarTest {
	public void setup() throws Exception {
		sugar.tasks.api.create();
		sugar.login();
	}

	/**
	 * Duplicate Task_Verify that duplicating a task can be canceled
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21033_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to Tasks's record view
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		
		// Click 'Copy' button
		sugar.tasks.recordView.copy();
		
		// Modify the task and Click 'Cancel' button
		sugar.tasks.createDrawer.getEditField("subject").set(testName);
		sugar.tasks.createDrawer.cancel();
		
		// Verify that task detail view is displayed as current page
		sugar.tasks.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(sugar.tasks.moduleNameSingular, true);
		sugar.tasks.recordView.getDetailField("subject").assertEquals(sugar.tasks.getDefaultData().get("subject"), true);
		
		// Go to tasks list view and verify that canceled duplicating task is not displayed in the list view
		sugar.tasks.navToListView();
		Assert.assertTrue("Record count in tasks Listview is not equal to 1", sugar.tasks.listView.countRows() == 1);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}