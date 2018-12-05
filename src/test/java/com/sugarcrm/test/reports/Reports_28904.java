package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_28904 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Current User should be displayed in run-time filter dropdown list in Reports module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_28904_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-822
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		// Create a Rows and Columns Report based on Opportunity
		sugar().reports.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", sugar().opportunities.moduleNamePlural).click();
		
		// In Related Modules, Select Assigned to user & User name in the Available Fields
		new VoodooControl("a", "id", "ygtvlabelel3").click();
		new VoodooControl("tr", "id", "Users_user_name").click();
		
		// Select Is & Current User option from drop down and Select checkbox Run time
		new VoodooControl("select", "css", "[name='qualify']").set("Is");
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#filter_designer_div .yui-module input[type='checkbox']").click();
		new VoodooControl("select", "css", "[title='select filter input']").set("Current User");
		nextBtnCtrl.click();
		
		// Select anything for display columns
		new VoodooControl("tr", "id", "Opportunities_description").click();
		nextBtnCtrl.click();
		
		// Name the report and then click 'Save and run'
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that Current User option should be displayed in run-time filter dropdown list
		new VoodooControl("td", "css", "#reportDetailsTable > tbody > tr:nth-child(1) > td:nth-child(1)").assertContains(testName, true);
		new VoodooControl("select", "css", "#rowid0 > td:nth-child(6) > table > tbody > tr > td > select").assertContains("Administrator", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}