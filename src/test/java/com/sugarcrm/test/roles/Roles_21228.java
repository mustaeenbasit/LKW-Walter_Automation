package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21228 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		FieldSet roleRecordData = testData.get("env_role_setup").get(0);

		// Create several(five) new records and assign to user
		customDS = testData.get(testName);
		sugar().contacts.api.create(customDS);
		sugar().login();

		// Assigned the Contact record to QAUser(Owned record for QAUser)
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		for(int i = 0; i < customDS.size()-1; i++) { // Assign only 4 records to QAuser
			sugar().contacts.recordView.edit();
			sugar().contacts.recordView.showMore();
			sugar().contacts.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
			sugar().contacts.recordView.save();
			sugar().contacts.recordView.gotoNextRecord();
		}

		// Create a role myRole
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// For Contacts module, Select a module(contact) and set its List role to be Owner
		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_list div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_list div select").set(roleRecordData.get("roleOwner"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Verify user should see the own records in the list view while the List role is set to Owner
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21228_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Go o Contacts listView
		sugar().contacts.navToListView();
		FieldSet customFS = testData.get(testName).get(0);
		
		// Only the records(four) assigned to this user are shown in the list view.
		for(int i = 0; i < customDS.size()-1; i ++) {
			String fullNameVerify = customFS.get("title") + customDS.get((i + 1)).get("firstName") + " " + customDS.get((i + 1)).get("lastName");
			sugar().contacts.listView.getDetailField((i + 1), "fullName").assertContains(fullNameVerify, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}