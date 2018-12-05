package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28121 extends SugarTest {
	VoodooControl salesAdministrator, accessTypeCtrl, accessTypeValueCtrl, saveRoleBtnCtrl; 
	FieldSet testDataFS = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		testDataFS = testData.get(testName).get(0);

		// Login to sugar as admin
		sugar().login();

		// Go to Admin > Roles Management and Edit the Role "Sales Administrator"
		sugar().admin.navToAdminPanelLink("rolesManagement");
		VoodooUtils.focusFrame("bwc-frame");

		// Click on 'Opportunities'
		// TODO: VOOD-580
		salesAdministrator = new VoodooControl("a", "css", "table.list.view tbody tr:nth-of-type(5) td:nth-of-type(3) a");
		salesAdministrator.click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Change the Opportunities Module to the following: Edit: Owner View: Owner Access Type: Normal
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Opportunities_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Opportunities_edit div select").set(testDataFS.get("owner"));
		new VoodooControl("div", "css", "td#ACLEditView_Access_Opportunities_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Opportunities_view div select").set(testDataFS.get("owner"));
		accessTypeCtrl = new VoodooControl("div", "css", "td#ACLEditView_Access_Opportunities_admin div:nth-of-type(2)");
		accessTypeValueCtrl = new VoodooControl("select", "css", "td#ACLEditView_Access_Opportunities_admin div select");
		saveRoleBtnCtrl = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		accessTypeCtrl.click();
		accessTypeValueCtrl.set(testDataFS.get("normal"));

		// Save the Role
		saveRoleBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Add QAUser to the Role 
		AdminModule.assignUserToRole(testDataFS);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Create an Opportunity assigned to QAUser
		// TODO: VOOD-444
		FieldSet oppName = new FieldSet();
		oppName.put("name", testDataFS.get("oppName"));
		sugar().opportunities.create(oppName);
		if(sugar().alerts.getSuccess().queryVisible()) { 
			sugar().alerts.getSuccess().closeAlert();
		}
	}

	/**
	 *  Verify that Regular User that was switched to Admin is not being Role Restricted within Reports Module.
	 *
	 * @throws Exception
	 */
	@Test
	public void Roles_28121_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Rows and Columns Report, for Opportunities 
		// Go to the reports module 
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// Define Controls
		// TODO: VOOD-822
		VoodooControl rolesAndColumnsReport = new VoodooControl("td", "css", "img[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl opportunitiesNameCtrl = new VoodooControl("tr", "id", "Opportunities_name");
		VoodooControl assignToUserCtrl = new VoodooControl("tr", "id", "Users_user_name");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		// Create report and select Rows and columns report
		rolesAndColumnsReport.click();
		VoodooUtils.waitForReady();

		// Choose the selected module of the setup 
		new VoodooControl("table", "id", sugar().opportunities.moduleNamePlural).click();
		VoodooUtils.waitForReady();

		// Follow the report wizard to run a report
		// Define Filters : Without filters
		nextBtnCtrl.click();

		// Choose Display Columns: Name
		opportunitiesNameCtrl.scrollIntoView();
		opportunitiesNameCtrl.click();
		new VoodooControl("a", "css", "#module_tree div:nth-of-type(2) td.ygtvcell.ygtvcontent a").click();
		assignToUserCtrl.scrollIntoView();
		assignToUserCtrl.click();
		nextBtnCtrl.click();
		// Select Name file to show on the detail list. Save and Run Report.
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		VoodooUtils.waitForReady(30000); // Needed extra wait

		// Verify that it's only returning all Opportunities that are only assigned to QAUser
		// TODO: VOOD-822
		VoodooControl oppNameCtrl = new VoodooControl("a", "css", ".listViewBody .oddListRowS1 a");
		VoodooControl secondOppListCtrl = new VoodooControl("a", "css", ".evenListRowS1 a");
		VoodooControl userAssignedToCtrl = new VoodooControl("a", "css", ".listViewBody .oddListRowS1 td:nth-child(2) a");
		oppNameCtrl.assertEquals(testDataFS.get("oppName"), true);
		userAssignedToCtrl.assertEquals(testDataFS.get("userName"), true);
		secondOppListCtrl.assertExists(false);
		VoodooUtils.focusDefault();

		// Log out from QAUser user and log in as Admin 
		sugar().logout();
		sugar().login();

		// Go to Admin -> Roles Management
		sugar().admin.navToAdminPanelLink("rolesManagement");
		VoodooUtils.focusFrame("bwc-frame");

		// Edit the Role "Sales Administrator" 
		// TODO: VOOD-580
		salesAdministrator.click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Change the Opportunities Module to the following: Access Type: Admin 
		accessTypeCtrl.click();
		accessTypeValueCtrl.set(testDataFS.get("admin"));

		// Save the Role
		saveRoleBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Log out from Admin user and again log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Run the report again
		sugar().reports.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-822
		new VoodooControl("input", "css", "#Reportsadvanced_searchSearchForm input[name='name']").set(testName);
		new VoodooControl("input", "id", "search_form_submit_advanced").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 td:nth-child(4) span a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "runReportButton").click();
		VoodooUtils.waitForReady(30000);

		// Verify that QAUser is now able to see all Opportunities that exist in the System
		oppNameCtrl.assertEquals(sugar().opportunities.getDefaultData().get("name"), true);
		secondOppListCtrl.assertEquals(testDataFS.get("oppName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}