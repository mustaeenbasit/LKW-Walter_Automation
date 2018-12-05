package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_30890 extends SugarTest {

	public void setup() throws Exception {
		FieldSet roleRecordData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
		
		// Navigate to system settings
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");

		// Enable the Preview Editing checkbox
		// TODO: VOOD-1903 Additional System Settings support
		new VoodooControl("input", "css", "input[name='preview_edit'].checkbox").click();

		// Save change settings
		sugar().admin.systemSettings.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
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
		
		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that pencil icon is not appearing for record`s Preview panel when user don't have permission to edit it
	 * @throws Exception
	 */
	@Test
	public void Roles_30890_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		
		// Preview the non-owned Account Record
		sugar().accounts.listView.previewRecord(1);

		// Verify pencil icon is not appearing in Preview panel near "Preview" label for non-owned records.
		// TODO: VOOD-2064 Need Lib support for Preview Pane's Edit View controls.
		new VoodooControl("i", "css", ".preview-headerbar .fa.fa-pencil").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
