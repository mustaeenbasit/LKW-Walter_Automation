package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_26818 extends SugarTest {
	DataSource AddressData;

	public void setup() throws Exception {
		AddressData = testData.get(testName);
		sugar().login();
		sugar().accounts.navToListView();

		// TODO:1418
		VoodooControl copyAddressbtnCtrl = new VoodooControl("span", "css","span[data-voodoo-name='copy'] input");
		VoodooControl Accountsship_City = new VoodooControl("input", "css", ".fld_shipping_address_city input");
		VoodooControl Accountsship_State = new VoodooControl("input", "css", ".fld_shipping_address_state input");
		VoodooControl Accountsship_Country = new VoodooControl("input", "css", ".fld_shipping_address_country input");

		// Creating two Accounts each with different Billing & Shipping Address
		for(int i = 0; i < AddressData.size()-1 ; i++) {
			sugar().accounts.listView.getControl("createButton").click();
			sugar().accounts.createDrawer.getEditField("name").set(testName + "_" + i);
			sugar().accounts.createDrawer.showMore();
			copyAddressbtnCtrl.click();
			sugar().accounts.createDrawer.getEditField("billingAddressCity").set(AddressData.get(i).get("billing_City"));
			sugar().accounts.createDrawer.getEditField("billingAddressState").set(AddressData.get(i).get("billing_State"));
			sugar().accounts.createDrawer.getEditField("billingAddressCountry").set(AddressData.get(i).get("billing_Country"));
			Accountsship_City.set(AddressData.get(i).get("shipping_City"));
			Accountsship_State.set(AddressData.get(i).get("shipping_State"));
			Accountsship_Country.set(AddressData.get(i).get("shipping_Country"));
			sugar().accounts.createDrawer.save();
		}

		// Creating Contact and relating it with Account having different billing and shipping Address
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.showMore();
		copyAddressbtnCtrl.click();
		sugar().contacts.createDrawer.getEditField("primaryAddressCity").set(AddressData.get(2).get("billing_City"));
		sugar().contacts.createDrawer.getEditField("primaryAddressState").set(AddressData.get(2).get("billing_State"));
		sugar().contacts.createDrawer.getEditField("primaryAddressCountry").set(AddressData.get(2).get("billing_Country"));

		// TODO:VOOD-1418
		VoodooControl contacts_shipCity = new VoodooControl("input", "css", "[name= 'alt_address_city']");
		VoodooControl contacts_shipState = new VoodooControl("input", "css", "[name= 'alt_address_state']");
		VoodooControl contacts_shipCountry = new VoodooControl("input", "css","[name= 'alt_address_country']");
		contacts_shipCity.set(AddressData.get(2).get("shipping_City"));
		contacts_shipState.set(AddressData.get(2).get("shipping_State"));
		contacts_shipCountry.set(AddressData.get(2).get("shipping_Country"));
		sugar().contacts.createDrawer.getEditField("relAccountName").set(testName + "_0");
		sugar().alerts.cancelAllWarning();
		sugar().contacts.createDrawer.save();
	}

	/**
	 * Verify that correct account is populated when create quote via quote subpanel in Contacts module
	 * @throws Exception
	 */
	@Test
	public void Contacts_26818_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(testName +"_1");
		sugar().alerts.cancelAllWarning();

		// Clicking Create icon through Quotes Subpanel
		// TODO:VOOD-1000
		VoodooControl quotes=  new VoodooControl("span", "css", ".layout_Quotes span[data-voodoo-name='create_button'] a");
		quotes.click();
		sugar().alerts.confirmAllWarning();
		VoodooUtils.focusFrame("bwc-frame");

		// Asserting that Quotes Accounts fields Contains the Account1 values.
		// TODO:VOOD-930
		VoodooControl rel_AccountBillingAddress = new VoodooControl("input", "id", "billing_account_name");
		rel_AccountBillingAddress.assertContains(testName +"_0", true);
		VoodooControl rel_AccountShippingAddress = new VoodooControl("input", "id", "shipping_account_name");
		rel_AccountShippingAddress.assertContains(testName +"_0", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
