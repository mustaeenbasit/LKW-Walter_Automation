package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21243 extends SugarTest {
	FieldSet roleRecordData;
	DataSource accountDS;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		accountDS = testData.get(testName+"_data");
		sugar().accounts.api.create(accountDS);
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
		
		// Assign four Account records to QAUser
		sugar().accounts.navToListView();
		FieldSet massUpdateField = new FieldSet();
	    massUpdateField.put("Assigned to", sugar().users.qaUser.get("userName"));
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(2);
		sugar().accounts.listView.checkRecord(3);
		sugar().accounts.listView.checkRecord(4);
		sugar().accounts.massUpdate.performMassUpdate(massUpdateField);
		sugar().alerts.waitForLoadingExpiration();
		
		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify user can delete any records while the delete role is set to be All/Not Set
	 * @throws Exception
	 */
	@Test
	public void Roles_21243_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(5);
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		sugar().accounts.listView.confirmDelete();
		VoodooUtils.waitForReady();

		// Verify that the selected records should be deleted and removed from the list view. 
		sugar().accounts.listView.getDetailField(5, "name").assertContains(accountDS.get(3).get("name"), false);

		// Go to Accounts recordView
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.delete();
		sugar().alerts.confirmAllAlerts();
		VoodooUtils.waitForReady();
		
		// Verify that the selected record assigned to this user can be deleted form detail view page.
		sugar().accounts.listView.getDetailField(1, "name").assertContains(accountDS.get(5).get("name"), false);

		// Go to Accounts recordView
		sugar().accounts.listView.clickRecord(3);
		sugar().accounts.recordView.delete();
		sugar().alerts.confirmAllAlerts();
		VoodooUtils.waitForReady();
		
		// Verify that the selected record not assigned to this user can be deleted from the detail view.
		sugar().accounts.listView.getDetailField(1, "name").assertContains(accountDS.get(1).get("name"), false);
		
		// Logout as QAUser & Login as Admin
		sugar().logout();
		sugar().login();
		
		// TODO: VOOD-856
		// Go to Role Management and change delete access All to Not Set
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "roles_management").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "name_basic").set(roleRecordData.get("roleName"));
		new VoodooControl("a", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "#MassUpdate table tbody tr.oddListRowS1 td:nth-child(3) b a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Select "Account" module and set its Delete role to be Owner
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_delete div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_delete div select").set("Not Set"); // Set default

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Logout as Admin & Login as QAUser
		sugar().logout();
		sugar().login();
		
		// Go to Accounts record listView
		sugar().accounts.navToListView();
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.checkRecord(3);
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		sugar().accounts.listView.confirmDelete();
		VoodooUtils.waitForReady(30000);

		// Verify the selected records should be deleted and removed from the list view.
		sugar().accounts.listView.getDetailField(1, "name").assertContains(accountDS.get(6).get("name"), false);

		// Go to Accounts recordView
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.delete();
		sugar().alerts.confirmAllAlerts();

		// Verify that the selected record assigned to this user can be deleted form detail view page.
		sugar().accounts.listView.getDetailField(1, "name").assertContains(accountDS.get(7).get("name"), false);

		// Go to Accounts recordView
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.delete();
		sugar().alerts.confirmAllAlerts();
		VoodooUtils.waitForReady();
		
		// Verify listView have not records.
		sugar().accounts.listView.assertIsEmpty();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}