package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21303 extends SugarTest {
	AccountRecord myAccountQA, myAccountAdmin;
	FieldSet roleRecord, accountsQA;
	VoodooControl noAccessBillingAddress;
	VoodooControl editBillingAddressStreet;
	VoodooControl editBillingAddressCity;
	VoodooControl editBillingAddressState;
	VoodooControl editBillingAddressPostalCode;
	VoodooControl editBillingAddressCountry;
	VoodooControl editWorkPhone;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21303").get(0);
		noAccessBillingAddress = new VoodooControl("span", "css",
				".address.noaccess span");
		editBillingAddressStreet = sugar().accounts.recordView
				.getEditField("billingAddressStreet");
		editBillingAddressCity = sugar().accounts.recordView
				.getEditField("billingAddressCity");
		editBillingAddressState = sugar().accounts.recordView
				.getEditField("billingAddressState");
		editBillingAddressPostalCode = sugar().accounts.recordView
				.getEditField("billingAddressPostalCode");
		editBillingAddressCountry = sugar().accounts.recordView
				.getEditField("billingAddressCountry");
		editWorkPhone = sugar().accounts.recordView.getEditField("workPhone");
		sugar().login();
		// Create an Account owned by qauser
		accountsQA = testData.get("Roles_21303_Accounts_GUIQA").get(0);
		myAccountQA = (AccountRecord) sugar().accounts.create(accountsQA);
		// Create an Account not owned by qauser
		myAccountAdmin = (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * User in multiple roles, ACL set at field level, Role 1 - Owner Read/Owner
	 * Write and Role 2 - Read/Write, Verify the most restrictive role (i.e.
	 * Owner) applies
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21303_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create the first role - Accounts:Fields Owner Read/Owner Write
		// TODO - VOOD-688
		sugar().admin.createRole(roleRecord);
		// Now on the Access matrix - Click the Accounts module and set the
		// field access for the Billing Address fields
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css",
				".edit.view tr:nth-of-type(2) td:nth-of-type(1) a").click();
		new VoodooControl("input", "css", "div#category_data .button").click();
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address")
				.set("Owner Read/Owner Write");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		sugar().admin.assignUserToRole(roleRecord);

		// Create the second role - Accounts:Fields Read\Write
		VoodooUtils.focusDefault();
		sugar().admin.createRole(roleRecord);
		// Now on the Access matrix - Click the Accounts module and set the
		// field access for the billing Address fields
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css",
				".edit.view tr:nth-of-type(2) td:nth-of-type(1) a").click();
		new VoodooControl("input", "css", "div#category_data .button").click();
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address")
				.set("Read/Write");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		sugar().admin.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as QAuser to verify Account
		// Read\Write
		// access for Address fields is None for Non owned Accounts
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		myAccountAdmin.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		// Verify there are only noAccess spans present for Billing Address
		noAccessBillingAddress.assertContains("No access", true);
		// Verify the Office Phone is editable to validate the test
		editWorkPhone.assertVisible(true);

		// Verify the User can Read\Write Billing Address fields for an Owned
		// Account
		myAccountQA.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		// Verify Billing Address is editable
		editBillingAddressCity.assertVisible(true);
		editBillingAddressCity.assertVisible(true);
		editBillingAddressState.assertVisible(true);
		editBillingAddressPostalCode.assertVisible(true);
		editBillingAddressCountry.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
