package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_24698 extends SugarTest {
	public void setup() throws Exception {
		// Creating 2 Accounts records
		sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);

		// Login
		sugar().login();

		// Create a Role
		FieldSet roleData = testData.get("env_role_setup").get(0);
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Set 'Delete' permission to 'None' for Accounts module
		// TODO: VOOD-580
		new VoodooControl("a", "css", "#ACLEditView_Access_Accounts_delete div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_delete div select").set(roleData.get("roleNone"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role and logout
		AdminModule.assignUserToRole(roleData);
		sugar().logout();
	}

	/**
	 * Role management: Verify that a user without delete permission in a module doesn't have 
	 * delete option in list view and record view. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_24698_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Verify no delete option present in row action dropdown
		sugar().accounts.navToListView();
		sugar().accounts.listView.openRowActionDropdown(1);
		sugar().accounts.listView.getControl("delete01").assertExists(false);

		// Verify no delete option present in list view action dropdown
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.getControl("deleteButton").assertVisible(false);

		// Verify no delete option present in record view Primary button dropdown
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.clickRecord(2);
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		sugar().accounts.recordView.getControl("deleteButton").assertVisible(false); 

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}