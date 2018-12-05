package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21220 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21220").get(0);
		sugar().login();
		AdminModule.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and
		// Description have been Saved
	}

	/**
	 * Verify user should not see the list view while the module list role is
	 * set to None
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21220_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set the Accounts import cell to None
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_list div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_list div select").set("None");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		// Log out of Sugar as Admin and log in as QAuser to verify there is no
		// View Accounts option

		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		// TODO - VOOD-658
		sugar().navbar.clickModuleDropdown(sugar().accounts);
		new VoodooControl("a", "css",
				"li[data-module='Accounts'] a[data-navbar-menu-item='LNK_ACCOUNT_LIST']")
				.assertVisible(false);
		// Verify the other options exist
		new VoodooControl("a", "css",
				"li[data-module='Accounts'] a[data-navbar-menu-item='LNK_IMPORT_ACCOUNTS']")
				.assertVisible(true);
		new VoodooControl("a", "css",
				"li[data-module='Accounts'] a[data-navbar-menu-item='LNK_NEW_ACCOUNT']")
				.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
