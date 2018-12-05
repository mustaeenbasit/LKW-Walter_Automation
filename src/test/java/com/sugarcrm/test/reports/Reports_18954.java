package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Reports_18954 extends SugarTest {
	FieldSet roleRecordData = new FieldSet();
	VoodooControl nameCtrl, advanceSearchCtrl, deleteCtrl,massDelete;

	public void setup() throws Exception {
		sugar().login();

		// User is now on the ACL Matrix screen after the Role and Description has been saved
		roleRecordData = testData.get(testName+"_roles").get(0);
		AdminModule.createRole(roleRecordData);
	}

	/**
	 * ACL_Report_Owner
	 * @throws Exception
	 */
	@Test
	public void Reports_18954_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Now on the Access matrix - Click the Reports module and delete,view, export permissions to owner
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-856
		new VoodooControl("td", "id", "ACLEditView_Access_Reports_delete").click();
		new VoodooControl("option", "css", "#ACLEditView_Access_Reports_delete option:nth-of-type(2)").click();
		new VoodooControl("td", "id", "ACLEditView_Access_Reports_export").click();
		new VoodooControl("option", "css", "#ACLEditView_Access_Reports_export option:nth-of-type(2)").click();
		new VoodooControl("td", "id", "ACLEditView_Access_Reports_view").click();
		new VoodooControl("option", "css", "#ACLEditView_Access_Reports_view option:nth-of-type(2)").click();

		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecordData);

		// Log out of sugar() as Admin and log in as QAuser
		sugar().logout();

		sugar().login(sugar().users.getQAUser());

		// Reports module
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");

		// TODO: VOOD-822
		VoodooControl createRowsAndColumnsReportCtrl = new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(1) table tbody tr:nth-child(1) td");
		VoodooControl accountsModuleCtrl = new VoodooControl("table", "id", "Accounts");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");

		// Create Rows and Columns report, select Account module
		VoodooUtils.focusFrame("bwc-frame");
		createRowsAndColumnsReportCtrl.click();
		accountsModuleCtrl.click();
		nextBtnCtrl.click();

		// TODO: VOOD-822
		// Displayed columns: Account > Name 
		new VoodooControl("tr", "id", "Accounts_name").click();
		nextBtnCtrl.click();

		// Report name and save
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "assigned_user_name").set(customData.get("assigned_user"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#ReportsWizardForm_assigned_user_name_results div").click();
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Verifying error message
		new VoodooControl("td", "css", "#content td").assertContains(customData.get("error_msg1"), true);
		VoodooUtils.focusDefault();

		// Verifying report in list view
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);

		// TODO: VOOD-822
		VoodooUtils.focusFrame("bwc-frame");
		nameCtrl = new VoodooControl("input", "css", ".edit table tr:nth-of-type(1) td:nth-of-type(2) input");
		nameCtrl.set(testName);
		advanceSearchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		advanceSearchCtrl.click();
		massDelete = new VoodooControl("input", "css", ".checkbox.massall");
		massDelete.click();
		deleteCtrl = new VoodooControl("input", "id", "delete_listview_bottom");
		deleteCtrl.click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();

		// TODO: VOOD-822
		// Don't have privilege to delete the record
		// Verifying record is displayed when delete a record
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(4) span a").assertEquals(testName, true);

		// Asserting the error message when deleting the record
		new VoodooControl("span", "css", "#contentTable span").assertEquals(customData.get("error_msg2"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}