package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23427 extends SugarTest {
	DataSource casesData = new DataSource();

	public void setup() throws Exception {
		casesData = testData.get(testName);
		sugar().cases.api.create(casesData);
		sugar().login();
	}

	/**
	 * Run Reports_Verify that case information can be reported correctly.
	 * @throws Exception
	 */
	@Test
	public void Cases_23427_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		// Create Custom(Summation) Report in Cases module
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl subjectCtrl = new VoodooControl("tr", "id", "Cases_name");

		//  Go to report modules and create Summation Reports for Opportunity module.
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "img[name='summationImg']").click();
		new VoodooControl("table", "id", "Cases").click();

		// On the "Define Filters" tab, add one or more filters to limit the data records to be retrieved.
		// TODO: VOOD-822
		subjectCtrl.click();
		new VoodooControl("input", "css", "input[id='Cases:name:name:1']").set(casesData.get(0).get("name"));
		VoodooControl elementNameCtrl = new VoodooControl("li", "css", "div[id='ReportsWizardForm_Cases:name:name:1_results'] ul li");
		elementNameCtrl.click();
		nextBtnCtrl.click();

		// On the "Define Group by" tab, specify the summary data to be grouped. /*subject*/
		subjectCtrl.click();
		nextBtnCtrl.click();

		// On the "Choose Display Summaries" tab, select the columns to be displayed and click Next
		nextBtnCtrl.click();

		// Chart Options : no chart and save & run report.
		// TODO: VOOD-822
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "input[name='Save and Run']").click();
		VoodooUtils.waitForReady();

		// Verify that the report is generated according to the filter, group, and display column specified.
		// Verify that only one case (i.e. Case1) is appears in first row. No other record are appears in the report
		// TODO: VOOD-822
		new VoodooControl("a", "css", ".oddListRowS1 a").assertContains(casesData.get(0).get("name"), true);
		new VoodooControl("td", "css", ".evenListRowS1").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}