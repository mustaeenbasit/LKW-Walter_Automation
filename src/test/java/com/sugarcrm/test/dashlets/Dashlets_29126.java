package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_29126 extends SugarTest {
	FieldSet dashboardData;

	public void setup() throws Exception {
		sugar.calls.api.create();
		dashboardData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify that the x-axis label on the chart should display the value on the "Show x-axis label" field on the dashlet configurations page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_29126_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls for Dashlets
		// TODO: VOOD-960
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooSelect selectReportCtrl = new VoodooSelect("div", "css", ".fld_saved_report_id .select2-container");
		VoodooSelect chartTypeCtrl = new VoodooSelect("div", "css", ".fld_chart_type .select2-container");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");

		// Go to Home -> My Dashboard -> Edit
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();

		// Add a Dashlet
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);

		// Add a Dashlet -> Select "Saved Reports Chart Dashlet"
		dashletSearchCtrl.set(dashboardData.get("savedReportsChartDashlet"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady(); // Extra wait needed 

		// Select a report: Calls By Team By User
		selectReportCtrl.set(dashboardData.get("reportName"));
		VoodooUtils.waitForReady(); // Need to wait so that label field reflect the default values

		// Change the Chart Type to "Line Chart".
		chartTypeCtrl.set(dashboardData.get("chartType"));

		// Check 'Show y-axis label' and 'Show x-axis label'
		// TODO: VOOD-960
		new VoodooControl("input", "css", ".fld_show_x_label input").click();
		VoodooUtils.waitForReady(); // Need to wait so that label field reflect the default values
		new VoodooControl("input", "css", ".fld_show_y_label input").click();
		VoodooUtils.waitForReady(); // Need to wait so that label field reflect the default values

		// Save
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Save the home page
		// TODO: VOOD-1645
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify that the x-axis label on the chart should display the value on the "Show x-axis label" field on the dashlet configurations page
		// TODO: VOOD-960
		new VoodooControl("text", "css", ".nv-y .nv-axislabel").assertEquals(dashboardData.get("count"), true);
		new VoodooControl("text", "css", ".nv-x .nv-axislabel").assertEquals(dashboardData.get("teamName"), true);
		new VoodooControl("text", "css", "g.nv-x.nv-axis g g text").assertEquals(dashboardData.get("admin"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}