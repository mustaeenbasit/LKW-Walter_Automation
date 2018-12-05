package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21205 extends SugarTest {
	AccountRecord myAccount;
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21205").get(0);
		sugar().login();
	}

	/**
	 * Verify user cannot delete any records while the module delete role is set
	 * to None
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21205_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//TODO - VOOD-688
		AdminModule.createRole(roleRecord);
		// Now on the Access matrix
		// Set the Accounts Access cell to Disabled
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_delete div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_delete div select").set("None");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as qauser to verify no Access to
		// Delete Accounts
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		// Create an Account via the GUI then verify the user can't Delete it
		AccountRecord myAccount = (AccountRecord) sugar().accounts.create();
		myAccount.navToRecord();
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		sugar().accounts.recordView.getControl("deleteButton").assertVisible(
				false);
		sugar().accounts.recordView.getControl("copyButton").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
