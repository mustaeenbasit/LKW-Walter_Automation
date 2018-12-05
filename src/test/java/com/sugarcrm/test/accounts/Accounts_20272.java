package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_20272 extends SugarTest {
	FieldSet myData = new FieldSet();

	public void setup() throws Exception {
		myData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Go to Accounts Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Add the Active tasks and Inactive tasks dashboards to RHS of an account record
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(myData.get("dashBoardName"));
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1,1);

		// TODO: VOOD-960 - Dashlet selection 
		VoodooControl searchDashlet = new VoodooControl("input", "css", ".span4.search");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl dashletSaveBtnCtrl = new VoodooControl("a", "css", "#drawers .fld_save_button a:not(.hide)");

		// Add Active Task dashlet
		searchDashlet.set(myData.get("firstDashboard"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady();
		dashletSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Add Inactive Task dashlet
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(2,1);
		searchDashlet.set(myData.get("secondDashboard"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady();
		dashletSaveBtnCtrl.click();
		VoodooUtils.waitForReady();
		sugar().accounts.dashboard.save();
	}

	/**
	 * Verify tasks with status 'Deferred' are shown on the Deferred tab under inactive tasks dashboard 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_20272_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Active tasks dashlet and click + icon to create several Tasks with status 'Deferred'
		for (int i = 0; i < 3; i++) {
			// TODO: VOOD-960 - Dashlet selection
			new VoodooControl("span", "css", ".row-fluid > li div span a span").click();
			new VoodooControl("a", "css", "[data-dashletaction = 'createRecord']").click();
			sugar().tasks.createDrawer.getEditField("subject").set(myData.get("subject") + i);
			sugar().tasks.createDrawer.getEditField("date_start_date").set("04/" + i + "1/2015");
			sugar().tasks.createDrawer.getEditField("status").set(myData.get("status"));
			sugar().tasks.createDrawer.save();
			if(sugar().alerts.getSuccess().queryVisible()) {
				sugar().alerts.getSuccess().closeAlert();
			}
		}

		// Refresh the page
		VoodooUtils.refresh();

		// Verify number of tasks shown on the "To Do" tab to indicate the number of todo tasks
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("a", "css", ".dashlet-tabs.tab2 div div a").click();
		VoodooUtils.waitForReady(30000); // Extra wait needed
		new VoodooControl("span", "css", ".dashlet-tabs.tab2 div div a .count").assertContains("3", true);

		// Verify newly created Tasks shown under "To Do" tab under Active Tasks dashlet, ordered by date modified DESC
		for (int i = 0; i < 3; i++) {
			// TODO: VOOD-963 - Some dashboard controls are needed
			new VoodooControl("a", "css", ".dashlet-content li:nth-child(" + (i + 1) + ") a:nth-child(2)").assertContains(myData.get("subject") + (2 - i), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}