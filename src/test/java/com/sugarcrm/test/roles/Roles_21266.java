package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21266 extends SugarTest {
	FieldSet roleRecordData;

	public void setup() throws Exception {
		roleRecordData = testData.get("env_role_setup").get(0);
		FieldSet customFs = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set its Edit role to be Owner
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_admin div select").set(customFs.get("access_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_export div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_export div select").set(customFs.get("export"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);
		
		// Assign Accounts record to QAUser
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(sugar().users.qaUser.get("userName"));
		sugar().accounts.recordView.save();

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify no export action is shown while Access Type = Admin & Export = None
	 * @throws Exception
	 */
	@Test
	public void Roles_21266_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.openActionDropdown();
		
		// Verify that No "Export" action is shown in the action drop-down list in the list view.
		sugar().accounts.listView.getControl("exportButton").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}