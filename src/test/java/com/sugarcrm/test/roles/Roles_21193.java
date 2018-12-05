package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21193 extends SugarTest {
	FieldSet roleRecord;
	AccountRecord myAccount;
	VoodooControl editBillingAddressStreet;
	VoodooControl editBillingAddressCity;
	VoodooControl editBillingAddressState;
	VoodooControl editBillingAddressPostalCode;
	VoodooControl editBillingAddressCountry;

	public void setup() throws Exception {
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
		roleRecord = testData.get("Roles_21193").get(0);
		sugar().login();
		myAccount = (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * Verify field permissions in multiple roles for Read Write and Not Set
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21193_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Create the first Role which has Accounts Billing Address set to Not
		// Set
		// TODO - VOOD-688
		sugar().admin.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and
		// Description have been Saved
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css",
				".edit.view tr:nth-of-type(2) td:nth-of-type(1) a").click();
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address")
				.set("Not Set");
		// Save the role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		sugar().admin.assignUserToRole(roleRecord);

		// Create the second Role which has Accounts Billing Address set to Read
		// Write
		VoodooUtils.focusDefault();
		sugar().admin.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and
		// Description have been Saved
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css",
				".edit.view tr:nth-of-type(2) td:nth-of-type(1) a").click();
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address")
				.set("Read/Write");
		// Save the role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		// Assign the same suer, QAuser, to the second role
		VoodooUtils.focusDefault();
		sugar().admin.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin and log in as QAuser to verify the Billing
		// Address access is Read Write
		VoodooUtils.focusDefault();
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		myAccount.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		// Verify the Billing Address is Read\Write
		editBillingAddressStreet.assertVisible(true);
		editBillingAddressCity.assertVisible(true);
		editBillingAddressState.assertVisible(true);
		editBillingAddressPostalCode.assertVisible(true);
		editBillingAddressCountry.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
