package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21210 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		// Create an Account as Admin so that QAuser is Not the owner
		sugar().accounts.api.create();
		sugar().login();
		
		AdminModule.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and description have been Saved
	}

	/**
	 * Verify if Field permissions set to Read Owner Write on Accounts - Inline
	 * edit of not-owned records is not allowed
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21210_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-580 -Create a Roles (ACL) Module LIB
		// On the Access matrix, Click on Accounts module and set the field access
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".edit.view tr:nth-of-type(2) a").click();		
		new VoodooControl("div", "id", "email1link").click();
		new VoodooControl("select", "id", "flc_guidemail1").set("Read/Owner Write");
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address").set("Read/Owner Write");
		new VoodooControl("div", "id", "namelink").click();
		new VoodooControl("select", "id", "flc_guidname").set("Read/Owner Write");
		
		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.focusDefault();
		
		// Assign a non-admin user (qauser), to the Role
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Admin and log in as qauser to verify Owner inline edit access to Accounts
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		
		// Verify that no fields are editable as qauser is not the owner of the record
		sugar().accounts.listView.getEditField(1, "workPhone").assertVisible(true);
		sugar().accounts.listView.getEditField(1, "billingAddressCity").assertVisible(false);
		sugar().accounts.listView.getEditField(1, "billingAddressCountry").assertVisible(false);
		sugar().accounts.listView.getEditField(1, "name").assertVisible(false);
		sugar().accounts.listView.getEditField(1, "emailAddress").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}