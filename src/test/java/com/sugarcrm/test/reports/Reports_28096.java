package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Reports_28096 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 *  Verify that reports round up integer average value to whole numbers.
	 * @throws Exception
	 */
	@Test
	public void Reports_28096_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource reportData = testData.get(testName); 

		// TODO: VOOD-822
		// Create Custom(Summation) Report in Opportunities module
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='summationImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		// Go to Reports -> Create reports -> summation report -> Opportunities 
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Opportunities").click();

		// On the 'Define Filters' step, select 'Probablity (%)' and 'Is not empty' and click to next. 
		// TODO: VOOD-822
		new VoodooControl("tr", "id", "Opportunities_probability").click();
		new VoodooControl("option", "css", "#filter_designer_div div.bd tr:nth-child(1) td:nth-child(2) tr td:nth-child(3) select option:nth-of-type(9)").click();
		nextBtnCtrl.click();

		// On the 'define group by' step, select 'Month: Date created' and click next
		// TODO: VOOD-822
		new VoodooControl("tr", "css", "tr[id='Opportunities_date_entered:month']").click();
		nextBtnCtrl.click();

		// On the 'choose display summaries' step, select 'AVG:Probability (%)', 'SUM: Probability(%)' and 'count' click to next.
		new VoodooControl("tr", "css", "tr[id='Opportunities_probability:avg']").click();
		new VoodooControl("tr", "css", "tr[id='Opportunities_probability:sum']").click();
		new VoodooControl("tr", "id", "Opportunities_count").click();
		nextBtnCtrl.click();

		// Click next to by pass the chart option step
		// TODO: VOOD-822
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();

		// Name the report and then click 'Save and run'
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();

		// The report round up averages the integer field (Probability) to a significance digits. The 'AVG: Probability (%)' value should be determined by dividing the 'SUM: Probability (%)' field by the 'Count' field.
		// TODO: VOOD-822
		for(int i = 0; i< reportData.size(); i++){
			new VoodooControl("a", "css", ".reportlistViewThS1:nth-child("+ (i+1) +") a").assertContains(reportData.get(i).get("defaultColumns"), true);
			if (i > 0)
				new VoodooControl("td", "css", ".oddListRowS1:nth-child("+ (i+1) +")").assertEquals(reportData.get(i).get("columnsValues"), true);
		}

		// Verify the Probability and other fields in "Grand Total" report view also.
		// TODO: VOOD-822
		for(int i = 1; i< reportData.size(); i++){
			new VoodooControl("th", "css", ".list.view tr:nth-child(1) th:nth-child("+ (i+1) +")").assertContains(reportData.get(i).get("defaultColumns"), true);
			if (i > 0)
				new VoodooControl("td", "css", ".list.view tr:nth-child(2) td:nth-child("+ (i+1) +")").assertEquals(reportData.get(i).get("columnsValues"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}