package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21203 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify user can't access to a module while the module access is set to Disabled
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21203_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Now on the Access matrix, Set the Accounts Access cell to Disabled
		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_access div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_access div select")
				.set("Disabled");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		
		// Log out of Sugar as Admin and log in as qauser to verify the denied access to Accounts
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		// Verify "Acounts" module is not exist for qauser.
		sugar().navbar.assertContains("Accounts", false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
