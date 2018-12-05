package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22901 extends SugarTest {
	AccountRecord account;
	TaskRecord task;
	StandardSubpanel tasksSubpanel;
	
	String taskName, accountName;
	
	public void setup() throws Exception {
		sugar().login();
		
		account = (AccountRecord)sugar().accounts.api.create();
		accountName = account.getRecordIdentifier();

		task = (TaskRecord)sugar().tasks.api.create();
		taskName = task.getRecordIdentifier();
		
		account.navToRecord();
		
		tasksSubpanel = sugar().accounts.recordView.subpanels.get("Tasks");

		tasksSubpanel.scrollIntoViewIfNeeded(false);

		tasksSubpanel.clickLinkExisting();
		
		// TODO VOOD-739
		// Select the first Task from the list
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		sugar().alerts.getSuccess().closeAlert();
		
	}

	/**
	 *	22901 Verify that the task detail view related to this account is displayed by clicking the subject link on Tasks sub-panel.
	 *
	 *	@throws Exception
	 */
	@Test
	public void Accounts_22901_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		tasksSubpanel.clickRecord(1);
		
		// Verify that Record View is opened
		sugar().tasks.recordView.getDetailField("subject").assertEquals(taskName, true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
