package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21269 extends SugarTest {
	public void setup() throws Exception {
		
		// Initialize Test Data
		sugar().contacts.api.create();
		FieldSet testDataFS = testData.get(testName).get(0);
		FieldSet roleRecordFS = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a new role in Sugar and Select a module and set Access Type = List & Delete = Owner
		AdminModule.createRole(roleRecordFS);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, set Access Type = Admin & List = Owner
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_admin div select").set(testDataFS.get("admin"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_list div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_list div select").set(roleRecordFS.get("roleOwner"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select a user for this role
		AdminModule.assignUserToRole(roleRecordFS);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Create two contact records assigned to QAUser
		// TODO: VOOD-444
		FieldSet contactName = new FieldSet();
		for (int i = 0; i < 2; i++) {
			contactName.put("lastName", testName + "_" + i);
			sugar().contacts.create(contactName);
		}
	}

	/**
	 * Verify user can see own records in the list view while Access Type = Admin & List = Owner
	 * @throws Exception
	 */
	@Test
	public void Roles_21269_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify count of records on the list view 
		Assert.assertTrue("The number of record assigned to this user qauser are not matched", sugar().contacts.listView.countRows() == 2);
		
		// Verify that records assigned to this user are visible on the list view
		sugar().contacts.listView.getDetailField(1, "fullName").assertContains(testName + "_1", true);
		sugar().contacts.listView.getDetailField(2, "fullName").assertContains(testName + "_0", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}