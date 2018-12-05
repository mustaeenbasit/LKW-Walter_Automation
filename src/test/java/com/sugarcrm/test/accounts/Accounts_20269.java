package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_20269 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// TODO: VOOD-960 - Dashlet selection 
		VoodooControl searchDashlet = new VoodooControl("input", "css", ".span4.search");
		VoodooControl selectDashletCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl dashletSaveBtnCtrl = new VoodooControl("a", "css", "#drawers .fld_save_button a:not(.hide)");

		// Add the Active tasks and Inactive tasks dashboards to RHS of an account record
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(customData.get("dashBoardName"));
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);

		// Setting Active Tasks dashlet 
		searchDashlet.set(customData.get("firstDashlet"));
		VoodooUtils.waitForReady();
		selectDashletCtrl.click();
		VoodooUtils.waitForReady();
		dashletSaveBtnCtrl.click();
		VoodooUtils.waitForReady();
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(2,1);

		// Setting Inactive Tasks dashlet 
		searchDashlet.set(customData.get("secondDashlet"));
		VoodooUtils.waitForReady();
		selectDashletCtrl.click();
		VoodooUtils.waitForReady();
		dashletSaveBtnCtrl.click();
		VoodooUtils.waitForReady();
		sugar().accounts.dashboard.save();
	}

	/**
	 * Verify tasks with due date > todays date are shown on the upcoming tab on the active tasks dashboard
	 * @throws Exception
	 */
	@Test
	public void Accounts_20269_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating multiple tasks
		for (int i = 0 ; i < Integer.parseInt(customData.get("count")) ; i++) {

			// Clicking on '+' button on Dashlet Toolbar to create task
			// TODO: VOOD-960 -  Dashlet selection 
			new VoodooControl("a", "css", ".dashlet-toolbar .dropdown-toggle").click();

			// clicking the 'Create Task' option
			new VoodooControl("a", "css", "[data-dashletaction = 'createRecord']").click();
			sugar().tasks.createDrawer.getEditField("subject").set(customData.get("subject") + "_" + i);

			// Setting task date in future (i.e. due date > todays date)
			sugar().tasks.createDrawer.getEditField("date_due_date").set("04/" + i + "1/2025");
			sugar().tasks.createDrawer.save();
			if(sugar().alerts.getSuccess().queryVisible()) {
				sugar().alerts.getSuccess().closeAlert();
			}
		}

		// Verify number of tasks shown on the "Upcoming" tab to indicate the number of upcoming tasks
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("div", "css", ".dashlet-tab:nth-child(2)").click();
		new VoodooControl("span", "css", ".dashlet-tab:nth-child(2) .count").assertContains(customData.get("count"), true);

		// Verify newly created Tasks shown in "Upcoming" tab, ordered by date entered ASC
		for (int i = 0 ; i < Integer.parseInt(customData.get("count")) ; i++) {
			// TODO: VOOD-963 - Some dashboard controls are needed
			new VoodooControl("a", "css", ".dashlet-content li:nth-child(" + (i + 1) + ") a:nth-child(2)").assertContains(customData.get("subject") + "_" + i, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}