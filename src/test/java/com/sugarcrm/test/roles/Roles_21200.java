package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21200 extends SugarTest {
	FieldSet roleRecord;
	AccountRecord myAccount;
	// The text fields have different classes if editable, these are used in
	// verification
	VoodooControl editBillingAddressStreet;
	VoodooControl editBillingAddressCity;
	VoodooControl editBillingAddressState;
	VoodooControl editBillingAddressPostalCode;
	VoodooControl editBillingAddressCountry;
	VoodooControl editWorkPhone;

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
		editWorkPhone = sugar().accounts.recordView.getEditField("workPhone");
		roleRecord = testData.get("Roles_21200").get(0);
		sugar().login();
		myAccount = (AccountRecord) sugar().accounts.api.create();
		AdminModule.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and
		// Description have been Saved
	}

	/**
	 * Verify ACL Set read only of address fields
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21200_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Now on the Access matrix - Click the Accounts module and set the
		// field access
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".edit.view tr:nth-of-type(2) td a")
				.click();
		new VoodooControl("div", "id", "shipping_addresslink").click();
		new VoodooControl("select", "id", "flc_guidshipping_address")
				.set("Read Only");
		// Without this Save after changing the Shipping Address field access
		// the next ACL field could not be found, I tried pauses and set focus
		// but only Save works here
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address")
				.set("Read Only");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		// Log out of Sugar as Admin and log in as QAuser to verify Address
		// restrictions
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		myAccount.navToRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		// Verify there are no editable address fields
		editBillingAddressStreet.assertVisible(false);
		editBillingAddressCity.assertVisible(false);
		editBillingAddressState.assertVisible(false);
		editBillingAddressPostalCode.assertVisible(false);
		editBillingAddressCountry.assertVisible(false);
		// Verify the Shipping Address cannot be edited by verifying the Copy
		// edit is disabled. The check box is an edit field that is disabled, so
		// the attribute needs to be verified
		new VoodooControl("input", "css", ".fld_copy.edit input")
				.assertAttribute("disabled", "true");
		// Verify the Office Phone is visible to validate the test false
		// assertions above
		editWorkPhone.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
