package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18963 extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl reportModuleCtrl, reportCtrl;

	public void setup() throws Exception {
		// Creating calls record
		customData = testData.get(testName).get(0);
		sugar().calls.api.create();
		FieldSet newCallsData = new FieldSet();
		newCallsData.put("name", customData.get("call_name"));
		newCallsData.put("status", customData.get("status"));
		newCallsData.put("date_start_date", customData.get("date_start_date"));
		newCallsData.put("date_start_time", customData.get("date_start_time"));
		newCallsData.put("durationHours", customData.get("durationHours"));
		newCallsData.put("durationMinutes", customData.get("durationMinutes"));
		sugar().calls.api.create(newCallsData);
		sugar().login();

		// Creating Summation Report with Details report against Calls module
		// TODO: VOOD-822
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("name", "css", "img[name='summationWithDetailsImg']").click();
		new VoodooControl("table", "id", "Calls").click();

		// Applying Filter - startdate with after condition
		new VoodooControl("tr", "id", "Calls_date_start").click();
		new VoodooControl("select", "css", "select[name='qualify']").set(customData.get("after"));
		new VoodooControl("input", "css", "td:nth-of-type(4) table tbody tr td:nth-of-type(1) input[type='text']").set(customData.get("date_c"));
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();

		// Applying filer Group By - Status
		new VoodooControl("tr", "id", "Calls_status").click();
		nextBtnCtrl.click();

		// Selecting choose by Display summary
		new VoodooControl("tr", "id", "Calls_count").click();
		nextBtnCtrl.click();

		// Selecting columns updated above
		new VoodooControl("tr", "id", "Calls_duration_hours").click();
		new VoodooControl("tr", "id", "Calls_duration_minutes").click();
		new VoodooControl("tr", "id", "Calls_status").click();
		new VoodooControl("tr", "id", "Calls_name").click();
		nextBtnCtrl.click();

		// Chart type - Horizontal Bar
		new VoodooControl("select", "id", "chart_type").set(customData.get("chart_type"));
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();

		// Save and Run Report
		new VoodooControl("input", "id", "save_report_as").set(customData.get("report_name"));
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Summation Report with Details report for Calls module
	 * @throws Exception
	 */
	@Test
	public void Reports_18963_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Report record
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa.fa-caret-down");
		reportModuleCtrl.click();
		reportCtrl = new VoodooControl("li", "css", "#header div.module-list li.dropdown.active li:nth-child(6)");
		reportCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1171
		// Verify Horizontal Bar chart and result displayed
		// UI assertions can be best done manually
		// Added waitForReady as on CI it takes time to render chart on reports result page
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "showHideChartButton").assertExists(true);
		new VoodooControl("div", "css", ".reportChartContainer").assertExists(true);
		new VoodooControl("div", "css", "div.reportChartContainer div.chartContainer div.scrollBars div").assertExists(true);
		new VoodooControl("input", "id", "showHideChartButton").click();
		new VoodooControl("div", "id", "report_results").assertExists(true);

		// Verify calls data inside report
		// Duration hours
		new VoodooControl("a", "css", "#report_results table.reportDataChildtablelistView th:nth-child(1) a").assertContains(customData.get("duration_hours_header"), true);
		new VoodooControl("td", "css", "#report_results table.reportDataChildtablelistView tr:nth-child(2) td:nth-child(1)").assertEquals(sugar().calls.getDefaultData().get("durationHours"), true);
		new VoodooControl("td", "css", "#report_results table:nth-child(8) .reportDataChildtablelistView tr:nth-child(2) td:nth-child(1)").assertEquals(customData.get("durationHours"), true);

		// Duration minutes
		new VoodooControl("a", "css", "#report_results table.reportDataChildtablelistView th:nth-child(2) a").assertContains(customData.get("duration_mins_header"), true);
		new VoodooControl("td", "css", "#report_results table.reportDataChildtablelistView tr:nth-child(2) td:nth-child(2)").assertEquals(sugar().calls.getDefaultData().get("durationMinutes"), true);
		new VoodooControl("td", "css", "#report_results table:nth-child(8) .reportDataChildtablelistView tr:nth-child(2) td:nth-child(2)").assertEquals(customData.get("durationMinutes"), true);

		// Status
		new VoodooControl("a", "css", "#report_results table.reportDataChildtablelistView th:nth-child(3) a").assertContains(customData.get("status_header"), true);
		new VoodooControl("td", "css", "#report_results table.reportDataChildtablelistView tr:nth-child(2) td:nth-child(3)").assertEquals(sugar().calls.getDefaultData().get("status"), true);
		new VoodooControl("td", "css", "#report_results table:nth-child(8) .reportDataChildtablelistView tr:nth-child(2) td:nth-child(3)").assertEquals(customData.get("status"), true);

		// Name
		new VoodooControl("a", "css", "#report_results table.reportDataChildtablelistView th:nth-child(4) a").assertContains(customData.get("call_header"), true);
		new VoodooControl("td", "css", "#report_results table.reportDataChildtablelistView tr:nth-child(2) td:nth-child(4) a").assertEquals(sugar().calls.getDefaultData().get("name"), true);
		new VoodooControl("td", "css", "#report_results table:nth-child(8) .reportDataChildtablelistView tr:nth-child(2) td:nth-child(4) a").assertEquals(customData.get("call_name"), true);

		// Verify Total summary for Calls
		// Grand Total
		new VoodooControl("td", "css", "#report_results table:nth-child(10) td").assertContains(customData.get("total_header"), true);
		new VoodooControl("td", "css", "#report_results table:nth-child(11) td:nth-child(2)").assertContains(customData.get("count_header"), true);
		new VoodooControl("td", "css", "#report_results table:nth-child(11) tr:nth-child(2) td:nth-child(2)").assertContains(customData.get("totalCount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}