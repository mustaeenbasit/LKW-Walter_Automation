package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_22778 extends SugarTest {
	StandardSubpanel tasksSubpanel;
	FieldSet dashboardData = new FieldSet();
	
	public void setup() throws Exception {
		// Initializing test data
		dashboardData = testData.get(testName).get(0);
		sugar().leads.api.create();
		TaskRecord myTaskRecord = (TaskRecord) sugar().tasks.api.create();

		// Login as a valid user
		sugar().login();

		// Existing task related an existing account record needed.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		tasksSubpanel = sugar().leads.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		tasksSubpanel.linkExistingRecord(myTaskRecord);

		// Choose "My Dashboard"
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboard");
		if(!dashboardTitle.queryContains(dashboardData.get("myDashboard"), true)) {
			sugar().accounts.dashboard.chooseDashboard(dashboardData.get("myDashboard"));
		}

		// Add 'InActive Tasks' dashlet in RHS dashlet
		sugar().leads.dashboard.edit();
		sugar().leads.dashboard.addRow();
		sugar().leads.dashboard.addDashlet(3,1);

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
	 * Edit Task_Verify that the deferred task is displayed in "Inactive Tasks" dashlet.
	 * @throws Exception
	 */
	@Test
	public void Dashlets_22778_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit the task in Subpanel to deferred
		FieldSet editTaskData =  new FieldSet();
		editTaskData.put("status", dashboardData.get("status"));
		tasksSubpanel.editRecord(1, editTaskData);
		
		// TODO: VOOD-960
		// Open Inactive Tasks Dashlet settings and click refresh.
		new VoodooControl("div", "css", ".dashlet-row li:nth-child(3) i[data-action='loading']").click();
		new VoodooControl("div", "css", ".dashlet-row li:nth-child(3) .dropdown-menu.left li:nth-child(2) a").click();
		
		// Assert that Deferred tab displays "1" as count
		new VoodooControl("a", "css", "div[data-voodoo-name='inactive-tasks'] .active a:nth-child(2)").scrollIntoView();
		new VoodooControl("span", "css", "div[data-voodoo-name='inactive-tasks'] .dashlet-tabs-row div:nth-child(1) .count").assertContains(dashboardData.get("count"), true);
		// Assert task record created is displayed in "Inactive tasks" dashlet's "Deferred" section
		new VoodooControl("a", "css", "div[data-voodoo-name='inactive-tasks'] .active a:nth-child(2)").assertEquals(sugar().tasks.getDefaultData().get("subject"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}