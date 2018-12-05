package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22900 extends SugarTest {
	TaskRecord myTask;

	public void setup() throws Exception {
		FieldSet customFS = testData.get(testName).get(0);
		sugar().accounts.api.create();
		myTask = (TaskRecord) sugar().tasks.api.create();
		sugar().login();

		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Existing task related an existing account record needed.
		StandardSubpanel taskSubPanel = sugar().accounts.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		taskSubPanel.scrollIntoViewIfNeeded(false);
		taskSubPanel.linkExistingRecord(myTask);

		// Add the Active tasks and Inactive tasks dashboards to RHS of an account record
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(customFS.get("dashBoardName"));
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);

		// TODO: VOOD-960 - Dashlet selection
		VoodooControl searchDashlet = new VoodooControl("input", "css", ".span4.search");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl dashletSaveBtnCtrl = new VoodooControl("a", "css", "#drawers .fld_save_button a:not(.hide)");
		searchDashlet.set(customFS.get("activeDashboard"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady();
		dashletSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Add inactive task dashlet
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(2,1);
		searchDashlet.set(customFS.get("inactiveDashboard"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady();
		dashletSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Save the Dashboard
		sugar().accounts.dashboard.save();
	}

	/**
	 * Search Account_Verify that condition can be still displayed after searching with accounts name 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22900_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-963 - Some dashboard controls are needed
		// Click "Completed" button in front of a task on sub-panel Active Tasks dashlet
		new VoodooControl("a", "css", "[data-voodoo-name='active-tasks'] .dashlet-tabs-row div:nth-child(2) a").click();
		new VoodooControl("i", "css", "[data-voodoo-name='active-tasks'] .tab-content .fa.fa-times-circle").click();
		sugar().alerts.confirmAllWarning();

		// Click configuration Icon on Inactive dashlet
		new VoodooControl("i", "css", "ul.dashlet-row li.row-fluid:nth-child(2) .dropdown-toggle.btn.btn-invisible .fa.fa-cog").click();
		new VoodooControl("li", "css", "ul.dashlet-row li.row-fluid:nth-child(2) .dropdown-menu.left li:nth-child(2)").click();

		// Click on Completed tab
		new VoodooControl("a", "css", "[data-voodoo-name='inactive-tasks'] .dashlet-tabs-row div:nth-child(2) a").click();

		// Verify that the closed task record is displayed on "Inactive" dashlet under "Completed" tab
		new VoodooControl("a", "css", "[data-voodoo-name='inactive-tasks'] .tab-pane.active li p a:nth-child(2)").assertContains(myTask.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}