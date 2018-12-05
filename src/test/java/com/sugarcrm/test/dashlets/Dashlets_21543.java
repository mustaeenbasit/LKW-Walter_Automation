package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21543 extends SugarTest {
	FieldSet chartData = new FieldSet();
	UserRecord newUser;
	
	public void setup() throws Exception {
		sugar.login();
		chartData = testData.get(testName).get(0);
		
		// Create custom user
		newUser = (UserRecord) sugar.users.create();
		VoodooUtils.waitForReady(); // Needed extra wait
		
		// Logout from the admin user and login as custom user
		sugar.logout();
		newUser.login();
		
		// Define Controls for Dashlets
		// TODO: VOOD-960 - Dashlet selection
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");
		VoodooControl saveDashboard = new VoodooControl("a", "css", ".fld_save_button a");		
		
		// Create chart on "Dashboard" page
		// Go to Home -> My Dashboard -> Edit
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();
		
		// Add a Dashlet
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);
				
		// Add a Dashlet -> Select "Saved Reports Chart Dashlet" tab in toggle drawer
		dashletSearchCtrl.set(chartData.get("chartName"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady();
		
		// Save Dashlet
		saveBtnCtrl.click();

		// Save the home page Dashboard
		saveDashboard.click();
		sugar.alerts.waitForLoadingExpiration();
	}

	/**
	 * View charts_Verify that the removed chart's title is displayed as an item of "Add a Chart" drop down list.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21543_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Remove created Dashlet
		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("i", "css", ".row-fluid.sortable:nth-of-type(4) .btn-group i").click();
		new VoodooControl("a", "css", ".row-fluid.sortable:nth-of-type(4) [data-dashletaction='removeClicked']").click();
		sugar.alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();
		
		// Edit Dashboard, Click on "Add a row" and then Click "Add a Dashlet"
		sugar.home.dashboard.edit();
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);
		
		// Verify that removed Chart title is displayed as an item on the "Add a Dashlet" pop up
		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("tbody", "css", "[data-voodoo-name='dashletselect'] table tbody").assertContains(chartData.get("chartName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}