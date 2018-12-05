package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21247 extends SugarTest {
	FieldSet roleRecordData;
	DataSource accountDS;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName+"_role").get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set Import role to be All
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_import div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_import div select").set(roleRecordData.get("import"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify user can import while the module import role is set to All/Not Set
	 * @throws Exception
	 */
	@Test
	public void Roles_21247_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		
		// Go to Accounts record listView
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		// Verify that the import action should be shown when click on the selected module. 
		VoodooControl importLinkCtrl = sugar().accounts.menu.getControl("importAccounts");
		importLinkCtrl.assertExists(true);

		// Click on Import link
		importLinkCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the import page module title
		VoodooControl importModuleTitle = new VoodooControl("h2", "css", " .moduleTitle h2");
		importModuleTitle.assertContains(customFS.get("importModuleTitle"), true);
		VoodooUtils.focusDefault();
		
		// Logout as QAUser and Login as Admin
		sugar().logout();
		sugar().login();

		// TODO: VOOD-856
		// Go to Role Management and change the Import role to be "Not Set"
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "roles_management").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "name_basic").set(roleRecordData.get("roleName"));
		new VoodooControl("a", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "#MassUpdate table tbody tr.oddListRowS1 td:nth-child(3) b a").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_import div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_import div select").set(customFS.get("setDefaultRole")); // Set default

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout as Admin & Login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Accounts record listView
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		// Verify that the import action should be shown when click on the selected module. 
		sugar().accounts.menu.getControl("importAccounts").assertExists(true);

		// Click on Import link
		sugar().accounts.menu.getControl("importAccounts").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1396 - Need Controls for the Import Tasks functionality
		// Verify that the import page title
		importModuleTitle.assertContains(customFS.get("importModuleTitle"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}