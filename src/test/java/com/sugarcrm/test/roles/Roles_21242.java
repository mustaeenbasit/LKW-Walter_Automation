package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21242 extends SugarTest {
	FieldSet roleRecordData; 

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set its Delete role to be Owner
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_delete div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_delete div select").set(roleRecordData.get("delete"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify user can not delete not-owned records while the module delete role is set to be Owner
	 * @throws Exception
	 */
	@Test
	public void Roles_21242_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		sugar().accounts.listView.confirmDelete();

		// Verify that the record NOT assigned to the selected user should NOT be deleted and should be still shown in the list view. 
		sugar().accounts.listView.verifyField(1, "name", sugar().accounts.getDefaultData().get("name"));

		// Go to Accounts recordView
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.openPrimaryButtonDropdown();

		// Verify that the delete button should not be shown on the drop down action menu from the detail view.
		sugar().accounts.recordView.getControl("deleteButton").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
