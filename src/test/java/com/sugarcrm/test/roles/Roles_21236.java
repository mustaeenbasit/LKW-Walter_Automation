package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21236 extends SugarTest {
	DataSource rolesData;

	public void setup() throws Exception {
		rolesData = testData.get(testName);
		
		sugar().accounts.api.create();
		sugar().login();

		// Create a role
		AdminModule.createRole(rolesData.get(0));
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Owner Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		for(int i = 0; i < rolesData.size(); i++) {
			// Need to click again on fieldCtrl if permissionsCtrl is not visible as some time script fails to click.
			VoodooControl fieldCtrl = new VoodooControl("div", "id", rolesData.get(i).get("fields"));
			VoodooControl permissionsCtrl = new VoodooControl("select", "id", rolesData.get(i).get("permissions"));
			fieldCtrl.click();
			if(!permissionsCtrl.queryVisible())
				fieldCtrl.click();
			permissionsCtrl.set(rolesData.get(0).get("access"));
		}

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(rolesData.get(0));

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Field permissions set to Owner Read/ Owner Write on Accounts, non-owned records cannot be viewed
	 * @throws Exception
	 */
	@Test
	public void Roles_21236_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Navigate to Accounts Listview
		sugar().accounts.navToListView();
		
		// Verify that records not owned by Max should not be displayed
		// TODO: VOOD-1349
		new VoodooControl("span", "css", "tbody tr:nth-of-type(1) .fld_name").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", "tbody tr:nth-of-type(1) .fld_billing_address_city").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", "tbody tr:nth-of-type(1) .fld_billing_address_country").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", "tbody tr:nth-of-type(1) .fld_phone_office").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", "tbody tr:nth-of-type(1) .fld_assigned_user_name").assertAttribute("class", "noaccess", true);
		new VoodooControl("span", "css", "tbody tr:nth-of-type(1) .fld_email").assertAttribute("class", "noaccess", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}