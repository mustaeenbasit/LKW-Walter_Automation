package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_27705 extends SugarTest{
	FieldSet customData;
	VoodooControl reportModuleCtrl, nameCtrl, advanceSearchCtrl;

	public void setup() throws Exception {
		sugar().contacts.api.deleteAll();
		sugar().accounts.api.deleteAll();
		customData = testData.get(testName).get(0);
		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("account_name1"));
		fs.put("billingAddressCity", customData.get("account_city_code1"));
		sugar().accounts.api.create(fs);
		fs.clear();
		fs.put("name", customData.get("account_name2"));
		fs.put("billingAddressCity", customData.get("account_city_code2"));
		sugar().accounts.api.create(fs);
		sugar().login();
		
		sugar().navbar.navToModule(customData.get("module_plural_name"));

		// TODO: VOOD-822 -need lib support of reports module
		advanceSearchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl createReportCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li");
		VoodooControl createRowsAndColumnsReportCtrl = new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(3) table tbody tr:nth-child(1) td");
		VoodooControl accountsModuleCtrl = new VoodooControl("table", "id", "Accounts");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");

		// Create Rows and Columns report, select Account module
		VoodooUtils.focusDefault();
		reportModuleCtrl.click();
		createReportCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.click();
		accountsModuleCtrl.click();
	
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("select", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(3) > select").set("Is Not Empty");
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(4) > table > tbody > tr > td:nth-child(2) > input[type='checkbox']").click(); // Click on Run-Time checkbox
		
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > div > table [name='Add Filter Group']").click(); // click on Add Filter 
		new VoodooControl("tr", "id", "Accounts_phone_office").click();
		new VoodooControl("li", "css", "#mycontextmenu > div.bd > ul > li:nth-child(3)").click(); // Select filter group
		new VoodooControl("select", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(2) > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(3) > select").set("Does Not Equal"); // choose select value 
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(2) > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(4) [name='text_input']").set("000-000-0000"); // set input value
		
		// add Industry
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > div > table [name='Add Filter Group']").click(); // click on Add Filter 
		new VoodooControl("tr", "id", "Accounts_industry").click();
		new VoodooControl("li", "css", "#mycontextmenu > div.bd > ul > li:nth-child(4)").click();
		new VoodooControl("select", "css", "#filter_designer_div div.bd > table tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(3) > div.bd > div > table tr > td:nth-child(2) > select").set("OR");
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(3) > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(4) [type='checkbox']").click();
		
		// Add sub-industry
		new VoodooControl("select", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(3) > div.bd > div > table [name='Add Filter Group']").click();
		new VoodooControl("tr", "id", "Accounts_industry").click();
		new VoodooControl("li", "css", "#mycontextmenu > div.bd > ul > li:nth-child(5)").click();
		new VoodooControl("select", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(3) > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div > div.bd > div > table > tbody > tr > td:nth-child(2) > select").set("OR");
		new VoodooControl("select", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(3) > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(4) > table > tbody > tr > td:nth-child(1) > select").set("Banking");
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(3) > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(4) > table > tbody > tr > td:nth-child(2) > input[type='checkbox']").click();
				
		// Add billing address city
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > div > table [name='Add Filter Group']").click(); // click on Add Filter 
		new VoodooControl("tr", "id", "Accounts_billing_address_city").click();
		new VoodooControl("li", "css", "#mycontextmenu > div.bd > ul > li:nth-child(6)").click();
		new VoodooControl("select", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(4) > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(3) > select").set("Does Not Contain"); // choose select value
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(4) > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(4) [name='text_input']").set("132"); // set input value
		new VoodooControl("input", "css", "#filter_designer_div > div > div.bd > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > div:nth-child(4) > div.bd > table > tbody > tr:nth-child(1) > td:nth-child(2) > table > tbody > tr > td:nth-child(4) [type='checkbox']").click();
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		
		// Define Group By
		new VoodooControl("tr", "id", "Accounts_count").click();
		nextBtnCtrl.click();
		
		// Displayed columns: Account > Name, Billing Street 
		new VoodooControl("tr", "id", "Accounts_name").click();		
		nextBtnCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div > table:nth-child(1) input#nextButton").click();

		// Report name and save
		new VoodooControl("input", "id", "save_report_as").set(customData.get("name"));
		new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton").click();
	}

	/**
	 * Verify report should not display database failure when report filters are in their own group after upgrade
	 * @throws Exception
	 */
	@Test
	public void Reports_27705_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Verify account name
		new VoodooControl("a", "css", "#report_results > table.reportlistView > tbody > tr:nth-child(2) > td > a").assertEquals(customData.get("account_name1"), true);
		
		// Verify count 1
		new VoodooControl("a", "css", "#report_results > table.list.view > tbody > tr:nth-child(2) > td").assertEquals("1", true);		
		
		// Edit report
		new VoodooControl("span", "css", "#detail_header_action_menu > li > span").click();
		new VoodooControl("li", "css", "#detail_header_action_menu > li > ul > li:nth-child(2)").click();
		new VoodooControl("input", "id", "saveAndRunBtn").click();
		
		// Verify account name
		new VoodooControl("a", "css", "#report_results > table.reportlistView > tbody > tr:nth-child(2) > td > a").assertEquals(customData.get("account_name1"), true);
		
		// Verify count 1
		new VoodooControl("a", "css", "#report_results > table.list.view > tbody > tr:nth-child(2) > td").assertEquals("1", true);		
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}