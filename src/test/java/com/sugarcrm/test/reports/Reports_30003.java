package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_30003 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Filter Conditions for "Currency rate" field should be display while creating a report 
	 * of Opportunities Module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_30003_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to report module and click on Create Report
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// click on Summation Report
		// TODO: VOOD-822
		new VoodooControl("img", "css", "#report_type_div [name='summationImg']").click();

		// Click on Opportunities
		new VoodooControl("table", "id", "Opportunities").click();

		// Select 'Currency Rate' on define filter screen
		new VoodooControl("tr", "id", "Opportunities_base_rate").click();
		VoodooUtils.waitForReady();

		// Verify the filter conditions are displayed on the screen
		FieldSet filterData = testData.get(testName).get(0);
		new VoodooControl("b", "css", "[id='Filter.1_table_filter_row_1'] td:nth-child(2) b")
		.assertEquals(filterData.get("filterConditions1"), true);
		new VoodooControl("option", "css", "[name='qualify'] option")
		.assertEquals(filterData.get("filterConditions2"), true);
		new VoodooControl("input", "css", "[title='filter text input']").assertVisible(true);
		new VoodooControl("input", "css", "[id='runtime_filter_Filter.1_table_filter_row_1']").assertVisible(true);
		new VoodooControl("img", "css", "[id='Filter.1_table_filter_row_1'] td:nth-child(4) img").assertVisible(true);
		new VoodooControl("img", "css", "[id='Filter.1_table_filter_row_1'] td:nth-child(5) img").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}