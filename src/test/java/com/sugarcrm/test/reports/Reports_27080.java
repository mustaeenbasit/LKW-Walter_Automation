package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_27080 extends SugarTest{
	FieldSet customData;
	VoodooControl reportModuleCtrl, nameCtrl, advanceSearchCtrl ;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Account record
		FieldSet newData = new FieldSet();
		newData.put("name", customData.get("account_name"));
		sugar().accounts.api.create(newData);
		sugar().login();

		// TODO: VOOD-822 -need lib support of reports module
		advanceSearchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl createReportCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li");
		VoodooControl createRowsAndColumnsReportCtrl = new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(1) table tbody tr:nth-child(1) td");
		VoodooControl accountsModuleCtrl = new VoodooControl("table", "id", "Accounts");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");

		// Create Rows and Columns report, select Account module
		sugar().navbar.navToModule(customData.get("module_plural_name"));
		VoodooUtils.focusDefault();
		reportModuleCtrl.click();
		createReportCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.click();
		accountsModuleCtrl.click();
		nextBtnCtrl.click();

		// TODO: VOOD-822 -need lib support of reports module
		// Displayed columns: Account > Name, Billing Street 
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_street").click();
		nextBtnCtrl.click();

		// Report name and save
		new VoodooControl("input", "id", "save_report_as").set(customData.get("name"));
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();

		// Run report and Verifying Account Names
		new VoodooControl("a", "css", "#report_results div tr:nth-child(3) td:nth-child(1) a").assertEquals(customData.get("account_name"), true);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that edit icon on the report list view is working properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_27080_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(customData.get("module_plural_name"));
		VoodooUtils.focusFrame("bwc-frame");

		// Search created report
		// TODO: VOOD-822 -need lib support of reports module
		VoodooControl nameCtrl = new VoodooControl("input", "css", ".edit table tr:nth-of-type(1) td:nth-of-type(2) input");
		nameCtrl.set(customData.get("name"));
		VoodooControl filterModuleSearchCtrl = new VoodooControl("option", "css", ".edit table tr:nth-of-type(2) td:nth-of-type(2) select[name='report_module[]'] option:nth-of-type(1)");
		filterModuleSearchCtrl.click();		
		advanceSearchCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("a", "css", "[title='Edit']").click();		
		sugar().alerts.waitForLoadingExpiration();

		// Verify that report edit page module title
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("h2", "css", ".moduleTitle h2").assertEquals("Report Wizard", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}