package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_30919 extends SugarTest {

	public void setup() throws Exception {
		FieldSet roleRecord = testData.get("env_role_setup").get(0);
		
		// Login as admin
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

		// Admin -> Role management -> Creating Role
		AdminModule.createRole(roleRecord);

		// For this role, set Account Module's Edit access to Owner 
		// TODO: VOOD-856 VOOD-580 -Create a Roles (ACL) Module LIB,
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",	"#ACLEditView_Access_Accounts_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_edit div select").set(roleRecord.get("roleOwner"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();

		// Logout
		sugar().logout();
	}

	/**
	 *  Verify that a valid message is displayed while change record`s Owner via List View "In-Line Edit" and save record from "Preview Edit"
	 * @throws Exception
	 */
	@Test
	public void Roles_30919_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Creating an account record
		sugar().accounts.create();

		// Click on Preview icon of the created record in List view
		sugar().accounts.listView.previewRecord(1);

		// TODO: VOOD-2064
		// Click on pencil icon of record in RHS preview pane
		new VoodooControl("i", "css", ".fa.fa-pencil").click();
		VoodooUtils.waitForReady();

		// In Listview inline editing same record and change assigned to field from qauser to Admin.
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1, "relAssignedTo").set(customData.get("assignedUserName"));
		sugar().accounts.listView.saveRecord(1);

		// Verifying Administrator is showing in list view of the record instead of qauser
		sugar().accounts.listView.getDetailField(1, "relAssignedTo").assertEquals(customData.get("assignedUserName"), true);

		// Click on save button in RHS Preview Pane
		new VoodooControl("a", "css", ".preview-header.fld_save_button").click();

		// Verifying Valid Error message is displaying
		sugar().alerts.getError().assertEquals(customData.get("validErrorMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}