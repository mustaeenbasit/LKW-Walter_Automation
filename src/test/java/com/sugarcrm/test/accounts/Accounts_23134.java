package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_23134 extends SugarTest{
	FieldSet customData = new FieldSet();
	AccountRecord accRecord;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		accRecord = (AccountRecord)sugar().accounts.api.create();
		sugar().login();

		// Go to Accounts record view
		accRecord.navToRecord();

		// Create a Dashboard
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(customData.get("dashboard_title"));
		sugar().accounts.dashboard.addRow();
		sugar().dashboard.addDashlet(1,1);

		// TODO: VOOD-960 - Dashlet selection 
		// Select Active task
		new VoodooControl("a", "css", ".list-view .list.fld_title a").click();
		VoodooControl saveCtrl = new VoodooControl("a", "css", "#drawers .fld_save_button a");
		saveCtrl.click();

		// Add new row for inactive task
		sugar().accounts.dashboard.addRow();
		sugar().dashboard.addDashlet(2,1);

		// TODO: VOOD-960 - Dashlet selection 
		// Select Inactive task
		VoodooControl inactiveLinkCtrl = new VoodooControl("a", "css", ".list-view .dataTable tbody tr:nth-child(15) .list.fld_title a");
		inactiveLinkCtrl.scrollIntoViewIfNeeded(false);
		inactiveLinkCtrl.click();
		saveCtrl.click();
		sugar().accounts.dashboard.save();
	}

	/**
	 * Verify that a completed task is created for an related account record in full form from "active tasks" dashlet.
	 * @throws Exception;
	 */
	@Test
	public void Accounts_23134_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960 - Dashlet selection 
		// Go to Active tasks dashlet and click + icon to create task
		new VoodooControl("i", "css", ".dashlet-row li .dashlet-header .fa.fa-plus").click();
		new VoodooControl("a", "css", ".dashlet-row li .dashlet-header .dropdown-menu li").click();
		VoodooUtils.waitForReady();

		// Fill in the required fields for the task, set the status to Completed, and click Save.
		sugar().tasks.createDrawer.getEditField("subject").set(customData.get("task_name"));
		sugar().tasks.createDrawer.getEditField("status").set(customData.get("status"));
		sugar().tasks.createDrawer.save();

		// Go to Accounts record view
		accRecord.navToRecord();

		// TODO: VOOD-960 - Dashlet selection 
		// Expected result: The newly created task for the account is displayed on "inactive task" dashlet.
		new VoodooControl("div", "css", ".dashlet-tabs.tab2 .dashlet-tab:nth-of-type(2)").click();
		new VoodooControl("a", "css", ".unstyled li p a:nth-of-type(2)").assertContains(customData.get("task_name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}