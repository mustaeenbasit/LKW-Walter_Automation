package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21222 extends SugarTest {
	FieldSet roleRecordData = new FieldSet();

	public void setup() throws Exception {
		roleRecordData = testData.get("env_role_setup").get(0);
		FieldSet customFS = testData.get(testName).get(0);

		// Create two Accounts record with different name
		FieldSet  fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().accounts.api.create();
		sugar().login();

		// Assigned the Contact record to QAUser(Owned record for QAUser)
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1, "relAssignedTo").set(roleRecordData.get("userName"));
		sugar().accounts.listView.saveRecord(1);

		// Create a role myRole
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// For Contacts module, Select a module(contact) and set its List role to be Owner
		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("a", "css", ".edit.view tbody tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".detail.view tbody tr:nth-child(2) td:nth-child(2) div.aclNot.Set").click();
		new VoodooControl("select", "css", ".detail.view tbody tr:nth-child(2) td:nth-child(2) select.aclNot.Set").set(customFS.get("viewRole"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".detail.view tbody tr:nth-child(2) td:nth-child(7) div.aclNot.Set").click();
		new VoodooControl("select", "css", ".detail.view tbody tr:nth-child(2) td:nth-child(7) select.aclNot.Set").set(customFS.get("listRole"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Access type = "Admin" display all records in listview when "List" set to "Owner" for module level ACL
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21222_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// TODO: VOOD-822
		// Go to Reports -> Create reports -> "Rows and Columns". -> Accounts 
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "img[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Accounts").click();
		VoodooControl nextBtn = new VoodooControl("input", "id", "nextBtn");
		nextBtn.click();

		// No filter and select "Full Name", "User Name" of "Assigned to User " for display columns.
		// TODO: VOOD-822
		// Click "Assigned to User" folder and select field "Full Name" -> Next
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("a", "css", "#module_tree > div > div > div > div > div:nth-child(1) > table > tbody > tr > td.ygtvcell.ygtvcontent a").click();
		new VoodooControl("tr", "id", "Users_full_name").click();
		new VoodooControl("tr", "id", "Users_user_name").click();
		nextBtn.click();

		// Name the report and then click 'Save and run'
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "input[name='Save and Run']").click();
		VoodooUtils.waitForReady();

		// Verify that it display owned records only
		new VoodooControl("tr", "css", ".oddListRowS1 td").assertContains(sugar().accounts.getDefaultData().get("name"), true);
		new VoodooControl("tr", "css", ".oddListRowS1 td:nth-child(2)").assertContains(roleRecordData.get("userName"), true);
		
		// Verify that the second row not visible
		new VoodooControl("tr", "css", ".evenListRowS1").assertExists(false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}