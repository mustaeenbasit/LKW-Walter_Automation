package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21264 extends SugarTest {
	FieldSet testDataFS = new FieldSet();

	public void setup() throws Exception {
		sugar().contacts.api.create();
		testDataFS = testData.get(testName).get(0);
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a new role in Sugar and Select a module and set Access Type = Admin & Delete = Owner
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, set Access Type = Admin & Delete = Owner
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_admin div select").set(testDataFS.get("admin"));
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_delete div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_delete div select").set(roleRecord.get("roleOwner"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select a user for this role
		AdminModule.assignUserToRole(roleRecord);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Create two contact records assigned to QAUser
		// TODO: VOOD-444
		FieldSet contactName = new FieldSet();
		for(int i = 0; i < 2; i++) {
			contactName.put("lastName", testName + "_" + i);
			sugar().contacts.create(contactName);
		}
	}

	/**
	 * Verify user cannot delete non-owned record while Access Type = Admin & Delete = Owner
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21264_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// For Owned Record
		// Select a record assigned to this user and click delete button in the list view 
		sugar().contacts.listView.checkRecord(1);
		sugar().contacts.listView.openActionDropdown();
		sugar().contacts.listView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that the record assigned to this user should be deleted and removed from the list view
		VoodooControl contactsRecordListViewCtrl = sugar().contacts.listView.getDetailField(1, "fullName");
		contactsRecordListViewCtrl.assertContains(testName + "_1", false);
		contactsRecordListViewCtrl.assertContains(testName + "_0", true);
		Assert.assertTrue("The record assigned to this user is not deleted", sugar().contacts.listView.countRows() == 2);

		// Select a record NOT assigned to this user and click delete button in the list view 
		sugar().contacts.listView.checkRecord(2);
		sugar().contacts.listView.openActionDropdown();
		sugar().contacts.listView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Assert the warning message 
		sugar().alerts.getWarning().assertContains(testDataFS.get("warningMessage"), true);
		sugar().alerts.getWarning().closeAlert();

		// Verify that the record not assigned to this user should NOT be removed and deleted from the list view
		contactsRecordListViewCtrl.assertContains(testName + "_0", true);
		sugar().contacts.listView.getDetailField(2, "fullName").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
		Assert.assertTrue("The record assigned to this user is deleted", sugar().contacts.listView.countRows() == 2);

		// Select a record assigned to this user and go to the detail view 
		sugar().contacts.listView.clickRecord(1);

		// Click delete button from action menu
		sugar().contacts.recordView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that the record should be deleted in the detail view
		contactsRecordListViewCtrl.assertContains(testName + "_0", false);
		contactsRecordListViewCtrl.assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
		Assert.assertTrue("The record assigned to this user is not deleted", sugar().contacts.listView.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}