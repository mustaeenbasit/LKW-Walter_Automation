package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21248 extends SugarTest {
	FieldSet roleRecordData;
	DataSource accountDS;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName+"_role").get(0);
		sugar().accounts.api.create();
		sugar().accounts.api.create();
		sugar().login();

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set Export role to be All
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_export div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_export div select").set(roleRecordData.get("export"));

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
	 * Verify user can export any records while the module export role is set to All/Not Set
	 * @throws Exception
	 */
	@Test
	public void Roles_21248_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		
		// Go to Accounts record listView
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		sugar().accounts.listView.openActionDropdown();
		VoodooUtils.waitForReady();
		
		// Verify that the export action should be shown in the list view action list.
		VoodooControl exportLinkCtrl = sugar().accounts.listView.getControl("exportButton");
		exportLinkCtrl.assertExists(true);

		// Logout as QAUser and Login as Admin
		sugar().logout();
		sugar().login();

		// TODO: VOOD-856
		// Go to Role Management and change the Import role to be "Not Set"
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "roles_management").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "name_basic").set(roleRecordData.get("roleName"));
		new VoodooControl("a", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "#MassUpdate table tbody tr.oddListRowS1 td:nth-child(3) b a").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_export div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_export div select").set(customFS.get("setDefaultRole")); // Set default

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout as Admin & Login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		sugar().accounts.listView.openActionDropdown();
		
		// Verify that the export action should be shown after the changes.
		exportLinkCtrl.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}