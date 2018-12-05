package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_26738 extends SugarTest {
	VoodooControl dashletConfigureToolCtrl;

	public void setup() throws Exception {
		FieldSet customFS = testData.get(testName).get(0);
		sugar.login();

		// TODO: VOOD-960 - Dashlet selection
		// Go to Home -> My Dashboard -> Edit
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();

		// Add a Dashlet
		sugar.home.dashboard.addRow();
		VoodooUtils.waitForReady();
		sugar.home.dashboard.addDashlet(4, 1);

		// Add a Dashlet -> Select "Saved Reports Chart Dashlet"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customFS.get("savedReportsChartDashlet"));
		VoodooUtils.waitForReady();

		// Click report
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Select a report: "Calls By Team By User"
		new VoodooSelect("div", "css", ".fld_saved_report_id .select2-container").set(customFS.get("selectAReport"));
		VoodooUtils.waitForReady(); 
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page
		// TODO: VOOD-1645 - Need to update method(s) in 'Dashboard.java' for Edit page.
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that clicking edit selected report link in dashlet will display the edit view of the report in report modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_26738_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.home.dashboard.edit();
		dashletConfigureToolCtrl = new VoodooControl("i", "css", ".row-fluid.sortable:nth-of-type(4) .btn-group i");
		dashletConfigureToolCtrl.click();
		new VoodooControl("a", "css", ".row-fluid.sortable:nth-of-type(4) .btn-group [data-dashletaction='editClicked']").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("a", "css", ".edit.fld_editReport a").click();
		sugar.alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-822 - need lib support of reports module
		// Verify that User would be re-directed to 'Define Filter' page of the selected report in the Edit view mode.
		new VoodooControl("input", "id", "nextBtn").assertExists(true);
		new VoodooControl("input", "css", "input[name='Save and Run']").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}