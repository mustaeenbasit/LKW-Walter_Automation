package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21213 extends SugarTest {
	DataSource roleRecordData;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName);
		FieldSet accountName = new FieldSet();
		accountName.put("name", roleRecordData.get(0).get("name"));
		sugar().accounts.api.create(accountName);
		accountName.clear();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData.get(0));
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		for(int i = 0; i < roleRecordData.size(); i++) {
			// Need to click again on fieldCtrl if permissionsCtrl is not visible as some time script fails to click.
			VoodooControl fieldCtrl = new VoodooControl("div", "id", roleRecordData.get(i).get("fields"));
			VoodooControl permissionsCtrl = new VoodooControl("select", "id", roleRecordData.get(i).get("permissions"));
			fieldCtrl.click();
			if(!permissionsCtrl.queryVisible())
				fieldCtrl.click();
			permissionsCtrl.set(roleRecordData.get(0).get("access"));
		}

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData.get(0));

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Field permissions set to Read Owner Write on Accounts - Owned and non-owned records can be viewed in reports
	 * @throws Exception
	 */
	@Test
	public void Roles_21213_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Create an Account(Owned record for QAUser)
		sugar().accounts.create();

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

		// On the 'Define Filters' step, select 'Name' and 'Is not empty' and click to next. 
		// TODO: VOOD-822
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("option", "css", "#filter_designer_div div.bd tr:nth-child(1) td:nth-child(2) tr td:nth-child(3) select option:nth-of-type(10)").click();
		nextBtnCtrl.click();

		// Choose Display Columns: click on "Name","Description".
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("tr", "id", "Accounts_description").click();

		// Click "Assigned to User" folder and select field "Full Name" -> Next
		new VoodooControl("a", "css", "#module_tree > div > div > div > div > div:nth-child(1) > table > tbody > tr > td.ygtvcell.ygtvcontent a").click();
		new VoodooControl("tr", "id", "Users_full_name").click();
		nextBtnCtrl.click();

		// Name the report and then click 'Save and run'
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();

		// Verify that all records are displayed properly (owned and non-owned)
		VoodooControl firstRecordCtrl = new VoodooControl("tr", "css", ".oddListRowS1");
		VoodooControl secondRecordCtrl = new VoodooControl("tr", "css", ".evenListRowS1");
		firstRecordCtrl.assertContains(roleRecordData.get(0).get("name"), true);
		firstRecordCtrl.assertContains(roleRecordData.get(0).get("administrator"), true);
		secondRecordCtrl.assertContains(sugar().accounts.getDefaultData().get("name"), true);
		secondRecordCtrl.assertContains(sugar().users.getQAUser().get("userName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}