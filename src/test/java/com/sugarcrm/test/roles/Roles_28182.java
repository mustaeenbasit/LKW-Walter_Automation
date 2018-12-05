package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28182 extends SugarTest {
	FieldSet reportData = new FieldSet();

	public void setup() throws Exception {
		reportData = testData.get(testName).get(0);

		// Login to sugar as admin
		sugar().login();

		// Go to Admin > Roles Management and Edit the Role "Tracker"
		sugar().admin.navToAdminPanelLink("rolesManagement");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-580
		new VoodooControl("input", "id", "name_basic").set(reportData.get("trackers").substring(0, (reportData.get("trackers").length() - 1)));
		new VoodooControl("input", "id", "search_form_submit").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Add QAUser to the Role 
		AdminModule.assignUserToRole(reportData);
	}

	/**
	 * Verify that tracker reports should be visible for normal users with tracker role access
	 *
	 * @throws Exception
	 */
	@Test
	public void Roles_28182_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Assign the "My Module Usage (Today)" report to the normal user.
		sugar().reports.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-822
		new VoodooControl("input", "css", "#Reportsadvanced_searchSearchForm input[name='name']").set(reportData.get("reportName"));
		VoodooControl searchBtnCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl firstReport = new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(4) span a");
		firstReport.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", "#detail_header_action_menu .ab").click();
		new VoodooControl("a", "id", "editReportButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#report_type_outline:nth-child(6)").click();
		new VoodooControl("input", "id", "assigned_user_name").set(reportData.get("userName"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#ReportsWizardForm_assigned_user_name_results div").click();
		new VoodooControl("input", "css", "#report_details_div #saveAndRunButton").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Search assigned reports
		sugar().reports.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-822
		new VoodooControl("option", "css", "select[name='assigned_user_id[]'] option:nth-child(3)").click();
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the Tracker Report is present in the List View
		firstReport.assertEquals(reportData.get("reportName"), true);
		// TODO: VOOD-822
		new VoodooControl("td", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(5)").assertEquals(reportData.get("trackers"), true);
		new VoodooControl("td", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(7) a").assertEquals(reportData.get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}