package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_22902 extends SugarTest {

	public void setup() throws Exception {
		// create account and task, and related tasks to account
		sugar().accounts.api.create();
		sugar().tasks.api.create();

		// Login as admin
		sugar().login();

		// Navigate to the task record view and relate it to the account
		sugar().navbar.navToModule(sugar().tasks.moduleNamePlural);
		sugar().tasks.listView.clickRecord(1);
		sugar().tasks.recordView.edit();
		sugar().tasks.recordView.getEditField("relRelatedToParent").set(sugar().accounts.getDefaultData().get("name"));
		sugar().tasks.recordView.save();

		// Navigate to the account record view
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);
	}

	/**
	 * Verify that updating status of the task related to the account is canceled
	 * @throws Exception
	 */
	@Test
	public void Accounts_22902_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet taskStatus = testData.get(testName).get(0);			
		StandardSubpanel taskSub = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);

		// Expand Subpanel
		taskSub.expandSubpanel();
		
		// Edit a task record in the subpanel
		taskSub.editRecord(1);

		// Change the status
		taskSub.getEditField(1, "status").set(taskStatus.get("statusnew"));

		// Assert that the status is shown as what has been set
		taskSub.getEditField(1, "status").assertEquals(taskStatus.get("statusnew"), true);

		// Cancel the edit action
		taskSub.cancelAction(1);

		// Assert that the status of the task has not been changed
		taskSub.getDetailField(1, "status").assertEquals(taskStatus.get("statusold"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}