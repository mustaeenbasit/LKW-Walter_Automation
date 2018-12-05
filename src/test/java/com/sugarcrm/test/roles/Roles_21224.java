package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21224 extends SugarTest {
	FieldSet roleRecord;
	AccountRecord myAccount;
	VoodooControl editRelAssignedTo;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21224").get(0);
		editRelAssignedTo = sugar().accounts.recordView
				.getEditField("relAssignedTo");
		sugar().login();
		AdminModule.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and
		// Description have been Saved
	}

	/**
	 * Module permissions set "Edit" to Owner on Accounts - User can then change
	 * owner and Not edit after
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21224_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set the Accounts Edit cell to Owner
		VoodooUtils.focusFrame("bwc-frame");
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
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as QAuser to Create an Acccount
		// and verify change ownership
		// TODO VOOD-444 Support creating relationships via API
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		AccountRecord myAccount = (AccountRecord) sugar().accounts.create();
		myAccount.navToRecord();
		// Verify the user can edit the record
		sugar().accounts.recordView.edit();
		editRelAssignedTo.set("Administrator");
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		myAccount.navToRecord();
		// Verify the current user cannot edit the Account as it is now owned by
		// someone else
		sugar().accounts.recordView.getControl("editButton").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
