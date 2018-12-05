package com.sugarcrm.test.dashlets;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20979 extends SugarTest {
	FieldSet dashboardData = new FieldSet();
	TaskRecord myTaskRecord;
	StandardSubpanel tasksSubpanel;

	public void setup() throws Exception {
		dashboardData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		myTaskRecord = (TaskRecord) sugar().tasks.api.create();

		// Login as a valid user
		sugar().login();

		// Existing task related an existing account record needed.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		tasksSubpanel =  sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		tasksSubpanel.linkExistingRecord(myTaskRecord);		

		// Choose "My Dashboard"
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboard");;
		if(!dashboardTitle.queryContains(dashboardData.get("myDashboard"), true)) {
			sugar().accounts.dashboard.chooseDashboard(dashboardData.get("myDashboard"));
		}

		// Add 'Active Tasks' dashlet in RHS dashlet
		sugar().accounts.dashboard.edit();
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(6,1);

		// TODO: VOOD-960 - Dashlet selection
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
	 * Verify that task is removed from "Planned ACTIVITIES" dashlet by clicking "Unlink" button.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20979_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to a task subject link on Active Tasks' RHS dashlet
		// TODO: VOOD-960 - Dashlet selection
		VoodooControl activeTasksDashletCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='active-tasks']");
		new VoodooControl("a", "css", activeTasksDashletCtrl.getHookString() + " .dashlet-tab:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Click "Unlink" button
		// TODO: VOOD-976 - need lib support of RHS on record view
		VoodooControl unlinkBtnCtrl = new VoodooControl("i", "css", activeTasksDashletCtrl.getHookString() + " .tab-content li .pull-right .fa-chain-broken");
		unlinkBtnCtrl.waitForVisible(); // Extra wait needed
		unlinkBtnCtrl.scrollIntoView();
		unlinkBtnCtrl.click();

		// Verify that Pop-up window "Are you sure you want to unlink task <taskname>?"
		sugar().alerts.getWarning().assertContains(dashboardData.get("warningMessage") + myTaskRecord.getRecordIdentifier(), true);

		// Click "Confirm" button on the pop-up window. 
		sugar().alerts.getWarning().confirmAlert();

		// Verify that No Matching task record is displayed on Active Tasks' RHS dashlet
		// TODO: VOOD-976 - need lib support of RHS on record view
		new VoodooControl("div", "css", activeTasksDashletCtrl.getHookString() + " .tab-content .block-footer").assertContains(dashboardData.get("noData"), true);

		// Refresh the Account record view ( to see record is removed from subpanel )
		VoodooUtils.refresh();

		// Verify that the record is also removed from built-in tasks subpanel for account record
		Assert.assertTrue("The subpanel is not empty", tasksSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}