package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_30182 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that newly added Dashlet should display after removing a saved 'Dashlet' in Opportunities. 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_30182_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Go to Opportunities -> My Dashboard
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.dashboard.chooseDashboard(customData.get("dashboardHeader"));

		// Select Edit link from action drop-down to edit 'My Dashboard'.
		sugar().opportunities.dashboard.edit();

		// Selecting the 'Configure' cog icon on Pipeline Dashlet 
		// TODO: VOOD-960 Dashlet selection
		new VoodooControl("i", "css", ".dashlet-header .dropdown-toggle i").click();

		// Remove dashlet
		new VoodooControl("a", "css", ".dropdown-menu.left li:nth-child(3) span").click();

		// Click Confirm on pop-up displayed and click Save.
		sugar().alerts.getAlert().confirmAlert();

		// TODO: VOOD-1645 - Need to update method(s) in 'Dashboard.java' for Edit page.
		VoodooControl dashboardSaveBtn = new VoodooControl("a", "css", "div.preview-headerbar .btn-toolbar .fld_save_button a");
		dashboardSaveBtn.click();

		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl dashboardHeader = new VoodooControl("a", "css", "[data-type='dashboardtitle'] .dropdown-toggle");
		VoodooControl dashletHeader = new VoodooControl("h4", "css", ".dashlet-header h4");

		// Verify that Pipeline dashlet has been removed from 'My Dashboard'.
		dashboardHeader.assertEquals(customData.get("dashboardHeader"), true);
		dashletHeader.assertVisible(false);

		// Again select Edit link from action drop-down to edit 'My Dashboard'.
		sugar().opportunities.dashboard.edit();

		// Adding a new row in Dashboard
		sugar().opportunities.dashboard.addRow();
		sugar().opportunities.dashboard.addDashlet(1,1);

		// Select List View
		// TODO: VOOD-960 - Dashlet selection, VOOD-670 - More Dashlet Support
		VoodooControl searchDashlet = new VoodooControl("input", "css", ".span4.search");
		searchDashlet.set(customData.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Save the Dashlet
		new VoodooControl("a", "css", ".active .fld_save_button a").click();

		// Save the Dashboard
		dashboardSaveBtn.click();

		// Verify that Opportnities List View dashlet is visible on 'My Dashboard'
		dashboardHeader.assertEquals(customData.get("dashboardHeader"), true);
		dashletHeader.assertEquals(customData.get("oppDashletName"), true);

		// Refresh the page
		VoodooUtils.refresh();
		VoodooUtils.waitForReady();

		// Verify that Opportnities List View dashlet is still visible on 'My Dashboard'.
		dashboardHeader.assertEquals(customData.get("dashboardHeader"), true);
		dashletHeader.assertEquals(customData.get("oppDashletName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}