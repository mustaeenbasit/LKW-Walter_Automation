package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18959 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
		sugar().contracts.api.create();

	}

	/**
	 * run_summation_contracts_report
	 *
	 * @throws Exception
	 */
	@Test
	public void Reports_18959_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// VOOD-643
		// navigate to report module
		sugar().navbar.navToModule("Reports");
		new VoodooControl("li", "css", ".dropdown.active .fa-caret-down").click();
		// click on Create Report
		new VoodooControl("a", "css", "[data-navbar-menu-item='LBL_CREATE_REPORT']").click();
 		VoodooUtils.focusFrame("bwc-frame");
		// click on Summation Report
 		new VoodooControl("td", "css", "#report_type_div  tr:nth-child(2) td:nth-child(1) table tbody  tr:nth-child(3) td:nth-child(1)").click();
		// click on Contracts module
 		new VoodooControl("a", "css", "#Contracts tbody tr td:nth-child(2) a").click();
		// select Contract Name
 		new VoodooControl("tr", "id", "Contracts_name").click();
		// select the contract record
 		new VoodooControl("input", "css", "td:nth-child(4) table tbody tr td:nth-child(2) input").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusWindow("SugarCRM");
		new VoodooControl("a", "css", "table.list.view tbody tr:nth-child(3) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "nextBtn").click();
		new VoodooControl("tr", "id", "Contracts_name").click();
		new VoodooControl("input", "id", "nextBtn").click();
		new VoodooControl("tr", "id", "Contracts_count").click();
		new VoodooControl("input", "id", "nextBtn").click();
		new VoodooControl("option", "css", "#chart_type > option:nth-child(3)").click();
		new VoodooControl("input", "css", "#chart_options_div > table:nth-child(1) > tbody > tr > td > input:nth-child(2)").click();
		new VoodooControl("input", "id", "save_report_as").set("Contract summation  report");
		// save and run the report
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// assert the record count in the reports chart and list
		new VoodooControl("text", "css", "div.chartContainer div.scrollBars div g.nv-titleWrap text").assertContains("1", true);
		new VoodooControl("td", "css", "#report_results table.reportlistView tbody tr.Array td:nth-child(2)").assertContains("1", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}