package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_17549 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.tasks.api.create();
		sugar.login();	
	}

	/**
	 * Verify close button should NOT be shown in the edit mode for Tasks
	 * @throws Exception
	 */	
	@Test
	public void Tasks_17549_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar.tasks.navToListView();
		sugar.tasks.listView.clickRecord(1);
		sugar.tasks.recordView.edit();

		// Verify "Close" and "Close and New" buttons should NOT be shown in the task edit view
		sugar.tasks.recordView.assertContains(customData.get("closeButton"), false);
		sugar.tasks.recordView.assertContains(customData.get("closeNewButton"), false);

		// Verify "Cancel" and "Save" buttons should be shown in the task edit view
		sugar.tasks.recordView.getControl("cancelButton").assertVisible(true);
		sugar.tasks.recordView.getControl("saveButton").assertVisible(true);
		// Cancel
		sugar.tasks.recordView.cancel();		

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}