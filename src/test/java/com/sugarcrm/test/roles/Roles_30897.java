package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_30897 extends SugarTest {
	AccountRecord myAccount;
	UserRecord chrisUser;

	public void setup() throws Exception {
		// Creating an account later to be assigned to qauser
		myAccount = (AccountRecord)sugar().accounts.api.create();
		
		// Login as admin
		sugar().login();
		
		// Assigning the above created account to 'qauser'
		FieldSet editData = new FieldSet();
		editData.put("relAssignedTo", testData.get("env_role_setup").get(0).get("userName"));
		
		// Edit the account using the UI.
		myAccount.edit(editData);
		
		// Create an Opportunity with Account record as the above
		sugar().opportunities.create();
		
		// Create a new user 'Chris'
		// TODO: VOOD-1200
		chrisUser = (UserRecord) sugar().users.create();
	}

	/**
	 * Verify that Account name (of non-owned records) is shown while inline editing an opportunity when view is set to owner.
	 * @throws Exception
	 */
	@Test
	public void Roles_30897_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet roleData = testData.get(testName).get(0);

		// Create a role
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set its View role to Owner
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Accounts_view div select").set(roleData.get("view"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (Chris - created above) to the Role
		AdminModule.assignUserToRole(roleData);

		// Logout as Admin 
		sugar().logout();
		
		// Login as Chris
		sugar().login(chrisUser);
		
		// Navigate to Opportunities
		sugar().opportunities.navToListView();
		
		// Clicking on "Edit" to inline edit the opportunity. 
		sugar().opportunities.listView.editRecord(1);
		
		// Verify Account name is displayed to Chris user in Opportunity List View
		sugar().opportunities.listView.getEditField(1, "relAccountName").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}