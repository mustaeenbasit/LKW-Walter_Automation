package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_30709 extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Campaigns Modules should not accessible via Manage Subscriptions when Campaign module is disabled via Role.
	 * @throws Exception
	 */
	@Test
	public void Roles_30709_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a role
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, Set the Campaigns Access cell to Disabled
		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		new VoodooControl("div", "css", "td#ACLEditView_Access_Campaigns_access div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Campaigns_access div select").set(roleRecord.get("campaignAccess"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as qauser 
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Navigate to contacts record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Verifying action dropdown doesn't contains Manage Subscription
		// TODO: VOOD-738
		sugar().contacts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("ul", "css", ".fld_main_dropdown .dropdown-menu").assertContains(roleRecord.get("manageSubscription"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}