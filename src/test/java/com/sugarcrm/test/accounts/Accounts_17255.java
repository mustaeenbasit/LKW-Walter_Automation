package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Accounts_17255 extends SugarTest {
	FieldSet roleData = new FieldSet();
	String accountModule = "";

	public void setup() throws Exception {
		roleData = testData.get(testName).get(0);
		accountModule = sugar().accounts.moduleNamePlural;
		// Log-In as an Admin
		sugar().login();

		// Create an account record with email address.
		sugar().navbar.navToModule(accountModule);
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("emailAddress").set(roleData.get("emailAddress"));
		sugar().accounts.createDrawer.save();

		// Create a role, set a field of accounts module's access to None, such as email address field
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-856 - Lib is needed for Roles Management
		new VoodooControl("a", "css", ".edit tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		//Click the Accounts link on the Roles page
		new VoodooControl("td", "css", "#ACLEditView table:nth-of-type(2) tr:nth-child(3) td:nth-child(2)").click();

		// Set field permission of email field to "None"
		new VoodooControl("select", "id", "flc_guidemail").set(roleData.get("roleNone"));

		// Save the role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select regular user(qaUser) for the role 
		AdminModule.assignUserToRole(roleData);

		// Logout from application
		sugar().logout();

	}

	/**
	 * Check field's None ACL control in listview inline edit
	 * @throws Exception
	 */
	@Test
	public void Accounts_17255_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log-In as qaUser
		sugar().login(sugar().users.qaUser);

		// Navigate to the Accounts list view
		sugar().navbar.navToModule(accountModule);

		// Click inline edit action of a record
		sugar().accounts.listView.editRecord(1);

		// Assert that  the field is not editable and diplays "No access"
		// TODO: VOOD-1445 - Need lib support for enhanced disabled check in parent controls of a child
		new VoodooControl("span", "css", ".fld_email.noaccess span").assertEquals(roleData.get("noAccess"), true);
		sugar().accounts.listView.getEditField(1, "emailAddress").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}