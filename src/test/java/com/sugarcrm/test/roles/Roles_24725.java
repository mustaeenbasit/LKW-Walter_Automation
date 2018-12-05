package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_24725 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 *  User-Role_Verify that user can access the modules whose role is set to enable access corresponding modules.
	 * @throws Exception
	 */
	@Test
	public void Roles_24725_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Admin -> Role management -> Select a module (Accounts) -> Set Access Role to be Enabled
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580 
		// Create a Roles (ACL) Module LIB -> Set Access Role to be Enabled
		new VoodooControl("div", "css", "td#ACLEditView_Access_Accounts_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_access [label='Enabled']").click();
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Navigate to account module
		sugar().accounts.navToListView();

		// Verify that it should show the full actions from the navigation bar
		sugar().accounts.listView.verifyModuleTitle(sugar().accounts.moduleNamePlural);

		// Select a record and check drop down action menu in the list view
		sugar().accounts.listView.getControl("checkbox01").click();
		sugar().accounts.listView.openRowActionDropdown(1);

		// Verify that full action lists from the drop down menu displayed
		sugar().accounts.listView.getControl("edit01").assertVisible(true);
		sugar().accounts.listView.getControl("unfollow01").assertVisible(true);
		sugar().accounts.listView.getControl("delete01").assertVisible(true);
		sugar().accounts.listView.openRowActionDropdown(1); // Closing action dropdown

		// Open action dropdown in list view
		sugar().accounts.listView.openActionDropdown();

		// TODO: VOOD-689
		// Verify that full action lists from the drop down menu displayed on the listview page
		new VoodooControl("a", "name", "merge_button").assertVisible(true);
		sugar().accounts.listView.getControl("massUpdateButton").assertVisible(true);
		sugar().accounts.listView.getControl("deleteButton").assertVisible(true);
		sugar().accounts.listView.getControl("exportButton").assertVisible(true);
		sugar().accounts.listView.getControl("actionDropdown").click(); // Closing action dropdown

		// Click a record and check the drop down action menu in the detail view
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-695
		// Verify the action list in the record view
		sugar().accounts.recordView.getControl("editButton").assertVisible(true);
		sugar().accounts.recordView.getControl("deleteButton").assertVisible(true);
		sugar().accounts.recordView.getControl("copyButton").assertVisible(true);
		new VoodooControl("a", "css", ".fld_share.detail a").assertVisible(true);
		new VoodooControl("a", "css", ".fld_find_duplicates_button").assertVisible(true);
		new VoodooControl("a", "css", ".fld_historical_summary_button").assertVisible(true);;
		new VoodooControl("a", "css", ".fld_audit_button").assertVisible(true);;
		sugar().accounts.recordView.openPrimaryButtonDropdown(); // Close primary button

		// Click Edit button to the edit view page
		sugar().accounts.recordView.edit();

		// Verify that edit view page displayed
		sugar().accounts.recordView.getControl("cancelButton").assertVisible(true);;
		sugar().accounts.recordView.getControl("saveButton").assertVisible(true);;
		sugar().accounts.createDrawer.getEditField("name").assertVisible(true);;

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}