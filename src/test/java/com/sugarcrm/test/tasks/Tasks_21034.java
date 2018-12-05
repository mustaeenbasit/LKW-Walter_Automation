package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21034 extends SugarTest {
	public void setup() throws Exception {
		sugar.tasks.api.create();
		sugar.login();	
	}

	/**
	 * Delete Task_Verify that deleting a task from detail view can be canceled.
	 * 
	 * @throws Exception
	 */	
	@Test
	public void Tasks_21034_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Go to Task's record view
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		
		// Click "Delete" button
		sugar.tasks.recordView.delete();
		
		// Click "Cancel" button in pop-up dialog box
		sugar.alerts.cancelAllWarning();
		
		// Verify that Task record view is still displayed as current page
		sugar.tasks.recordView.assertVisible(true);
		sugar.tasks.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(sugar.tasks.moduleNameSingular, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}