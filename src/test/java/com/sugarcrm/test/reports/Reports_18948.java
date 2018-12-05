package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18948 extends SugarTest{
	VoodooControl nameCtrl, advanceSearchCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * create_rows_columns_report
	 * @throws Exception
	 */
	@Test
	public void Reports_18948_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to reports module
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Verifying  home page is displayed successfully
		// TODO: VOOD-822
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("h2", "css", ".moduleTitle h2").assertElementContains(customData.get("module_title"), true);
		VoodooUtils.focusDefault();
		VoodooControl createRowsAndColumnsReportCtrl = new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(1) table tbody tr:nth-child(1) td");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");

		// Create Rows and Columns report, select Quotes module
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.assertExists(true);
		createRowsAndColumnsReportCtrl.click();
		new VoodooControl("table", "id", "Quotes").click();

		// Set Quotes Stage is Closed Accepted.
		new VoodooControl("tr", "id", "Quotes_quote_stage").click();
		new VoodooControl("option", "css", "#filter_designer_div div.bd tr:nth-child(1) td:nth-child(2) tr td:nth-child(4) select option:nth-of-type(6)").click();
		nextBtnCtrl.click();

		// Displayed columns: Quotes > Quoted Line Item > Name, Quantity
		new VoodooControl("a", "css", "#module_tree div div:nth-child(14) td.ygtvcell.ygtvcontent a").click();
		new VoodooControl("tr", "id", "Products_name").click();
		new VoodooControl("tr", "id", "Products_quantity").click();

		// Team name Quotes > Team > Primary Team Name
		new VoodooControl("a", "css", "#module_tree div div:nth-child(19) td.ygtvcell.ygtvcontent a").click();
		new VoodooControl("tr", "id", "Teams_name").click();
		nextBtnCtrl.click();

		// Report name and save/run
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();
		
		// Run and save report and Verifying column names
		new VoodooControl("a", "css", "#report_results div tr:nth-child(2) th:nth-child(1) a").assertContains(customData.get("column_1"), true);
		new VoodooControl("a", "css", "#report_results div tr:nth-child(2) th:nth-child(2) a").assertContains(customData.get("column_2"), true);
		new VoodooControl("a", "css", "#report_results div tr:nth-child(2) th:nth-child(3) a").assertContains(customData.get("column_3"), true);

		// Verifying report in list view
		VoodooUtils.focusDefault();
		sugar().navbar.selectMenuItem(sugar().reports, "viewReports");
		VoodooUtils.focusFrame("bwc-frame");
		nameCtrl = new VoodooControl("input", "name", "name");
		nameCtrl.set(testName);
		advanceSearchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		advanceSearchCtrl.click();
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(4) span a").assertEquals(testName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}