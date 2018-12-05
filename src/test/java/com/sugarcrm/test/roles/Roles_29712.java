package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_29712 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		sugar().accounts.api.create();
		roleRecord = testData.get("env_role_setup").get(0);

		// Login as a Admin user
		sugar().login();

		// Create a role and set permission for Accounts module - Delete : Owner and Edit : Owner
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, Set the Account module - Delete : Owner and Edit : Owner
		// TODO: VOOD-580
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_delete").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_delete select").set(roleRecord.get("roleOwner"));
		new VoodooControl("td", "id", "ACLEditView_Access_Accounts_edit").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_edit select").set(roleRecord.get("roleOwner"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign role to the QAUser
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);

		// Log out from Admin and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that correct error message is displayed while deleting a record if Delete permission is set to "Owner".
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_29712_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet errorMessage = testData.get(testName).get(0);

		// Navigate to Accounts module
		sugar().accounts.navToListView();

		// Select records from the list view -> select delete option from the action drop-down
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that the proper warning message should be displayed
		sugar().alerts.getWarning().assertContains(errorMessage.get("errorMessage"), true);

		// Verify that custom user should not be able to delete selected records
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 