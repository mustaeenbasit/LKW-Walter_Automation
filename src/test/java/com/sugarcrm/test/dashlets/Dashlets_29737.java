package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_29737 extends SugarTest {
	FieldSet customData = new FieldSet(); 
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify created dashboard (w/, w/o a dashlet) still appears when user navigates to any other dashboard
	 * @throws Exception
	 */
	@Test
	public void Dashlets_29737_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Initialize test data
		customData = testData.get(testName).get(0);

		// Go to Opportunities -> My Dashboard
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);

		// In RHS pane, Click on "Create" to create a dashboard. Provide dashboard title.
		sugar().opportunities.dashboard.clickCreate();
		sugar().opportunities.dashboard.getControl("title").set(testName);
		
		// Save and Assert created dashboard is available
		saveAndCheckDashboard(testName);
				
		// In RHS pane, Click on "Create" to create a dashboard. Provide dashboard title.
		sugar().opportunities.dashboard.clickCreate();
		sugar().opportunities.dashboard.getControl("title").set(testName + "_2");
		
		// Add a dashlet and then click on "save" to save the created dashboard.
		sugar().opportunities.dashboard.addRow();
		sugar().opportunities.dashboard.addDashlet(1, 1);

		// Add a Dashlet by selecting "My Activity Stream" Dashlet
		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("input", "css", ".layout_Home .inline-drawer-header .search").set(customData.get("myActivityStream"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save and Assert created dashboard is available
		saveAndCheckDashboard(testName + "_2");
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	private void saveAndCheckDashboard(String dashName) throws Exception {
		// Save dashboard w/o any dashlets
		sugar().opportunities.dashboard.save();

		// Navigate to any other dashboard (for ex: Help Dashboard).
		sugar().opportunities.dashboard.chooseDashboard(customData.get("helpDashboard"));

		// Assert created dashboard is available
		sugar().opportunities.dashboard.chooseDashboard(dashName);
		sugar().opportunities.dashboard.getControl("dashboardTitle").assertEquals(dashName, true);
	}

	public void cleanup() throws Exception {}
}