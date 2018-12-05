package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest; 

public class Reports_26745 extends SugarTest {
	FieldSet customData;
	DataSource ds;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();
		
		ds = testData.get(testName+"_1");
		customData = testData.get(testName).get(0);
		
		FieldSet fs = new FieldSet();
		for(int i = 0; i < ds.size(); i++) {
			fs.put("name", ds.get(i).get("account_name"));
			fs.put("billingAddressStreet", ds.get(i).get("billing_street"));
			sugar().accounts.api.create(fs);
		}
	}

	/**
	 * Address field in report.
	 * @throws Exception
	 */
	@Test
	public void Reports_26745_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 
		
		// TODO: VOOD-822
		VoodooControl reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl rowColumsReportCtrl = new VoodooControl("img", "css", "img[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");
		
		// Create Custom Report in RLI module, select the created custom field
		sugar().navbar.navToModule("Reports");
		reportModuleCtrl.click();
		new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)").click();
		VoodooUtils.focusFrame("bwc-frame");
		rowColumsReportCtrl.waitForVisible();
		rowColumsReportCtrl.click();
		new VoodooControl("table", "id", "Accounts").click();		
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "Accounts_name").click();
		new VoodooControl("input", "id", "Accounts_billing_address_street").click();
		nextBtnCtrl.click();
		reportNameCtrl.set(customData.get("report_name"));
		saveAndRunCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("a", "css", "#report_results > div > table > tbody > tr:nth-child(2) > th:nth-child(1) > a").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Assert list of accounts
		for(int i = 0; i < ds.size(); i++) {//account_name,billing_street
			// TODO: VOOD-822
			new VoodooControl("td", "css", "#report_results > div > table > tbody > tr:nth-child("+(3+i)+")").assertContains(ds.get(i).get("billing_street"), true);
			new VoodooControl("td", "css", "#report_results > div > table > tbody > tr:nth-child("+(3+i)+")").assertContains(ds.get(i).get("account_name"), true);
		}	
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}
		
	public void cleanup() throws Exception {}
}