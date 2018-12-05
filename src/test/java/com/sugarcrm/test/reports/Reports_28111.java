package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Reports_28111 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();

		// Need to save forecast default settings as it reflects the value in Commit Stage field
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
	}

	/**
	 *  Verify that summation report should run and display result when using group by with commit.
	 * @throws Exception
	 */
	@Test
	public void Reports_28111_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet reportData = testData.get(testName).get(0); 

		// TODO: VOOD-822
		// Create Custom(Summation) Report in Opportunities module
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='summationImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		//  Go to report modules and create Summation Reports for Opportunity module. 
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Opportunities").click();

		// Define Filters : Opportunities > Assigned to User > User Name is Administrator
		// TODO: VOOD-822
		new VoodooControl("a", "css", "#module_tree > div > div > div > div > div:nth-child(2) > table > tbody > tr > td.ygtvcell.ygtvcontent a").click();
		new VoodooControl("tr", "id", "Users_user_name").click();
		new VoodooControl("option", "css", "#filter_designer_div div.bd tr:nth-child(1) td:nth-child(2) tr td:nth-child(4) select option:nth-of-type(3)").click();
		nextBtnCtrl.click();

		// Define Group By: Opportunities > Commit stage, Opportunities > name
		// TODO: VOOD-822
		new VoodooControl("tr", "id", "Opportunities_commit_stage").click();
		new VoodooControl("tr", "id", "Opportunities_name").click();
		nextBtnCtrl.click();

		// Display Summaries : Opporutnities > Commit stage , Opporutnities > name 
		nextBtnCtrl.click();

		// Chart Options : no chart and save & run report.
		// TODO: VOOD-822
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the report should run and display result.
		// TODO: VOOD-822
		new VoodooControl("a", "css", ".reportlistViewThS1 a").assertContains(reportData.get("commitStage"), true);
		new VoodooControl("a", "css", ".reportlistViewThS1:nth-child(2) a").assertContains(reportData.get("oppName"), true);
		new VoodooControl("a", "css", ".oddListRowS1").assertContains(reportData.get("exclude"), true);
		new VoodooControl("a", "css", ".oddListRowS1:nth-child(2)").assertContains(sugar().opportunities.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}