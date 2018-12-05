package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_29065 extends SugarTest {
	DataSource dashboardData;

	public void setup() throws Exception {
		dashboardData = testData.get(testName);
		sugar.login();
	}

	/**
	 * Verify that filter in Home Dashboard created by a user is not available to use by other user 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_29065_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls for Dashlets
		// TODO: VOOD-960
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl selectModuleCtrl = new VoodooSelect("div", "css", ".fld_module .select2-container");
		VoodooSelect defaultDataFilterCtrl = new VoodooSelect("i", "css", ".search-filter .select2 i");
		VoodooControl createFilterCtrl = new VoodooControl("div", "css", ".select2-drop-active.search-filter-dropdown li:nth-child(1) .select2-icon-prepend");
		VoodooControl filterNameCtrl = new VoodooControl("input", "css", ".filter-options .filter-header .controls input");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");

		// Go to Home -> My Dashboard -> Create -> Add Row -> Add a Dashlet.
		sugar.navbar.clickModuleDropdown(sugar.home);
		sugar.home.dashboard.clickCreate();
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(1, 1);

		// Add a dashlet -> select "List View"
		dashletSearchCtrl.set(dashboardData.get(0).get("listView"));
		dashletSelectCtrl.click();

		// Module: Opportunities 
		selectModuleCtrl.set(sugar.opportunities.moduleNamePlural);

		// Filter: Assigned to = Administrator, Status is any of ( New, In progress), Filter name: my open opp
		// TODO: VOOD-960
		defaultDataFilterCtrl.click();
		createFilterCtrl.click();
		for(int i = 0; i < dashboardData.size(); i++) {
			new VoodooSelect("div", "css", ".filter-body:nth-child(" + (i+1) + ") div[data-filter='field']").set(dashboardData.get(i).get("filterField"));
			new VoodooSelect("div", "css", ".filter-body:nth-child(" + (i+1) + ") div[data-filter='operator']").set(dashboardData.get(0).get("filterOperator"));
			VoodooSelect filterValueCtrl = new VoodooSelect("div", "css", ".filter-body:nth-child(" + (i+1) + ") div[data-filter='value']");
			if(i < dashboardData.size() - 1) {
				new VoodooControl("i", "css", ".filter-body:nth-child(" + (i+1) + ") .filter-actions button[data-action='add'] i").click();
				filterValueCtrl.set(dashboardData.get(i).get("filterValue"));
			} else {
				filterValueCtrl.clickSearchForMore();
				sugar.users.searchSelect.selectRecord(3);
				// TODO: VOOD-1162
				new VoodooControl("a", "css", ".fld_select_button a").click();

			}
		}
		filterNameCtrl.set(dashboardData.get(0).get("filterName"));

		// Save
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Save the home page
		sugar.home.dashboard.getControl("title").set(testName);
		sugar.home.dashboard.save();

		// Log out and Log in as QAUser
		sugar.logout();
		sugar.login(sugar.users.getQAUser());

		// Create the dashlet in the Home page dashboard
		// Go to Home -> My Dashboard -> Edit
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();

		// Add a Dashlet
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);

		// Set the dashlet as listview with module opportunities
		dashletSearchCtrl.set(dashboardData.get(0).get("listView"));
		dashletSelectCtrl.click();
		selectModuleCtrl.set(sugar.opportunities.moduleNamePlural);

		// Set the filter 
		defaultDataFilterCtrl.click();

		// Verify that the filter 'my open opp' should not appear
		// TODO: VOOD-960
		new VoodooControl("ul", "id", "select2-drop").assertContains(dashboardData.get(0).get("filterName"), false);
		new VoodooControl("div", "css", ".select2-drop-active.search-filter-dropdown li:nth-child(4) .select2-icon-prepend").click(); // Need to click on the filter to remove focus

		// Click on cancel button
		// TODO: VOOD-963
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_cancel_button a").click();
		new VoodooControl("a", "css", ".detail.fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}