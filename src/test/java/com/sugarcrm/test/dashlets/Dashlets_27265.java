package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_27265 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify Filter is working properly in list view dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_27265_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource dashboardData = testData.get(testName);

		// Go to Home -> My Dashboard -> Create -> Add Row -> Add a Dashlet.
		sugar.navbar.clickModuleDropdown(sugar.home);
		sugar.home.dashboard.clickCreate();
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(1, 1);

		// Add a dashlet -> select "List View" dashlet view.
		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashboardData.get(0).get("listView"));
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Under list view dashlet configuration page,for Calls module observe "Default Data Filter". 
		new VoodooSelect("div", "css", ".fld_module .select2-container").set(sugar.calls.moduleNamePlural);

		// Verify the predefined Filter option list is displayed, such as Create, All Calls, My Calls, MY Favorites, Recently Created and Recently Viewed
		// TODO: VOOD-1463
		new VoodooControl("i", "css", ".filter-view .select2-choice-type").click();
		for(int i = 0; i < 5; i++) {
			if (i == 0 || i == 2) 
				new VoodooControl("span", "css", ".select2-with-searchbox.search-filter-dropdown .select2-results li:nth-child(" + (i+1) + ") .select2-icon-prepend").assertContains(dashboardData.get(i).get("filterOption"), true);
			else
				new VoodooControl("span", "css", ".select2-with-searchbox.search-filter-dropdown .select2-results li:nth-child(" + (i+1) + ") .select2-no-icon").assertContains(dashboardData.get(i).get("filterOption"), true);
		}

		// Verify user able to select "Create" to define filter(Verify the select field option and click on 'x' sign beside the create option)
		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("span", "css", ".select2-with-searchbox.search-filter-dropdown .select2-results li:nth-child(1) .select2-icon-prepend").click();
		new VoodooControl("span", "css", ".fld_filter_row_name").assertExists(true);
		new VoodooControl("i", "css", ".choice-filter-clickable i").click();

		// Click on cancel button
		new VoodooControl("a", "css", ".detail.fld_cancel_button a").click();
		new VoodooControl("a", "css", ".fld_create_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}