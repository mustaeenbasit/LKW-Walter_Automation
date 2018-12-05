package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Tasks_21155 extends SugarTest {

	public void setup() throws Exception {	
		sugar.tasks.api.create();
		sugar.login();					
	}

	/**
	 * The Tasks listview refreshes with the task closed out.
	 * @throws Exception
	 */	
	@Test
	public void Tasks_21155_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		FieldSet taskStatus = testData.get(testName).get(0);
		sugar.tasks.navToListView();
		sugar.tasks.listView.openRowActionDropdown(1);

		// TODO:VOOD-606
		VoodooControl taskCloseCtrl = new VoodooControl("a","css","[name='record-close']");
		taskCloseCtrl.click();

		// Verifying that Success message is shown
		sugar.alerts.getSuccess().assertVisible(true);

		// Verifying that Close Action is not shown in the Row Action Dropdown
		sugar.tasks.listView.openRowActionDropdown(1);
		taskCloseCtrl.assertVisible(false);

		// Including the Status field on the Task ListView
		sugar.tasks.listView.toggleHeaderColumn("status");

		// Verifying the status of Task is "Completed"
		sugar.tasks.listView.getDetailField(1, "status").assertEquals(taskStatus.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}
