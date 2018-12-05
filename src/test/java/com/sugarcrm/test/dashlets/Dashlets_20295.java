package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20295 extends SugarTest {
	public void setup() throws Exception {
		FieldSet customFS = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Go to accounts recordView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Create a new Dashboard and select active task dashlet
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(customFS.get("dashboardTitle"));
		sugar().opportunities.dashboard.addRow();
		sugar().opportunities.dashboard.addDashlet(1, 1);
		VoodooUtils.waitForReady();

		// TODO: VOOD-670
		// Select Active Tasks Dashlet
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customFS.get("selectActiveTaskDashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("div", "css", ".edit.fld_limit div").set(customFS.get("five"));
		new VoodooControl("a", "css", ".drawer.active .fld_save_button a").click(); // Save dashlet
		VoodooUtils.waitForReady();

		// Save Dash-board
		sugar().accounts.dashboard.save();
	}

	/**
	 * Verify more link shown while there is more records on the tasks dashlets
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20295_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource taskRecords = testData.get(testName+"_taskData");

		// TODO: VOOD-670
		// Create Active tasks records
		VoodooControl todoTabCtrl = new VoodooControl("a", "css", ".dashlet-cell .dashlet-tabs-row .dashlet-tab:nth-child(3) a span");
		for(int i = 0; i < taskRecords.size(); i++) {
			new VoodooControl("span", "css", ".dashlet-cell .fa.fa-plus").click();
			new VoodooControl("a", "css", "[data-dashletaction='createRecord']").click();
			VoodooUtils.waitForReady();

			// Create tasks records
			sugar().tasks.createDrawer.getEditField("subject").set(taskRecords.get(i).get("taskName"));
			sugar().tasks.createDrawer.getEditField("date_start_time").set(sugar().tasks.getDefaultData().get("date_start_time"));
			sugar().tasks.createDrawer.getEditField("date_start_date").set(sugar().tasks.getDefaultData().get("date_start_date"));
			sugar().tasks.createDrawer.save();

			// Verify that A '+' plus sign should be appended to the number of loaded records above "Upcoming" label if more than 5 records, i.e. 5+
			if(i < 5)
				todoTabCtrl.assertContains(""+(i+1)+"", true);
			else
				todoTabCtrl.assertContains(""+(i)+"+", true);
		}

		// Click on "To Do" tab
		todoTabCtrl.click();

		// Verify that A 'More...' link should be shown at the bottom under "Upcoming" tab on Active Tasks dashlets if more than 5 records
		VoodooControl showMoreCtrl = new VoodooControl("button", "css", ".dashlet-cell .block-footer [data-action='show-more']");
		showMoreCtrl.assertExists(true);
		showMoreCtrl.click(); // Click on More link

		// Verify that Click more link will show all the task records under "Upcoming" tab on the active tasks dash lets
		for(int i = 0; i < taskRecords.size(); i++)
			new VoodooControl("a", "css", ".dashlet-cell .tab-pane.active ul li:nth-child("+(i+1)+") p a:nth-child(2)").assertContains(taskRecords.get(i).get("taskName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}