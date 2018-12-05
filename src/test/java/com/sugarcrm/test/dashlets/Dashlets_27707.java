package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_27707 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify the configuration fields are available in "My Activity Stream" dashlet 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_27707_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource dashboardData = testData.get(testName);

		// Go to Home -> My Dashboard
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);

		// Add a Dashlet, which is "My Activity Stream" Dashlet
		sugar.home.dashboard.edit();
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);

		// Select "My Activity Stream" dashlet view.
		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashboardData.get(0).get("myActivityStream"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Define Controls for Dashlets
		// TODO: VOOD-960
		VoodooControl dashletTitleCtrl = new VoodooControl("input", "css", ".edit.fld_label input");
		VoodooControl dashletCtrl = new VoodooControl("div", "css", ".row-fluid.sortable:nth-of-type(4) .dashlet-container:nth-of-type(1)");
		VoodooControl dashletHeaderTitleCtrl = new VoodooControl("div", "css", dashletCtrl.getHookString() + " .dashlet-header");
		VoodooSelect displayRowsCtrl = new VoodooSelect("div", "css", ".edit.fld_limit div");
		VoodooSelect autoRefreshCtrl = new VoodooSelect("div", "css", ".edit.fld_auto_refresh div");
		VoodooSelect defaultDataFilterCtrl = new VoodooSelect("i", "css", ".search-filter .select2 i");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Activities .fld_save_button a");
		VoodooControl cancelBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_cancel_button a");

		// Observe the config options in "My Activity stream" dashlet configuration page
		dashletTitleCtrl.set(testName);
		displayRowsCtrl.set(dashboardData.get(0).get("displayRows"));
		autoRefreshCtrl.set(dashboardData.get(0).get("autoRefresh"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the User can change the title of the dashlet
		dashletHeaderTitleCtrl.assertContains(testName, true);

		// Edit the save Dashlet
		// TODO: VOOD-960
		new VoodooControl("i", "css", dashletCtrl.getHookString() + " .btn-group i").click();
		new VoodooControl("a", "css", dashletCtrl.getHookString() + " .dropdown-menu.left li:nth-child(1)").click();
		VoodooUtils.waitForReady();

		// Verify user can configure the dashlet with the following options
		dashletTitleCtrl.assertEquals(testName, true);
		displayRowsCtrl.assertEquals(dashboardData.get(0).get("displayRows"), true);
		autoRefreshCtrl.assertEquals(dashboardData.get(0).get("autoRefresh"), true);

		// Verify the Filter messages by post, create, link, unlink, update
		defaultDataFilterCtrl.click();
		for(int i = dashboardData.size(); i > 0; i--) {
			if (i == 1) {
				VoodooControl selectFilterCtrl = new VoodooControl("div", "css", ".select2-drop-active.search-filter-dropdown li:nth-child(" + (i) + ") .select2-icon-prepend");
				selectFilterCtrl.assertContains(dashboardData.get(i-1).get("filterOption"), true);
				selectFilterCtrl.click(); // Need to click on the filter to remove focus
			} else
				new VoodooControl("div", "css", ".select2-drop-active.search-filter-dropdown .select2-results li:nth-child(" + (i) + ") .select2-no-icon").assertContains(dashboardData.get(i-1).get("filterOption"), true);
		}

		// Click on cancel button
		// TODO: VOOD-963
		cancelBtnCtrl.click();
		new VoodooControl("a", "css", ".fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}