package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_19003 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();

		// Config Forecasts settings.
		sugar().navbar.navToModule("Forecasts");
		new VoodooControl("a", "css", " .rowaction[name='save_button']").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify Fiscal Year Functionality in Reporting.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_19003_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Custom Report in Accounts module
		// TODO: VOOD-822
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "#report_type_div tr:nth-child(2) td:nth-child(3) tr:nth-child(1) td:nth-child(1)");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl nxtbtnCtrl = new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");

		// Select 'Create Report' from Reports menu
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.waitForVisible();
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Accounts").click();
		new VoodooControl("tr", "id", "Accounts_date_modified").click();

		// Verify Fiscal filters listed in dropdown.
		// TODO: VOOD-822
		new VoodooControl("option", "css", "option[value='tp_previous_fiscal_year']").assertExists(true);
		new VoodooControl("option", "css", "option[value='tp_previous_fiscal_quarter']").assertExists(true);
		new VoodooControl("option", "css", "option[value='tp_current_fiscal_year']").assertExists(true);
		new VoodooControl("option", "css", "option[value='tp_current_fiscal_quarter']").assertExists(true);
		new VoodooControl("option", "css", "option[value='tp_next_fiscal_year']").assertExists(true);
		new VoodooControl("option", "css", "option[value='tp_next_fiscal_quarter']").assertExists(true);
		new VoodooControl("option", "css", "option[value='tp_current_fiscal_year']").click();
		nextBtnCtrl.click();

		// Verify, for Group by, Fiscal Year/Quarter is available for any date type fields.
		// TODO: VOOD-822
		new VoodooControl("tr", "xpath", "//*[@id='Accounts_date_entered:fiscalQuarter']").assertExists(true);
		new VoodooControl("tr", "xpath", "//*[@id='Accounts_date_modified:fiscalQuarter']").assertExists(true);
		new VoodooControl("tr", "xpath", "//*[@id='Accounts_date_entered:fiscalYear']").assertExists(true);
		new VoodooControl("tr", "xpath", "//*[@id='Accounts_date_modified:fiscalYear']").assertExists(true);

		new VoodooControl("tr", "xpath", "//*[@id='Accounts_date_modified:fiscalYear']").click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Accounts_count").click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Accounts_name").click();
		nextBtnCtrl.click();
		nxtbtnCtrl.click();
		FieldSet myData = testData.get(testName).get(0);
		reportNameCtrl.set(myData.get("report_name"));
		saveAndRunCtrl.click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify, Report is displayed correctly according to the fiscal period.
		new VoodooControl("a", "css", ".oddListRowS1").assertContains(sugar().accounts.getDefaultData().get("name"),true);
		new VoodooControl("a", "css", ".list.view .Array td:nth-child(2)").assertContains("1",true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
