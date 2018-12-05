package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18972 extends SugarTest {
	DataSource bugRecord = new DataSource();
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		bugRecord = testData.get(testName + "_bugs");

		// Creating Bug records
		sugar().bugs.api.create(bugRecord);
		sugar().login();

		// Enable display for Bug module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
	}

	/**
	 * Verify that report is created successfully and records are displayed in Report result.
	 * @throws Exception
	 */
	@Test
	public void Reports_18972_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// VOOD-643
		// Creating Rows and Columns Report
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#report_type_div > table > tbody > tr:nth-child(2) > td:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(1)").click();
		new VoodooControl("table", "id", "Bugs").click();
		new VoodooControl("select", "id", "Filter.1_combo").set("OR");

		// select the bug record
		new VoodooControl("tr", "id", "Bugs_name").click();
		new VoodooControl("input", "css", "td:nth-child(4) table tbody tr td:nth-child(2) input").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "tr.evenListRowS1 > td:nth-child(1) > a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// select Bugs Status
		new VoodooControl("tr", "id", "Bugs_status").click();
		new VoodooControl("select", "id", "select_Filter.1_table_filter_row_2").set(bugRecord.get(1).get("status"));

		// Saving the report
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Bugs_name").click();
		new VoodooControl("tr", "id", "Bugs_status").click();
		nextBtnCtrl.click();
		fs = testData.get(testName).get(0);
		new VoodooControl("input", "id", "save_report_as").set(fs.get("report_name"));
		new VoodooControl("input", "id", "saveButton").click();
		VoodooUtils.waitForReady();

		// Sorting the records the reports search result
		// TODO: VOOD-1534
		new VoodooControl("a", "css", ".listViewThLinkS1").click();

		// Asserting the records in above created reports search result
		new VoodooControl("a", "css", "#report_results  tr.oddListRowS1 > td:nth-child(1) > a").assertContains(bugRecord.get(0).get("name"), true);
		new VoodooControl("a", "css", "#report_results > div > table > tbody > tr.oddListRowS1 > td:nth-child(2)").assertEquals(bugRecord.get(0).get("status"),true);
		new VoodooControl("a", "css", "#report_results  tr.evenListRowS1 > td:nth-child(1) > a").assertContains(bugRecord.get(1).get("name"), true);
		new VoodooControl("a", "css", "#report_results > div > table > tbody > tr.evenListRowS1 > td:nth-child(2)").assertEquals(bugRecord.get(1).get("status"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}