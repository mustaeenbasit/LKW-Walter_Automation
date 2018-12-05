package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21550 extends SugarTest {
	DataSource opportunityReccord = new DataSource();
	FieldSet dashletData = new FieldSet();
	UserRecord newUser;
	
	public void setup() throws Exception {
		dashletData = testData.get(testName).get(0);
		sugar.opportunities.api.create();
		sugar.login();
		
		// Create custom user
		newUser = (UserRecord) sugar.users.create();
		VoodooUtils.waitForReady(); // Needed extra wait
				
		// Logout from the admin user and login as custom user
		sugar.logout();
		newUser.login();
		
		// Define Controls for Dashlets
		// TODO: VOOD-960
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");
		VoodooSelect selectReportCtrl = new VoodooSelect("div", "css", ".fld_saved_report_id .select2-container");

		// Go to Home -> My Dashboard -> Edit
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();
		
		// Add a Dashlet
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);
		
		// Add a Dashlet -> Select "Saved Reports Chart Dashlet" tab in toggle drawer
		dashletSearchCtrl.set(dashletData.get("dashlet"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady(); // Extra wait needed 
		selectReportCtrl.set(dashletData.get("selectReport"));
		sugar.alerts.waitForLoadingExpiration();
		
		// Click on checkboxes to show x-axis and y-axis labels
		new VoodooControl("input", "css", ".fld_show_x_label input").click();
		new VoodooControl("input", "css", ".fld_show_y_label input").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Save Dashlet
		saveBtnCtrl.click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Save the home page Dashboard
		// TODO: VOOD-1645 - Need to update method(s) in 'Dashboard.java' for Edit page.
		new VoodooControl("a", "css", ".fld_save_button a").click();
		sugar.alerts.waitForLoadingExpiration();
	}

	/**
	 * Edit basic charts_Verify that "All Opportunities By Lead Source" basic chart is not changed 
	 * and the dialog box panel is closed when clicking "Close" button on the dialog box panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21550_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Define Controls for Dashlets
		// TODO: VOOD-960 - Dashlet selection
		VoodooControl openActionDropDown = new VoodooControl("i", "css", ".row-fluid.sortable:nth-of-type(4) .btn-group i");
		VoodooControl editLink = new VoodooControl("a", "css", ".row-fluid.sortable:nth-of-type(4) [data-dashletaction='editClicked']");
		VoodooControl xAxisLabel = new VoodooControl("input", "css", "input[name='x_axis_label']");
		VoodooControl yAxisLabel = new VoodooControl("input", "css", "input[name='y_axis_label']");
		VoodooControl cancelButton = new VoodooControl("a", "css", "#drawers .fld_cancel_button a");
		
		// Click on Edit option from Action drop down in Opportunities By Lead Source dashlet
		openActionDropDown.click();
		editLink.click();
		
		// Make some changes in Drawer. Lets change x-axis and y-axis label
		xAxisLabel.set(dashletData.get("labelX"));
		yAxisLabel.set(dashletData.get("labelY"));
		
		// Click on Cancel link
		cancelButton.click();
		
		// Verify that Drawer is closed
		cancelButton.assertVisible(false);
		
		// Verify that "Opportunities By Lead Source" basic chart is not changed
		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("text", "css", ".nv-x.nv-axis .nv-axislabel").assertContains(dashletData.get("xAxisLabel"), true);
		new VoodooControl("text", "css", ".nv-y.nv-axis .nv-axislabel").assertContains(dashletData.get("yAxisLabel"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}