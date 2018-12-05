package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21265 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		FieldSet testDataFS = testData.get(testName).get(0);
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a new role in Sugar and Select a module and set Access Type = Admin & Edit = None
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, set Access Type = Admin & Edit = None
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_admin div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_admin div select").set(testDataFS.get("admin"));
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_edit div select").set(roleRecord.get("roleNone"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select a user for this role
		AdminModule.assignUserToRole(roleRecord);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that edit and related actions aren't shown while Access Type = Admin & Edit = None
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21265_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the select module 
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();	

		// Verify that No 'Mass Update' and no 'Merge' actions are shown in the action drop down list in the list view. 
		sugar().contacts.listView.getControl("massUpdateButton").assertExists(false);
		// TODO VOOD-681 and VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list a").assertExists(false);

		// Close the action drop down and de-select all the record 
		sugar().contacts.listView.openActionDropdown();
		sugar().contacts.listView.toggleSelectAll();

		// Select a record and go to the detail view
		sugar().contacts.listView.clickRecord(1);

		// Check the drop down action menu in the detail view
		sugar().contacts.recordView.openPrimaryButtonDropdown();

		// Verify that No 'Edit' and no 'Find Duplicates' actions are shown in the detail view. 
		sugar().contacts.recordView.getControl("editButton").assertExists(false);
		// TODO: VOOD-738
		new VoodooControl("a", "css", ".fld_find_duplicates.detail a").assertExists(false);

		// Close the opened primary action drop down'
		sugar().contacts.recordView.openPrimaryButtonDropdown();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}