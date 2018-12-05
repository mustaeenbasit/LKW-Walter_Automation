package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_19330 extends SugarTest {

	public void setup() throws Exception {
		// Logging in as admin
		sugar().login();
	}

	/**
	 * Verify the columns to the list view should be able to reorder for this dashlet
	 * @throws Exception
	 */
	@Test
	public void Dashlets_19330_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		FieldSet dashboardData = testData.get(testName).get(0);

		// Define Controls for Dashlets
		// TODO: VOOD-960 - Dashlet selection 
		VoodooControl myContactsDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(3) .dashlet-container");
		
		// Dashlet table headers
		VoodooControl firstHeaderCtrl = new VoodooControl("span", "css", myContactsDashletCtrl.getHookString() + " .table th span");
		VoodooControl secondHeaderCtrl = new VoodooControl("span", "css", myContactsDashletCtrl.getHookString() + " .table th:nth-child(2) span");
		VoodooControl thirdHeaderCtrl = new VoodooControl("span", "css", myContactsDashletCtrl.getHookString() + " .table th:nth-child(3) span");
		VoodooControl fourthHeaderCtrl = new VoodooControl("span", "css", myContactsDashletCtrl.getHookString() + " .table th:nth-child(4) span");
		
		// Go to main Dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// Verify the My Contacts dashlet's columns order
		firstHeaderCtrl.assertContains(dashboardData.get("firstDashletsColumn"), true);
		secondHeaderCtrl.assertContains(dashboardData.get("secondDashletsColumn"), true);
		thirdHeaderCtrl.assertContains(dashboardData.get("thirdDashletColumn"), true);
		fourthHeaderCtrl.scrollIntoViewIfNeeded(false);
		fourthHeaderCtrl.assertContains(dashboardData.get("fourtDashletsColumn"), true);

		// Edit the My Contacts dashlet
		new VoodooControl("div", "css", ".row-fluid.sortable:nth-of-type(3) .btn-group").click();
		new VoodooControl("a", "css", myContactsDashletCtrl.getHookString() + " .dropdown-menu li a").click();
		VoodooUtils.waitForReady();

		// Define multiselect column's widget controls
		VoodooControl widgetCtrl = new VoodooControl("ul", "css", ".fld_display_columns .select2-choices");
		VoodooControl firstColumnWidgetCtrl = new VoodooControl("div", "css", widgetCtrl.getHookString() + " li div");
		VoodooControl fourthColumnWidgetCtrl = new VoodooControl("div", "css", widgetCtrl.getHookString() + " li:nth-child(4) div");
		
		// In the edit page, you should see a multiselect widget for adding columns to the list view -> drag and drop to reorder the columns
		fourthColumnWidgetCtrl.dragNDrop(firstColumnWidgetCtrl);

		// Click Save button 
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify the When you save, the list will be refreshed and you should see columns in your new order.
		firstHeaderCtrl.assertContains(dashboardData.get("fourtDashletsColumn"), true);
		secondHeaderCtrl.assertContains(dashboardData.get("firstDashletsColumn"), true);
		thirdHeaderCtrl.assertContains(dashboardData.get("secondDashletsColumn"), true);
		fourthHeaderCtrl.scrollIntoViewIfNeeded(false);
		fourthHeaderCtrl.assertContains(dashboardData.get("thirdDashletColumn"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}