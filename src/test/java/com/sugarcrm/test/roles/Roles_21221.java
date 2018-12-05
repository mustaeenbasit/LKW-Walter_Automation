package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21221 extends SugarTest {

	public void setup() throws Exception {
		FieldSet rolesData = testData.get("env_role_setup").get(0);
		FieldSet customFS = testData.get(testName).get(0);
		sugar().login();

		// Create a role
		AdminModule.createRole(rolesData);
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Owner Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();

		// Set a few field permissions to "Owner read/Owner Write", like as "Name" and "Industry".
		new VoodooControl("div", "id", customFS.get("fields1")).click();
		new VoodooControl("select", "id", customFS.get("permissions1")).set(rolesData.get("roleOwnerRead/OwnerWrite"));
		new VoodooControl("div", "id", customFS.get("fields2")).click();
		new VoodooControl("select", "id", customFS.get("permissions2")).set(rolesData.get("roleOwnerRead/OwnerWrite"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(rolesData);

		// Logout from Admin and Login with QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Field permissions set to Owner read/Owner Write on Accounts - Owned records can be viewed in report
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21221_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Account record via UI
		sugar().accounts.create();

		// Create a Report and select 'Row and Columns Report'
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		VoodooControl rowsColsImg = new VoodooControl("img", "css", "[name='rowsColsImg']");
		rowsColsImg.click();
		VoodooControl accountModule = new VoodooControl("table", "id", "Accounts");
		accountModule.click();
		VoodooControl nextBtn = new VoodooControl("input", "id", "nextBtn");
		nextBtn.click();

		// No filter and select "Name", "Industry" and "User Name" of "Assigned to User " for display columns.
		// TODO: VOOD-822
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("tr", "id", "Accounts_industry").click();
		new VoodooControl("a", "css", "#module_tree > div > div > div > div > div:nth-child(1) > table > tbody > tr > td.ygtvcell.ygtvcontent a").click();
		new VoodooControl("tr", "id", "Users_user_name").click();
		nextBtn.click();

		// Name the report and then click 'Save and run'
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "input[name='Save and Run']").click();
		VoodooUtils.waitForReady();

		// Verify that the "Name" and "Industry" should display values from the record which is assigned to "QAuser".
		new VoodooControl("td", "css", "#report_results .oddListRowS1 .oddListRowS1").assertContains(sugar().accounts.getDefaultData().get("name"), true);
		new VoodooControl("td", "css", "#report_results .oddListRowS1 .oddListRowS1:nth-child(2)").assertContains(sugar().accounts.getDefaultData().get("industry"), true);
		new VoodooControl("tr", "css", "#report_results .oddListRowS1 .oddListRowS1:nth-child(3)").assertContains(sugar().users.qaUser.get("userName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
