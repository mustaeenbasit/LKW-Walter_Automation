package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_29125 extends SugarTest {
	DataSource leadsData;

	public void setup() throws Exception {
		leadsData = testData.get(testName + "_" + sugar.leads.moduleNamePlural);
		sugar.leads.api.create(leadsData);
		sugar.login();
	}

	/**
	 * Verify that dashlets are working fine if all dashlets are based on the same type of filter 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_29125_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource dashboardData = testData.get(testName);

		// Define Controls for Dashlets
		// TODO: VOOD-960
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl dashletTitleCtrl = new VoodooControl("input", "css", ".edit.fld_label input");
		VoodooControl selectModuleCtrl = new VoodooSelect("div", "css", ".fld_module .select2-container");
		VoodooSelect displayRowsCtrl = new VoodooSelect("div", "css", ".edit.fld_limit div");
		VoodooSelect defaultDataFilterCtrl = new VoodooSelect("i", "css", ".search-filter .select2 i");
		VoodooControl createFilterCtrl = new VoodooControl("div", "css", ".select2-drop-active.search-filter-dropdown li:nth-child(1) .select2-icon-prepend");
		VoodooSelect filterFieldCtrl = new VoodooSelect("div", "css", ".filter-body div[data-filter='field']");
		VoodooSelect filterOperatorCtrl = new VoodooSelect("div", "css", ".filter-body div[data-filter='operator']");
		VoodooSelect filterValueCtrl = new VoodooSelect("div", "css", ".filter-body div[data-filter='value']");
		VoodooControl filterNameCtrl = new VoodooControl("input", "css", ".filter-options .filter-header .controls input");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");

		// Go to Home -> My Dashboard -> Edit
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();

		for(int i = 0; i < 3; i++){
			// Add a Dashlet
			sugar.home.dashboard.addRow();
			sugar.home.dashboard.addDashlet((4+i), 1);

			// Add 3 dashlets
			// For first dashlet, add Listview -> Leads -> Display Rows: 20 Filter: Leads -> Status -> Is Any Of -> New
			// For second dashlet, add Listview -> Leads -> Display Rows: 5 Filter: Leads -> Status -> Is Any Of -> Converted
			// For third dashlet, add Listview -> Leads -> Display Rows: 20 Filter: Leads -> Status -> Is Any Of -> In Process

			// Select "List View"
			dashletSearchCtrl.set(dashboardData.get(0).get("listview"));
			dashletSelectCtrl.click();

			// Select Module -> Leads, Display Rows and Name it
			selectModuleCtrl.set(sugar.leads.moduleNamePlural);
			dashletTitleCtrl.set(dashboardData.get(i).get("dashletTitle"));
			displayRowsCtrl.set(dashboardData.get(i).get("displayRows"));

			// Filter: Leads -> Status -> Is Any Of -> New/Converted/In Process
			defaultDataFilterCtrl.click();
			createFilterCtrl.click();
			filterFieldCtrl.set(dashboardData.get(0).get("filterField"));
			filterOperatorCtrl.set(dashboardData.get(0).get("filterOperator"));
			filterValueCtrl.set(dashboardData.get(i).get("filterValue"));
			filterNameCtrl.set(dashboardData.get(i).get("filterName"));

			// Save
			saveBtnCtrl.click();
			VoodooUtils.waitForReady();
		}

		// Save the home page
		// TODO: VOOD-1645
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Log out and Log back in
		sugar.logout();
		sugar.login();

		// Verify that the dashlets show correct data and each dashlet should show only one status value for all records
		// TODO: VOOD-960
		for(int i = 0; i < leadsData.size(); i++) {
			new VoodooControl("h4", "css", ".row-fluid.sortable:nth-of-type(" + (4+i) + ") .dashlet-container:nth-of-type(1) .dashlet-header h4").assertEquals(dashboardData.get(i).get("dashletTitle"), true);
			new VoodooControl("a", "css", ".row-fluid.sortable:nth-of-type(" + (4+i) + ") .dashlet-container:nth-of-type(1) .fld_full_name a").assertContains(leadsData.get(i).get("firstName") + " " + leadsData.get(i).get("lastName"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}