package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21223 extends SugarTest {
	FieldSet roleRecordData;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set a few field permissions to "Read Only", like as "Name" and "Industry"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();

		// Set 'Name' field permissions to "Read Only"
		new VoodooControl("div", "id", "namelink").click();
		new VoodooControl("select", "id", "flc_guidname").set(roleRecordData.get("access"));

		// Set 'Industry' field permissions to "Read Only"
		new VoodooControl("div", "id", "industrylink").click();
		new VoodooControl("select", "id", "flc_guidindustry").set(roleRecordData.get("access"));

		// Save Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Field permissions set to Read Only on Accounts - Records can be viewed in report
	 * @throws Exception
	 */
	@Test
	public void Roles_21223_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// TODO: VOOD-822
		VoodooControl rowsAndColumnsReportCtrl = new VoodooControl("td", "css", "img[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		// Go to Reports -> Create reports -> "Rows and Columns". -> Accounts 
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		rowsAndColumnsReportCtrl.click();
		new VoodooControl("table", "id", sugar().accounts.moduleNamePlural).click();
		nextBtnCtrl.click();

		// No filter and select "Name", "Industry" of "Accounts" and "User Name" of "Assigned to User " for display columns.
		// TODO: VOOD-822
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("tr", "id", "Accounts_industry").click();

		// Click "Assigned to User" folder and select field "Full Name" -> Next
		new VoodooControl("a", "css", "#module_tree > div > div > div > div > div:nth-child(1) > table > tbody > tr > td.ygtvcell.ygtvcontent a").click();
		new VoodooControl("tr", "id", "Users_user_name").click();
		nextBtnCtrl.click();

		// Name the report and then click 'Save and run'
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();

		// Verify that the "Name" and "Industry" should display values, the report generates
		VoodooControl firstRecordCtrl = new VoodooControl("tr", "css", ".oddListRowS1");
		firstRecordCtrl.assertContains(sugar().accounts.getDefaultData().get("name"), true);
		firstRecordCtrl.assertContains(roleRecordData.get("administrator"), true);
		firstRecordCtrl.assertContains(sugar().accounts.getDefaultData().get("industry"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}