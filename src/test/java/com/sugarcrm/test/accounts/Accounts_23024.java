package com.sugarcrm.test.accounts;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

import org.junit.Test;

public class Accounts_23024 extends SugarTest {
	StandardSubpanel tasksSubpanel; 	
	
	public void setup() throws Exception {
		// Create account and task
		sugar().accounts.api.create();
		TaskRecord myTask = (TaskRecord) sugar().tasks.api.create();
		sugar().login();

		// link task to account		
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		tasksSubpanel = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		tasksSubpanel.scrollIntoViewIfNeeded(false);
		tasksSubpanel.linkExistingRecord(myTask);
	}

	/**
	 * Verify that the task can be edited through clicking "Edit" action on "Tasks" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23024_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural).hover();
		tasksSubpanel.editRecord(1);
		// TODO: VOOD-1380
		new VoodooControl("input", "css", ".fld_name.edit input").set(testName);
		tasksSubpanel.saveAction(1);

		// Verify that the modified information of the task related to the account is displayed in "Tasks" sub-panel
		FieldSet fs = new FieldSet();
		fs.put("subject", testName);
		tasksSubpanel.verify(1, fs, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}