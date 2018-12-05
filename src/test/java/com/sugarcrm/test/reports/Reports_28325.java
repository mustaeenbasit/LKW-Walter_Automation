package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_28325 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify non-admin user should not have any edit and delete accesses in the advanced reports 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_28325_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Reports -> Manage Advanced reports
		sugar().navbar.selectMenuItem(sugar().reports, "manageAdvancedReports");

		// Define controls for 'Manage Advanced reports'
		// TODO: VOOD-1057
		VoodooControl manageReportCaretDropdown = new VoodooControl("button", "css", "[data-module='ReportMaker'] button[data-toggle='dropdown']");
		VoodooControl createadvancedReportsCtrl = new VoodooControl("a", "css", "li.active a[data-navbar-menu-item='LNK_NEW_REPORTMAKER']");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "report_name");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "id", "SAVE_HEADER");
		VoodooControl advancedReportsFirstRecord = new VoodooControl("a", "css", ".list.view .oddListRowS1 td:nth-child(3) a");
		VoodooControl editBtnCtrl = new VoodooControl("input", "css", "input[title='Edit']");
		VoodooControl deleteBtnCtrl = new VoodooControl("input", "css", "input[title='Delete']");
		VoodooControl addBtnCtrl = new VoodooControl("input", "css", "input[title='Add']");
		VoodooControl runReportBtnCtrl = new VoodooControl("input", "css", "input[title='Run Report']");
		VoodooControl selectBtnCtrl = new VoodooControl("input", "css", "input[title='Select']");

		// Create a new advance report with a data format
		manageReportCaretDropdown.click();
		createadvancedReportsCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		reportNameCtrl.set(testName);
		saveBtnCtrl.click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Login as non-admin user (i.e QAUser)
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Reports -> Manage Advanced reports
		sugar().navbar.selectMenuItem(sugar().reports, "manageAdvancedReports");
		VoodooUtils.focusFrame("bwc-frame");

		// Click the newly created advance report
		advancedReportsFirstRecord.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Non-admin user should not have any edit and delete access in the advance report
		editBtnCtrl.assertExists(false);
		deleteBtnCtrl.assertExists(false);
		addBtnCtrl.assertExists(false);

		// Verify that under advance report: only "Run Report" button is shown
		runReportBtnCtrl.assertVisible(true);

		// Verify that under Data Format: only "Select" button is shown
		selectBtnCtrl.assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}