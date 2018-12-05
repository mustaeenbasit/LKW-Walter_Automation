package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_31249 extends SugarTest {

	public void setup() throws Exception {
		// Creating Account Record for quotes
		sugar().accounts.api.create();

		// Login as admin
		sugar().login();
	}

	/**
	 * Verify that billing address and shipping address fields retain values on clicking save after clicking edit button.
	 * @throws Exception
	 */
	@Test
	public void Roles_31249_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		FieldSet roleData = testData.get(testName).get(0);
		
		// Create a role
		AdminModule.createRole(roleData);
		VoodooUtils.focusFrame("bwc-frame");

		// Clicking 'Quotes' from the Left Hand Pane
		// TODO: VOOD-856
		new VoodooControl("a", "css", ".edit.view tr:nth-child(20) a").click();
		VoodooUtils.waitForReady();

		// Setting Billing Address permission to "Read Only", and leave all other fields as "Not Set"
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address").set(roleData.get("billingAddressPermission"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) to this Role
		AdminModule.assignUserToRole(roleData);

		// Create a Quote record
		sugar().quotes.create();
		
		// Edit the above created quote record to populate the Shipping Address fields
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-930
		new VoodooControl("input", "css", "#shipping_checkbox").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		
		// Logout as admin
		sugar().logout();

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Navigate to the quote record created above
		sugar().quotes.navToListView();
		sugar().quotes.listView.clickRecord(1);

		// Edit the record (Close Date)
		sugar().quotes.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();

		// Verify that the Quote is saved 
		sugar().quotes.detailView.assertVisible(true);
		VoodooUtils.focusFrame("bwc-frame");

		String reqdBillingAddrCtrl = "tr:nth-child(7) td:nth-child(2) #billing_address_";
		String reqdShippingAddrCtrl = "tr:nth-child(7) td:nth-child(4) #shipping_address_";
		String street = sugar().accounts.getDefaultData().get("billingAddressStreet");
		String city = sugar().accounts.getDefaultData().get("billingAddressCity");
		String state = sugar().accounts.getDefaultData().get("billingAddressState");
		String postalCode = sugar().accounts.getDefaultData().get("billingAddressPostalCode");
		String country = sugar().accounts.getDefaultData().get("billingAddressCountry");
		
		// Verify that values in Billing Name fields retain original values
		new VoodooControl("input", "css", reqdBillingAddrCtrl + "street").assertAttribute("value", street, true);
		new VoodooControl("input", "css", reqdBillingAddrCtrl + "city").assertAttribute("value", city, true);
		new VoodooControl("input", "css", reqdBillingAddrCtrl + "state").assertAttribute("value", state, true);
		new VoodooControl("input", "css", reqdBillingAddrCtrl + "postalcode").assertAttribute("value", postalCode, true);
		new VoodooControl("input", "css", reqdBillingAddrCtrl + "country").assertAttribute("value", country, true);

		// Verify that values in Shipping Name fields retain original values
		new VoodooControl("input", "css", reqdShippingAddrCtrl + "street").assertAttribute("value", street, true);
		new VoodooControl("input", "css", reqdShippingAddrCtrl + "city").assertAttribute("value", city, true);
		new VoodooControl("input", "css", reqdShippingAddrCtrl + "state").assertAttribute("value", state, true);
		new VoodooControl("input", "css", reqdShippingAddrCtrl + "postalcode").assertAttribute("value", postalCode, true);
		new VoodooControl("input", "css", reqdShippingAddrCtrl + "country").assertAttribute("value", country, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}