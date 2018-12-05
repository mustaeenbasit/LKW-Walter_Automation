package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21255 extends SugarTest {
	VoodooControl accessTypeCtrl, accessTypeOptionsCtrl, roleSaveBtnCtrl;
	FieldSet roleRecord = new FieldSet();
	FieldSet accessTypeData = new FieldSet();

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		accessTypeData = testData.get(testName).get(0);
		sugar().login();

		// Create a new role in Sugar and select a module -> Set Access Type to be Normal
		// Create a role
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, Set the Contacts module Access Type to be Normal
		// TODO: VOOD-580
		accessTypeCtrl = new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_admin div:nth-of-type(2)");
		accessTypeOptionsCtrl = new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_admin div select");
		roleSaveBtnCtrl = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		accessTypeCtrl.click();
		accessTypeOptionsCtrl.set(accessTypeData.get("normal"));
		roleSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Select a user for this role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify user doesn't have the admin access while the access type is set to be Normal or Not Set
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21255_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Check the drop down menu from user login name
		sugar().navbar.toggleUserActionsMenu();

		// Verify that the Admin access should not be shown under the drop down menu. No Admin access is found
		VoodooControl adminPanelLinkCtrl = sugar().navbar.userAction.getControl("admin");
		adminPanelLinkCtrl.assertExists(false);

		// Toggle user action menu to close the drop down
		sugar().navbar.toggleUserActionsMenu();

		// Logout from QAUser and login as Admin user
		sugar().logout();
		sugar().login();

		// Go to the setup and change the Access Type to be Not Set
		sugar().admin.navToAdminPanelLink("rolesManagement");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "id", "name_basic").set(roleRecord.get("roleName"));
		new VoodooControl("a", "id", "search_form_submit").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "table.list.view tbody tr:nth-of-type(3) td:nth-of-type(3) a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		accessTypeCtrl.click();
		accessTypeOptionsCtrl.set(accessTypeData.get("notSet"));
		roleSaveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Check the drop down menu from user login name
		sugar().navbar.toggleUserActionsMenu();

		// Verify that the Admin access should not be shown under the drop down menu. No Admin access is found
		adminPanelLinkCtrl.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}