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
public class Roles_17337 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		sugar().login();

		// Create role => Accounts => List=None
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_list").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_list div select").set("None");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify that actions in the megamenu are ACL aware - list set to none
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_17337_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Go to accounts module
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		// Verify 'View accounts' & 'View account reports' is not displayed on dropdown but 'Create' & 'Import' accounts are still displayed
		sugar().accounts.menu.getControl("viewAccounts").assertVisible(false);
		sugar().accounts.menu.getControl("viewAccountReports").assertVisible(false);
		sugar().accounts.menu.getControl("importAccounts").assertVisible(true);
		sugar().accounts.menu.getControl("createAccount").assertVisible(true);

		// to close dropdown
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}