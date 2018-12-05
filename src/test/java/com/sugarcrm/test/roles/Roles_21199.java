package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Roles_21199 extends SugarTest {
	FieldSet roleRecord, customData;
	VoodooControl reportNameInput, advancedSearchBtn;

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		customData = testData.get(testName).get(0);
		reportNameInput = new VoodooControl("input", "name", "name");
		advancedSearchBtn = new VoodooControl("input", "id", "search_form_submit_advanced");
		sugar().login();

		// Create role => Reports => accessType=Admin & Developer => delete=None => Edit=owner
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Reports_admin").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Reports_admin div select").set(customData.get("access_type"));
		new VoodooControl("td", "css", "#ACLEditView_Access_Reports_delete").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Reports_delete div select").set(customData.get("delete"));
		new VoodooControl("td", "css", "#ACLEditView_Access_Reports_edit").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Reports_edit div select").set(customData.get("edit"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();

		// Navigate to report module, Create Rows and Columns report, select Account module, assignedTo non-admin user
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(1) table tbody tr:nth-child(1) td").click();
		new VoodooControl("table", "id", "Accounts").click();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Accounts_name").click();
		nextBtnCtrl.click();

		// Report name and save
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "assigned_user_name").set(sugar().users.getQAUser().get("userName"));
		new VoodooControl("li", "css", "#ReportsWizardForm_assigned_user_name_results ul li").waitForVisible(); // required for auto search to be completed
		new VoodooControl("input", "css", "#report_details_div #saveAndRunButton").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().logout();
	}

	/**
	 * Verify report can only be edited by owner if Edit report privilege is Owner in the role
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_21199_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);

		// Verify create report option is available
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").assertVisible(true);

		// view reports 
		sugar().reports.menu.getControl("viewReports").click();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		// Verify no edit icon for non-owned report 
		VoodooControl editIcon = new VoodooControl("a", "css", "tr.oddListRowS1 td a[title='Edit']");
		editIcon.assertVisible(false);

		// searching owner reports only
		reportNameInput.set(testName);
		advancedSearchBtn.click();

		// Verify edit icon for owned report
		editIcon.click(); // visibility check under click method, if visibility of an element not found it will throw an Candybean Exception
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame"); // need bwc-frame focus again

		// Verify display a edit view for report
		new VoodooControl("h2", "css", "div.moduleTitle h2").assertEquals(customData.get("module_title"), true);
		new VoodooControl("input", "id", "cancelBtn").click();
		VoodooUtils.waitForReady();

		// Detailed non-owner report
		new VoodooControl("a", "css", "#MassUpdate table tbody tr:nth-of-type(3).oddListRowS1 a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame"); // need bwc-frame focus again

		// Verify edit option is not available on dropdown
		VoodooControl openPrimaryButtonDropdown = new VoodooControl("span", "css", "#detail_header_action_menu span.ab");
		openPrimaryButtonDropdown.click();
		new VoodooControl("a", "id", "editReportButton").assertVisible(false);
		openPrimaryButtonDropdown.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}