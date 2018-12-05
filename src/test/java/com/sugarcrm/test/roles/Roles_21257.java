package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Roles_21257 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21257").get(0);
		sugar().login();
	}

	/**
	 * Verify user should have the admin & developer studio privilege while the
	 * access type is set to "Admin & developer"
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21257_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//TODO - VOOD-688
		sugar().admin.createRole(roleRecord);
		// Now on the Access matrix
		// Set the Accounts Access Type cell to Developer
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_admin div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_admin div select")
				.set("Admin & Developer");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		sugar().admin.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as qauser to verify the Admin -
		// Studio Account access
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		new VoodooControl("a", "id", "studiolink_Accounts").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
