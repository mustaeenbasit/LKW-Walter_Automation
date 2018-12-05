package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_30658 extends SugarTest {

	public void setup() throws Exception {
		// Creating test account record
		sugar().accounts.api.create();

		// Initializing role data
		FieldSet roleData = testData.get("env_role_setup").get(0);

		// Login as admin
		sugar().login();

		// Create a Role
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Setting Accounts >> Name and Assigned to field permissions to 'Owner Read/Owner write'
		// TODO: VOOD-580 - Create Roles (ACL) Module LIB
		new VoodooControl("div", "css", "#contentTable table tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "id", "namelink").click();
		new VoodooControl("select", "id", "flc_guidname").set(roleData.get("roleOwnerRead/OwnerWrite"));
		new VoodooControl("div", "id", "assigned_user_namelink").click();
		new VoodooControl("select", "id", "flc_guidassigned_user_name").set(roleData.get("roleOwnerRead/OwnerWrite"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role and logout
		AdminModule.assignUserToRole(roleData);

		// Logout as admin
		sugar().logout();
	}

	/**
	 * Verify that user should be able to save the record when required field permissions are set "Owner read/Owner write"
	 * @throws Exception
	 */
	@Test
	public void Roles_30658_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Navigating to Accounts module
		sugar().accounts.navToListView();

		// Clicking on 'Follow Link icon' in name field to open record view for the record
		// TODO: VOOD-498 - Need ListView functionality for all row actions
		new VoodooControl("a", "css", ".noaccess.fld_name a").click();

		// Updating the values for 'City' and 'Country' for the Account record
		FieldSet accountData = testData.get(testName).get(0);
		String city = accountData.get("city");
		String country = accountData.get("country");
		
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("billingAddressCity").set(city);
		sugar().accounts.recordView.getEditField("billingAddressCountry").set(country);
		sugar().accounts.recordView.save();

		// Verify the updated values in Accounts record view
		sugar().accounts.recordView.getDetailField("billingAddressCity").assertEquals(city, true);
		sugar().accounts.recordView.getDetailField("billingAddressCountry").assertEquals(country, true);

		// Verify the updated values in Accounts list view
		sugar().accounts.navToListView();
		sugar().accounts.listView.getDetailField(1, "billingAddressCity").assertEquals(city, true);
		sugar().accounts.listView.getDetailField(1, "billingAddressCountry").assertEquals(country, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}