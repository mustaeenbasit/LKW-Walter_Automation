package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21268 extends SugarTest {
	FieldSet customFs;

	public void setup() throws Exception {
		customFs = testData.get(testName).get(0);
		sugar().login();

		// Create a role
		AdminModule.createRole(customFs);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set Access Type = Admin & Import = None
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_admin div select").set(customFs.get("access_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_import div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_import div select").set(customFs.get("import"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into the Role
		AdminModule.assignUserToRole(customFs);

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify no export action is shown while Access Type = Admin & Export = None
	 * @throws Exception
	 */
	@Test
	public void Roles_21268_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts module
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		
		// Clicks on the "caret" to activate the dropdown menu for Accounts module
		sugar().navbar.clickModuleDropdown(sugar().accounts);
		
		// Verify that No Import action is shown in the drop down action list.
		sugar().accounts.menu.getControl("importAccounts").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}