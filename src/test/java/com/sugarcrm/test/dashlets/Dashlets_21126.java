package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21126 extends SugarTest {
	public void setup() throws Exception {
		
		// Initializing test data
		FieldSet dashboardData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		TaskRecord myTaskRecord = (TaskRecord) sugar().tasks.api.create();

		// Login as a valid user
		sugar().login();

		// Existing task related an existing account record needed.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural).linkExistingRecord(myTaskRecord);

		// Choose "My Dashboard"
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboard");
		if(!dashboardTitle.queryContains(dashboardData.get("myDashboard"), true)) {
			sugar().accounts.dashboard.chooseDashboard(dashboardData.get("myDashboard"));
		}

		// Add 'Active Tasks' dashlet in RHS dashlet
		sugar().accounts.dashboard.edit();
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(6,1);

		// TODO: VOOD-960
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashboardData.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "div[data-voodoo-name='dashletconfiguration-headerpane'] .detail.fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save 'My Dashboard' with the new dashlet
		new VoodooControl("a", "css", ".dashboard-pane .fld_save_button a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * User close a task in Dashlets by clicking “X” icon and select “Cancel”
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21126_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the task on Active Tasks' RHS dashlet - 2nd Tab
		// TODO: VOOD-960
		VoodooControl activeTasksDashletCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='active-tasks']");
		new VoodooControl("a", "css", activeTasksDashletCtrl.getHookString() + " .dashlet-tab:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Click "Completed" button
		// TODO: VOOD-960
		VoodooControl cancelButton =  new VoodooControl("i", "css", activeTasksDashletCtrl.getHookString() + " .tab-content li .pull-right .fa-times-circle");
		cancelButton.scrollIntoView();
		cancelButton.click();
		VoodooUtils.waitForReady();
		
		// Click "Cancel" button on the pop-up window. 
		sugar().alerts.getWarning().cancelAlert();

		// Verify that record is displayed on Active Tasks' RHS dashlet
		// TODO: VOOD-960
		new VoodooControl("div", "css", activeTasksDashletCtrl.getHookString() + " .tab-content").assertContains(sugar().tasks.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}