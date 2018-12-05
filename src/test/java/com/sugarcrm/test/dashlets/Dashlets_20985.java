package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20985 extends SugarTest {
	AccountRecord myAccountRecord;

	public void setup() throws Exception {
		myAccountRecord = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that creating a task in in-line form on "Active Tasks" dashlet is cancelled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20985_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// Go to an account record detail view. 
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.dashboard.clickCreate();

		// create new dashboard
		sugar().accounts.dashboard.getControl("title").set(ds.get(0).get("dashboard_name"));
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);

		FieldSet dashletSetup = testData.get("env_dashlets_setup").get(0);

		// TODO: VOOD-960 - Dashlet selection 
		//  Add the new dashlet on a dashboard - Active Tasks.
		new VoodooControl("input", "css", ".span4.search").set( dashletSetup.get("activeDashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".pull-right.dropdown .fld_save_button .btn.btn-primary").click();
		VoodooUtils.waitForReady();
		sugar().accounts.dashboard.save();

		// Inside the dashlet click "+" and "Create Task"
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("span", "css", ".dashlet-header .btn-toolbar.pull-right .fa.fa-plus").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".layout_Home [data-dashletaction='createRecord']").click();
		VoodooUtils.waitForReady();

		// Enter all mandatory fields for new task.
		sugar().tasks.createDrawer.getEditField("subject").set(testName);

		// verify that in the field "Related to" account name is set automatically;
		sugar().tasks.createDrawer.getEditField("relRelatedToParentType").assertEquals(sugar().accounts.moduleNameSingular, true);
		sugar().tasks.createDrawer.getEditField("relRelatedToParent").assertEquals(myAccountRecord.getRecordIdentifier(), true);

		// cancel the record
		sugar().tasks.createDrawer.cancel();

		// TODO: VOOD-963 - Some dashboard controls are needed
		VoodooControl activeTab = new VoodooControl("div", "css", ".tab-pane.active");
		// Verify that no matching task record is displayed on 'Active Tasks' RHS dashlet 
		for (int i = 0; i < 3; i++) {
			new VoodooControl("a", "css", "div.dashlet-tabs.tab3 .dashlet-tab [data-index='"+i+"']").click();
			activeTab.assertContains(ds.get(0).get("dashboard_name"), false);
		}

		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboard");

		// Chose "My Dashboard"
		if(!dashboardTitle.queryContains(ds.get(1).get("dashboard_name"), true)) {
			sugar().accounts.dashboard.chooseDashboard(ds.get(1).get("dashboard_name"));
		}

		// Verify that no matching task record is displayed on "HISTORY"dashlet
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("h4", "css", "div.dashboard-pane li:nth-child(5) .dashlet-title.ui-draggable-handle").scrollIntoView();
		for (int i = 0; i < 3; i++) {
			new VoodooControl("a", "css", "div.dashlet-tabs.tab3 .dashlet-tab [data-index='" + i + "']").click();
			activeTab.assertContains(ds.get(0).get("dashboard_name"), false);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}