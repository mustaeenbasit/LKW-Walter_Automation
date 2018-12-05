package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_20268 extends SugarTest{
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();

		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		FieldSet dashletSetup = testData.get("env_dashlets_setup").get(0);

		// Add the Active tasks dashboard to RHS of an account record
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(testName);
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);

		// Setting Active Tasks dashlet 
		// TODO: VOOD-960 - Dashlet selection 
		new VoodooControl("input", "css", ".span4.search").set(dashletSetup.get("activeDashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#drawers .fld_save_button a:not(.hide)").click();
		VoodooUtils.waitForReady();
		sugar().accounts.dashboard.save();
	}

	/**
	 * Verify tasks with due date <= todays date are shown on the due now tab on the active tasks dashboard
	 * @throws Exception;
	 */
	@Test
	public void Accounts_20268_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource tasksData = testData.get(testName);
		int size = tasksData.size();

		VoodooControl dueDate = sugar().tasks.createDrawer.getEditField("date_due_date");
		VoodooControl dueTime = sugar().tasks.createDrawer.getEditField("date_due_time");

		// Creating multiple tasks
		for (int i = 0 ; i < size ; i++) {
			// Clicking on '+' button on Dashlet Toolbar to create task
			// TODO: VOOD-960 - Dashlet selection 
			new VoodooControl("a", "css", ".dashlet-toolbar .dropdown-toggle").click();

			// clicking the 'Create Task' option
			new VoodooControl("a", "css", "[data-dashletaction='createRecord']").click();
			sugar().tasks.createDrawer.getEditField("subject").set(tasksData.get(i).get("subject"));
			dueTime.set(tasksData.get(i).get("dueTime"));
			dueDate.set(tasksData.get(i).get("dueDate"));
			sugar().tasks.createDrawer.save();
			if(sugar().alerts.getSuccess().queryVisible()) {
				sugar().alerts.getSuccess().closeAlert();
			}
		}

		// Verify number of tasks shown on the "Due Now" tab to indicate the number of upcoming tasks
		// TODO: VOOD-960 - Dashlet selection 
		new VoodooControl("div", "css", ".dashlet-tab").click();
		new VoodooControl("span", "css", ".dashlet-tab .count").assertContains(Integer.toString(size), true);

		// Verify newly created Tasks are shown in "Due Now" tab, ordered by date entered ASC
		for (int i = 0 ; i < size ; i++) {
			// TODO: VOOD-963 - Some dashboard controls are needed
			new VoodooControl("a", "css", ".dashlet-content li:nth-child(" + (i + 1) + ") a:nth-child(2)").assertContains(tasksData.get(i).get("subject"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}