package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_31091 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Accounts module should be display when we set the access rights in Roles module.
	 * @throws Exception
	 */
	@Test
	public void Roles_31091_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet roleRecord = testData.get("env_role_setup").get(0);
		UserRecord qauser = new UserRecord(sugar().users.getQAUser());
		FieldSet userTypeData = testData.get(testName).get(0);
		String accounts = sugar().accounts.moduleNamePlural;
		
		// Creating one role
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Role => Accounts => Access=Disabled
		// TODO: VOOD-580, VOOD-856 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "#ACLEditView_Access_Accounts_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_access div select").set("Disabled");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign a 'Regular User=QAuser' to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		
		// Log out of Sugar as Admin and log in as qauser to verify the denied access to Accounts
		sugar().logout();
		sugar().login(qauser);
		
		// Verify "Acounts" module is not exist for qauser.
		sugar().navbar.assertContains(accounts, false);
		sugar().logout();
		
		// Login as Admin and change user type of qauser from Regular user to Administrator
		sugar().login();
		
		// Navigate to qauser profile
		qauser.navToRecord();
		
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-563 - need lib support for user profile edit page
		// Change qauser type from Regular user to Admin
		new VoodooControl("select", "id", "UserType").set(userTypeData.get("adminUser"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.waitForReady();
		sugar().logout();
		
		// Login as qauser and verifying Account is visible to qauser
		sugar().login(qauser);
		
		// Verifying Account is visible
		sugar().navbar.assertContains(accounts, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}