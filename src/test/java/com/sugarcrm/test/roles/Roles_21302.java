package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21302 extends SugarTest {
	AccountRecord myAccountQA, myAccountAdmin;
	FieldSet roleRecord, accountsQA;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21302").get(0);
		sugar().login();
		// Create an Account owned by qauser
		accountsQA = testData.get("Roles_21302_Accounts_GUIQA").get(0);
		myAccountQA = (AccountRecord) sugar().accounts.create(accountsQA);
		// Create an Account not owned by qauser
		myAccountAdmin = (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * Verify User in multiple roles, ACL set at Accounts module level, Role
	 * 1-Delete/Edit to Owner - Role 2-Edit to None
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21302_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create the first role - Accounts: Delete and Edit to Owner
		// TODO - VOOD-688
		sugar().admin.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_delete div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_delete div select")
				.set("Owner");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_edit div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_edit div select").set("Owner");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		sugar().admin.assignUserToRole(roleRecord);

		// Create the second role - Accounts: Edit to None
		VoodooUtils.focusDefault();
		sugar().admin.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",
				"td#ACLEditView_Access_Accounts_edit div:nth-of-type(2)")
				.click();
		new VoodooControl("select", "css",
				"td#ACLEditView_Access_Accounts_edit div select").set("None");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		sugar().admin.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as QAuser to verify Account Edit
		// access is None for Non owned Accounts
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		myAccountAdmin.navToRecord();
		// Check the Actions for a non owned record, there should be no Edit or
		// Delete
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		sugar().accounts.recordView.getControl("editButton").assertVisible(false);
		sugar().accounts.recordView.getControl("deleteButton").assertVisible(
				false);

		// Verify the User can Delete but not Edit an Owned Account
		myAccountQA.navToRecord();
		// Check the Actions for an owned record, there should be no Edit but
		// Delete should exist
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		sugar().accounts.recordView.getControl("editButton").assertVisible(false);
		sugar().accounts.recordView.getControl("deleteButton")
				.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
