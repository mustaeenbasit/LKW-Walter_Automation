package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21230 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21230").get(0);
		sugar().login();
		sugar().accounts.api.create();
		AdminModule.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and
		// Description have been Saved
	}

	/**
	 * Verify user cannot see the detail view page while the View role is set to
	 * None
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21230_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set the Accounts View cell to None
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_view div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_view div select").set("None");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as QAuser to verify Account
		// Record is not Viewable
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		// Verify the Account Name is not a link, which implies the Account is
		// not viewable from the List
		new VoodooControl("a", "css", ".fld_name.list a").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
