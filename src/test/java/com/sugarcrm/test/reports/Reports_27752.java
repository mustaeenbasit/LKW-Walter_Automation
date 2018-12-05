package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_27752 extends SugarTest {
	VoodooControl createRowsAndColumnsReportCtrl, rliCtrl, modulesCtrl;
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();

		// Create opp + rli record 
		// TODO: VOOD-444
		sugar().opportunities.create();
		if(sugar().alerts.getSuccess().queryVisible()){
			sugar().alerts.getSuccess().closeAlert();
		}

		// Define controls for reports module
		// TODO: VOOD-822
		createRowsAndColumnsReportCtrl = new VoodooControl("img", "css", "[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		rliCtrl = new VoodooControl("table", "css", "[id='Revenue Line Items']");
		VoodooControl rliSalesStageCtrl = new VoodooControl("tr", "id", "RevenueLineItems_sales_stage");
		modulesCtrl= new VoodooControl("td", "css", "#reportDetailsTable > tbody > tr:nth-child(2) > td:nth-child(1)");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");

		// Create report based on RLI module
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.click();
		VoodooUtils.waitForReady();
		rliCtrl.click();
		VoodooUtils.waitForReady();
		rliSalesStageCtrl.click();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("tr", "id", "RevenueLineItems_likely_case").click();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "save_report_as").set(testName+"_1");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000); // Extra wait Needed

		// verify that Revenue Line Items module is displayed in module list 
		modulesCtrl.assertContains(ds.get(2).get("assert_string"), true);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Create another report (not based on RLI module) which uses RLI -> Sales Stage 
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		createRowsAndColumnsReportCtrl.click();
		new VoodooControl("table", "id", sugar().opportunities.moduleNamePlural).click();
		new VoodooControl("a", "xpath", "//*[@id='module_tree']//a[.='Revenue Line Items']").click();
		rliSalesStageCtrl.click();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("tr", "id", "Opportunities_name").click();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "save_report_as").set(testName+"_2");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000); // Extra wait Needed

		// verify that Opportunities with RLI module is displayed in module list 
		modulesCtrl.assertContains(ds.get(0).get("assert_string"), true);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that Reporting does not show Revenue Line Items as an available module for inclusion in a report when the Opportunities setting is set to "Opportunities":
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_27752_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> Opportunities and set it to be Opportunities only.
		sugar().admin.api.switchToOpportunitiesView();
		VoodooUtils.waitForReady();

		// If a user has already been to the reports module, a logout is needed to hide the RLI module as it's cached in their Session. 
		sugar().logout();
		sugar().login();

		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that Reporting should not show Revenue Line Items as an available module for inclusion in a report
		rliCtrl.assertVisible(false);
		VoodooUtils.focusDefault();

		// Controls for the report search panel
		// TODO: VOOD-822
		VoodooControl searchCtrl = new VoodooControl("input", "css", "input[name='name']");
		VoodooControl searchButtonControl = new VoodooControl("input", "id", "search_form_submit_advanced");

		// Search a report based on RLI module
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		searchCtrl.set(testName + "_1");
		searchButtonControl.click();
		VoodooUtils.waitForReady();

		// Verify that Any Reports based off the Revenue Line Item Module should be soft deleted
		new VoodooControl("div", "css", ".list.view.listViewEmpty").assertContains(ds.get(1).get("assert_string"), true);

		// Search a report not based on RLI module
		searchCtrl.set(testName + "_2");
		searchButtonControl.click();
		VoodooUtils.waitForReady();

		// Any Reports that used RLI -> Sales Stage, should be converted back to Opp -> Sales Stage
		// TODO: VOOD-822
		new VoodooControl("a", "css", ".oddListRowS1 b span a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		modulesCtrl.assertContains(ds.get(0).get("assert_string"), false);
		modulesCtrl.assertContains(sugar().opportunities.moduleNamePlural, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}