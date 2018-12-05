package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21261 extends SugarTest {
	FieldSet roleRecordData;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		
		// Create two accounts record with different name
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create();
		sugar().accounts.api.create(fs);
		sugar().login();

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set its Edit role to be Owner
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_edit div select").set(roleRecordData.get("edit"));

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
	 * Verify user has the edit access to own records in the list view and detail view while the Edit role is set to Owner
	 * @throws Exception
	 */
	@Test
	public void Roles_21261_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		
		// Assigned record to this user
		sugar().accounts.listView.openRowActionDropdown(1);
		
		// Verify that "Edit" link is available in the "Actions" dropdown.
		sugar().accounts.listView.getControl(String.format("edit%02d", 1)).assertExists(true);
		sugar().accounts.listView.openRowActionDropdown(1); // Close opened row action drop-down
		
		// Not assigned record to this user
		sugar().accounts.listView.openRowActionDropdown(2);
		
		// Verify that "Edit" link is not available in the "Actions" dropdown.
		sugar().accounts.listView.getControl(String.format("edit%02d", 2)).assertExists(false);
		
		// Go to assigned record detail view
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-738 -Need to add a lib support for the actions in the record view 
		// Verify that the edit and related actions should be shown in the drop down menu:  Edit, Find Duplicate
		VoodooControl editBtnCtrl = sugar().accounts.recordView.getControl("editButton");
		editBtnCtrl.assertExists(true);
		VoodooControl findDuplicatCtrl = new VoodooControl("a","css","ul.dropdown-menu span[data-voodoo-name='find_duplicates_button'] a");
		findDuplicatCtrl.assertExists(true);
		
		// Go to none assigned record detail view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(2);
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		
		// Verify that the edit and related actions should not be shown in the drop down menu:  Edit, Find Duplicate
		editBtnCtrl.assertExists(false);
		findDuplicatCtrl.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}