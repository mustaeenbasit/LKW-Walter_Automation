package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_26746 extends SugarTest{
	FieldSet customData;
	VoodooControl reportModuleCtrl, nameCtrl ;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();

		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("account_name1"));
		fs.put("billingAddressState", customData.get("billingAddressState"));
		fs.put("billingAddressCity", customData.get("billingAddressCity"));
		fs.put("billingAddressPostalCode", customData.get("billingAddressPostalCode"));
		sugar().accounts.api.create(fs);

		fs.put("name", customData.get("account_name2"));
		sugar().accounts.api.create(fs);
	}

	/**
	 * Test Case 26746: Search with wildcards.
	 * @throws Exception
	 */
	@Test
	public void Reports_26746_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(customData.get("module_plural_name"));

		// TODO: VOOD-822
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl createReportCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li");
		VoodooControl createRowsAndColumnsReportCtrl = new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(1) table tbody tr:nth-child(1) td");
		VoodooControl accountsModuleCtrl = new VoodooControl("table", "id", "Accounts");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");

		VoodooUtils.focusDefault();
		reportModuleCtrl.click();
		createReportCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.click();
		accountsModuleCtrl.click();

		// TODO: VOOD-822
		new VoodooControl("tr", "id", "Accounts_name").click();
		// click on run-time check-box
		new VoodooControl("input", "css", "td:nth-child(4) > table > tbody > tr > td:nth-child(3) > input[type='checkbox']").click();
		// Select Account
		new VoodooControl("input", "css", "td:nth-child(4) > table td:nth-child(2) > input[title='Select']").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "body > table.list.view > tbody > tr.oddListRowS1 > td:nth-child(1) > a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		nextBtnCtrl.click();
		// Select columns for display
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_city").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_state").click();
		new VoodooControl("tr", "id", "Accounts_billing_address_postalcode").click();
		nextBtnCtrl.click();

		// Report name and save/run
		new VoodooControl("input", "id", "save_report_as").set(customData.get("name"));
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
		sugar().alerts.waitForLoadingExpiration();
		// Assert Account name, billing street, billing city, billing postal code
		new VoodooControl("a", "css", "#report_results table tr.oddListRowS1 > td:nth-child(1) > a").assertContains(customData.get("account_name2"), true);		
		new VoodooControl("td", "css", "#report_results table tr.oddListRowS1 > td:nth-child(2)").assertContains(customData.get("billingAddressCity"), true); 
		new VoodooControl("td", "css", "#report_results table tr.oddListRowS1 > td:nth-child(3)").assertContains(customData.get("billingAddressState"), true); 
		new VoodooControl("td", "css", "#report_results table tr.oddListRowS1 > td:nth-child(4)").assertContains(customData.get("billingAddressPostalCode"), true);

		// select second account name
		new VoodooControl("input", "css", "#filters > tr > td:nth-child(6) > table > tbody > tr > td:nth-of-type(2) > input").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "body > table.list.view > tbody > tr.evenListRowS1  > td:nth-child(1) > a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "#filters_tab input[title='Apply']").click();
		sugar().alerts.waitForLoadingExpiration();
		// Assert Account name, billing street, billing city, billing postal code
		new VoodooControl("a", "css", "#report_results table tr.oddListRowS1 > td:nth-child(1) > a").assertContains(customData.get("account_name1"), true); 
		new VoodooControl("a", "css", "#report_results table tr.oddListRowS1 > td:nth-child(2)").assertContains(customData.get("billingAddressCity"), true); 
		new VoodooControl("a", "css", "#report_results table tr.oddListRowS1 > td:nth-child(3)").assertContains(customData.get("billingAddressState"), true); 
		new VoodooControl("a", "css", "#report_results table tr.oddListRowS1 > td:nth-child(4)").assertContains(customData.get("billingAddressPostalCode"), true); 
		// reset filter
		new VoodooControl("input", "css", "#filters_tab input[title='Reset']").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}