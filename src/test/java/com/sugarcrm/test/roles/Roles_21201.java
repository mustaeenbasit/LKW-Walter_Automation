package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Roles_21201 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21201").get(0);
		sugar().login();
	}

	/**
	 * Verify the Quick create dropdown list should respect ACL control
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO: The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21201_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//TODO - VOOD-688
		sugar().admin.createRole(roleRecord);
		// Now on the Access matrix
		// Set the Accounts Access edit cell to None
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_edit div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_edit div select").set("None");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		sugar().admin.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as qauser to verify no Access to
		// Quick create Accounts
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		sugar().navbar.openQuickCreateMenu();
		// Verify Create Contacts exists - for validation of the test
		new VoodooControl("a", "css", "a[data-module='Contacts']").assertExists(true);
		// Verify Create Accounts does not exist
		new VoodooControl("a", "css", "a[data-module='Accounts']").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
