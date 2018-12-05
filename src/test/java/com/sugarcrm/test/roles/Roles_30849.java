package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_30849 extends SugarTest {

	public void setup() throws Exception {
		FieldSet roleData = testData.get("env_role_setup").get(0);
		
		// Creating an Account and an Opportunities record
		sugar().accounts.api.create();
		
		// Login as admin
		sugar().login();
		
		// Create an Opportunity with Account record as the above
		// TODO: VOOD-444
		sugar().opportunities.create();

		// Create a role
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set its List role to Owner
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_list div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_list div select").set(roleData.get("roleOwner"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a regular (non-admin) user i.e. qauser to the Role
		AdminModule.assignUserToRole(roleData);

		// Logout as Admin 
		sugar().logout();
	}

	/**
	 * Verify user should be able to view the record view when list view is set to owner.
	 * @throws Exception
	 */
	@Test
	public void Roles_30849_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		
		// Navigate to Opportunity record created above
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		// Click Account name link in Opportunity Record View
		sugar().opportunities.recordView.getDetailField("relAccountName").click();
		VoodooUtils.waitForReady();
		
		// Verify that the user is navigated to the Record View of the Account Record
		sugar().accounts.recordView.getDetailField("name").assertEquals(sugar().accounts.getDefaultData().get("name"), true);
		
		// Navigate to Accounts List View
		sugar().accounts.navToListView();
		
		// Verify that No Account record is visible to qauser (as per the permissions in setup).
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}