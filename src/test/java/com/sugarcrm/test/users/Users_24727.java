package com.sugarcrm.test.users;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Users_24727 extends SugarTest {
	FieldSet roleData = new FieldSet();

	public void setup() throws Exception {
		roleData = testData.get(testName).get(0);

		// Log-In as an Admin
		sugar().login();

		// Navigate to the Accounts module
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		VoodooControl nameEditField = sugar().accounts.createDrawer.getEditField("name");
		VoodooControl teamEditField = sugar().accounts.createDrawer.getEditField("relTeam");

		// TODO: VOOD-444 - Support creating relationships via API
		// Account records in different team exist.
		// Create two Account records, One with primary team = East and other with primary team = West
		for (int i = 2; i > 0; i --) {
			sugar().accounts.listView.create();
			nameEditField.set(roleData.get("team" + i));
			sugar().accounts.createDrawer.showMore();
			teamEditField.set(roleData.get("team" + i));
			sugar().accounts.createDrawer.save();
		}
	}

	/**
	 * User-Role_Verify that non-admin user could access all records in corresponding module when set admin as access type for the module.
	 * @throws Exception
	 */
	@Test
	public void Users_24727_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Role for Accounts with permission "Access Type"= Admin 
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 - Create a Roles (ACL) Module LIB
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_admin").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_admin select").set(roleData.get("permissionAccessTypeAdmin"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.focusDefault();

		// Assign the role created above to qaUser
		AdminModule.assignUserToRole(sugar().users.qaUser);

		// Log-out from admin user
		sugar().logout();

		// Log-in as qaUser
		sugar().login(sugar().users.qaUser);

		// Navigate to the Accounts module 
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		// Click the first record to navigate to its record view
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		VoodooControl nameDetailField = sugar().accounts.recordView.getDetailField("name");
		VoodooControl temaDetailField = sugar().accounts.recordView.getDetailField("relTeam");

		// Assert that all records in accounts module can be accessed by qauser.
		for (int i = 1; i <= 2; i ++) {
			nameDetailField.assertEquals(roleData.get("team" + i), true);
			temaDetailField.assertContains(roleData.get("team" + i), true);
			temaDetailField.assertContains(roleData.get("team3"), false);
			sugar().accounts.recordView.gotoNextRecord();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}