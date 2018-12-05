package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21165 extends SugarTest {
	public void setup() throws Exception {
		sugar.tasks.api.create();
		sugar.login();
	}

	/**
	 * New action dropdown list in task detail view page
	 * @throws Exception
	 */
	@Test
	public void Tasks_21165_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Tasks Record View 
		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		
		// Asserting the action Dropdown & list options the Action Dropdown list
		VoodooControl actionDropDown =sugar.tasks.recordView.getControl("actionDropDown");
		actionDropDown.assertVisible(true);
		actionDropDown.click();
		sugar.tasks.recordView.getControl("editButton").assertVisible(true);
		sugar.tasks.recordView.getControl("copyButton").assertVisible(true);
		VoodooControl delete = sugar.tasks.recordView.getControl("deleteButton");
		delete.assertVisible(true);
		
		// TODO:VOOD-607
		VoodooControl close_CreateNew = new VoodooControl("a", "css", ".fld_record-close-new.detail a");
		close_CreateNew.assertVisible(true);
		delete.click();
		sugar.alerts.getWarning().confirmAlert();
		sugar.tasks.listView.assertIsEmpty();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}