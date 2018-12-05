package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23350 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		sugar().cases.api.create();
		customData = testData.get(testName).get(0);
		sugar().login();

		// Navigating to Case Record View
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Add New Dashboard "Test"
		sugar().cases.dashboard.clickCreate();
		sugar().cases.dashboard.getControl("title").set(customData.get("dashboard_Title"));

		// Add New row for Active Tasks
		sugar().cases.dashboard.addRow();
		sugar().cases.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("a", "css", ".list-view .list.fld_title a").click();

		// TODO: VOOD-1004 - Library support need to create dashlet
		VoodooControl save = new VoodooControl("a", "css", "#drawers .fld_save_button a");
		save.click();
		VoodooUtils.waitForReady();

		// Add New row for Inactive Tasks
		sugar().cases.dashboard.addRow();
		sugar().cases.dashboard.addDashlet(2, 1);
		new VoodooControl("a", "css", ".list-view .dataTable tbody tr:nth-child(3) .list.fld_title a").click();
		save.click();
		VoodooUtils.waitForReady();

		// Save dashboard
		sugar().cases.dashboard.save();
	}

	/**
	 * Create Task_Verify that a task for case is moved to "Inactive Tasks" dashlet when its "Status" is set as "Completed".
	 * @throws Exception
	 */
	@Test
	public void Cases_23350_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960 - Dashlet selection
		// Go to Active tasks dashlet and click + icon to create task
		new VoodooControl("i", "css", ".dashlet-row li .dashlet-header .fa.fa-plus").click();
		new VoodooControl("a", "css", ".dashlet-row li .dashlet-header .dropdown-menu li").click();
		sugar().tasks.createDrawer.getEditField("subject").set(customData.get("task_Name"));
		sugar().tasks.createDrawer.getEditField("status").set(customData.get("status"));
		sugar().tasks.createDrawer.save();

		// TODO: VOOD-1004 - Library support need to create dashlet
		// Refresh the page OR Refresh "Inactive Tasks" dashlet by refresh link available on dashlet. (As per TY-250)
		new VoodooControl("i", "css", ".dashlet-row li:nth-child(2) .dashlet-cell .dashlet-header .btn-group .fa.fa-cog").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", ".dashlet-row li:nth-child(2) .dashlet-cell .dashlet-header .btn-group .dropdown-menu li:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-960 - Dashlet selection
		// Verifying taskName shown on the "Completed" tab is same as of Task created through Active Tasks.
		new VoodooControl("span", "css", ".dashlet-tabs.tab2 div:nth-child(2) span").click();;
		new VoodooControl("div", "css",".tab-content div a:nth-child(2)").assertContains(customData.get("task_Name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}