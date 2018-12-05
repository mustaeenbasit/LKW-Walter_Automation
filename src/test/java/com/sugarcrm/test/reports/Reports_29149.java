package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29149 extends SugarTest {
	VoodooControl activeDropdown, advancedReportingCtrl, viewadvancedReportsCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Blank report is not creating in Advanced Reports module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_29149_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet advancedReportsData = testData.get(testName).get(0);

		// Define controls
		advancedReportingCtrl = sugar().reports.menu.getControl("manageAdvancedReports");

		// TODO: VOOD-1057
		activeDropdown = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "id", "SAVE_HEADER");
		VoodooControl activeDropdownValue = new VoodooControl("a", "css", ".dropdown.active .dropdown-menu.scroll ");
		viewadvancedReportsCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_LIST_REPORTMAKER']");
		VoodooControl advancedReportsFirstRecord = new VoodooControl("a", "css", ".list.view .oddListRowS1 td:nth-child(3) a");
		VoodooControl advancedReportsSecondRecord = new VoodooControl("a", "css", ".list.view .evenListRowS1");
		VoodooControl createadvancedReportsCtrl = new VoodooControl("a", "css", activeDropdownValue.getHookString() + "a[data-navbar-menu-item='LNK_NEW_REPORTMAKER']");

		// Go to Reports Module	
		sugar().reports.navToListView();

		// Click on Reports -> Manage Advanced Reports
		activeDropdown.click();
		advancedReportingCtrl.click();

		// Click on Advanced Reports -> Create Advanced Report
		activeDropdown.click();
		createadvancedReportsCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify the mandatory sign (*) should be displayed at Report Name field
		// TODO: VOOD-1057
		new VoodooControl("sapn", "css", ".edit.view table tr:nth-child(1) td:nth-child(1) span").assertAttribute("class", "required", true);

		// Without Enter values in any field, Click on Save button
		saveBtnCtrl.click();

		// Verify that the Error message should be displayed at Report Name field "Missing required field: Report Name"
		// TODO: VOOD-1057
		new VoodooControl("div", "css", ".edit.view table tr:nth-child(1) td:nth-child(2) div").assertContains(advancedReportsData.get("errorMessage"), true);
		// Wait for JS action to conclude properly. waitForReady() not effective here.
		VoodooUtils.pause(2000); 

		// Enter value in Mandatory fields & Click on Save button
		// TODO: VOOD-1057
		new VoodooControl("input", "css", ".edit.view table tr:nth-child(1) td:nth-child(2) input").set(advancedReportsData.get("reportName"));
		saveBtnCtrl.click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Check Search Advanced Reports list view page
		activeDropdown.click();
		viewadvancedReportsCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the Record should be displayed at list view page
		advancedReportsFirstRecord.assertEquals(advancedReportsData.get("reportName"), true);
		advancedReportsSecondRecord.assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}