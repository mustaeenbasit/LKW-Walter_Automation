package com.sugarcrm.test.projects;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Projects_27764 extends SugarTest {
	FieldSet data;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		data = testData.get(testName).get(0);
		sugar.login();

		// Enable Projects module via Admin > Display Modules and Subpanels
		sugar.admin.enableModuleDisplayViaJs(sugar.projects);
		sugar.alerts.waitForLoadingExpiration(20000);

		// TODO: VOOD-1362
		// sugar.projects.api.create();
		// create a project record
		sugar.projects.create();

		// TODO: VOOD-1363
		// create project task records related to the project created 
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "project_task_submit_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#project_task_row_1 > td:nth-child(3)").doubleClick();
		new VoodooControl("input", "css", "#project_task_row_1 #description_1").set(data.get("project_task1"));
		new VoodooControl("td", "css", "#project_task_row_2 > td:nth-child(3)").click();
		new VoodooControl("td", "css", "#project_task_row_2 > td:nth-child(3)").doubleClick();
		new VoodooControl("input", "css", "#project_task_row_2 #description_2").set(data.get("project_task2"));
		new VoodooControl("a", "id", "saveGridLink").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Project Tasks module can be added to list view dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Projects_27764_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to accounts module listview 
		sugar.accounts.navToListView();

		// add a list view dashlet.
		sugar.accounts.dashboard.clickCreate();
		sugar.accounts.dashboard.addRow();
		sugar.accounts.dashboard.addDashlet(1, 1);

		// TODO: VOOD-1004
		VoodooControl searchDashlet = new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']");
		VoodooControl listViewCtrl = new VoodooControl("a", "css", "[data-original-title='List View'] a");

		searchDashlet.set(data.get("dashlet_name"));
		listViewCtrl.click();

		VoodooSelect selectModule = new VoodooSelect("div", "css", "[data-name='module'] .select2-container");
		VoodooControl saveCtrl = new VoodooControl("a", "css", "#drawers .fld_save_button a");

		// Select Project Tasks as the module. 
		selectModule.set(sugar.projects.moduleNamePlural + " Tasks");
		saveCtrl.click();
		sugar.alerts.waitForLoadingExpiration(20000);
		sugar.accounts.dashboard.getControl("title").set(data.get("dashboard_name"));
		sugar.accounts.dashboard.save();

		// TODO: VOOD-670
		// Verify that the Project Tasks module is successfully added and displayed in list view dashlet 
		VoodooControl task1 = new VoodooControl("div", "css", "[data-original-title='task1']");
		VoodooControl task2= new VoodooControl("div", "css", "[data-original-title='task2']");
		task1.assertEquals(data.get("project_task1"), true);
		task2.assertEquals(data.get("project_task2"), true);

		// navigate to accounts record view
		sugar.accounts.listView.clickRecord(1);
		sugar.accounts.dashboard.clickCreate();
		sugar.accounts.dashboard.addRow();
		sugar.accounts.dashboard.addDashlet(1, 1);

		// TODO: VOOD-1004
		searchDashlet.set(data.get("dashlet_name"));
		listViewCtrl.click();

		// Select Project Tasks as the module. 
		selectModule.set(sugar.projects.moduleNamePlural + " Tasks");
		saveCtrl.click();
		sugar.alerts.waitForLoadingExpiration(20000);
		sugar.accounts.dashboard.getControl("title").set(data.get("dashboard_name"));
		sugar.accounts.dashboard.save();

		// TODO: VOOD-670
		// Verify that the Project Tasks module is successfully added and displayed in list view dashlet on accounts record view
		task1.assertEquals(data.get("project_task1"), true);
		task2.assertEquals(data.get("project_task2"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}