package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21240 extends SugarTest {
	FieldSet customFS = new FieldSet(), roleRecordData = new FieldSet();
	VoodooControl accessCtrl, accessSelectCtrl, reportSaveBtnCtrl;

	public void setup() throws Exception {
		roleRecordData = testData.get("env_role_setup").get(0);
		customFS = testData.get(testName).get(0);
		sugar().login();

		// Admin -> Role management -> Select a module(Cases) -> Set Access Role to be Enabled
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		accessCtrl =  new VoodooControl("div", "css", "td#ACLEditView_Access_Cases_access div:nth-of-type(2)");
		accessCtrl.click();
		accessSelectCtrl =  new VoodooControl("select", "css", "td#ACLEditView_Access_Cases_access div select");
		accessSelectCtrl.set(customFS.get("accessEnable"));
		reportSaveBtnCtrl = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		reportSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecordData);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify user can run a report while access role is set to Enabled/Not Set
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21240_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		
		// Create a Report and select 'Row and Columns Report'
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-822
		VoodooControl rowsColsImg = new VoodooControl("img", "css", "[name='rowsColsImg']");
		rowsColsImg.click();
		VoodooControl accountModule = new VoodooControl("table", "id", "Cases");
		accountModule.click();
		VoodooControl nextBtn = new VoodooControl("input", "id", "nextBtn");
		nextBtn.click();

		// No filter and select "Full Name", "User Name" of "Assigned to User " for display columns.
		// TODO: VOOD-822
		VoodooControl selectCasesName = new VoodooControl("tr", "id", "Cases_name");
		selectCasesName.click();
		nextBtn.click();

		// Name the report and then click 'Save and run'
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		reportNameCtrl.set(testName);
		VoodooControl reportSaveandRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");
		reportSaveandRunCtrl.click();
		VoodooUtils.waitForReady();
	
		// Verify that the report should be generated without any error message. 
		VoodooControl VerifyCreatedReport = new VoodooControl("td", "css", "#reportDetailsTable [wrap='true']");
		VerifyCreatedReport.assertContains(testName, true);
		VoodooUtils.focusDefault();
		
		// Logout as QAuser and Login as Admin
		sugar().logout();
		sugar().login();
		
		// Go to the setup and change the Access role to be Not Set.  Repeat steps 1 to 4.  
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "roles_management").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "name_basic").set(roleRecordData.get("roleName"));
		new VoodooControl("a", "id", "search_form_submit").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#MassUpdate table tbody tr.oddListRowS1 td:nth-child(3) b a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Updated Access role to be Not Set.
		accessCtrl.click();
		accessSelectCtrl.set(customFS.get("accessNotSet"));
		reportSaveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Create Report again after access set to Not Set
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		rowsColsImg.click();
		accountModule.click();
		nextBtn.click();

		// No filter and select "account name " for display columns.
		selectCasesName.click();
		nextBtn.click();

		// Name the report and then click 'Save and run'
		reportNameCtrl.set(customFS.get("newReport"));
		reportSaveandRunCtrl.click();
		VoodooUtils.waitForReady();
		
		// The report should be generated without any error message after the change.
		VerifyCreatedReport.assertContains(customFS.get("newReport"), true);
		VoodooUtils.focusDefault();
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}