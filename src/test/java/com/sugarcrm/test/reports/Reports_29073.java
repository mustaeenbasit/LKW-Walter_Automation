package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29073 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Assigned User names should be displayed properly in Assigned User column while creating a Report
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_29073_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		// Create Custom(Rows and Columns) Report in Accounts module
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		// Go to Reports -> Create reports -> Rows and Columns -> Accounts 
		sugar().reports.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Accounts").click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Accounts_name").click();
		
		// Select Assigned to User
		new VoodooControl("a", "css", "#module_tree > div > div > div > div > div:nth-child(1) > table > tbody > tr > td.ygtvcell.ygtvcontent > a").click();
		new VoodooControl("tr", "id", "Users_user_name").click();
		nextBtnCtrl.click();

		// Name the report and then click 'Save and run'
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that Assigned User names should be displayed properly like Name instead of ID
		new VoodooControl("td", "css", ".oddListRowS1 td:nth-child(1)").assertContains(sugar().accounts.getDefaultData().get("name"), true);
		new VoodooControl("td", "css", ".oddListRowS1 td:nth-child(2)").assertContains("admin", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}