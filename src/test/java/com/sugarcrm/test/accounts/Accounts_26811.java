package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_26811 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify multi-line is available for street address field
	 * @throws Exception
	 */
	@Test
	public void Accounts_26811_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// Go to accounts module
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.showMore();
		
		String multiLineStreetAdd = String.format(ds.get(0).get("billingAddressStreet"), "\n", "\n");
		sugar().accounts.createDrawer.getEditField("billingAddressStreet").set(multiLineStreetAdd);
		sugar().accounts.createDrawer.getEditField("billingAddressCity").set(ds.get(0).get("billingAddressCity"));
		sugar().accounts.createDrawer.getEditField("billingAddressState").set(ds.get(0).get("billingAddressState"));
		sugar().accounts.createDrawer.getEditField("billingAddressPostalCode").set(ds.get(0).get("billingAddressState"));
		sugar().accounts.createDrawer.getEditField("billingAddressCountry").set(ds.get(0).get("billingAddressCountry"));
		
		// TODO: VOOD-555 -need lib support for all account shipping address fields
		// Verify that "Copy billing address to shipping address" checkbox is checked
		new VoodooControl("input", "css", ".edit.fld_copy input[type='checkbox']").assertAttribute("checked", "", true);

		// Click Save
		sugar().accounts.createDrawer.save();

		// Open the record just created
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Click "Edit" under record view
		sugar().accounts.recordView.edit();
		
		// TODO: VOOD-555 -need lib support for all account shipping address fields
		// Verify billing/shipping street address before update
		VoodooControl billingStreet = sugar().accounts.recordView.getEditField("billingAddressStreet");
		billingStreet.assertContains(multiLineStreetAdd, true);
		new VoodooControl("input", "css", "[name='shipping_address_street']").assertContains(multiLineStreetAdd, true);
		
		// Update billing street Address
		VoodooControl billingCity = sugar().accounts.recordView.getEditField("billingAddressCity");
		VoodooControl billingState = sugar().accounts.recordView.getEditField("billingAddressState");
		VoodooControl billingPostalCode = sugar().accounts.recordView.getEditField("billingAddressPostalCode");
		VoodooControl billingCountry = sugar().accounts.recordView.getEditField("billingAddressCountry");
		
		String multiLineStreetAdd2 = String.format(ds.get(1).get("billingAddressStreet"), "\n", "\n");
		billingStreet.set(multiLineStreetAdd2);
		billingCity.set(ds.get(1).get("billingAddressCity"));
		billingState.set(ds.get(1).get("billingAddressState"));
		billingPostalCode.set(ds.get(1).get("billingAddressPostalCode"));
		billingCountry.set(ds.get(1).get("billingAddressCountry"));
		sugar().accounts.recordView.save();

		// TODO: VOOD-555 -need lib support for all account shipping address fields
		// Verify that billing/shipping street address updated correctly
		sugar().accounts.recordView.edit();
		billingStreet.assertContains(multiLineStreetAdd2, true);
		new VoodooControl("input", "css", "[name='shipping_address_street']").assertContains(multiLineStreetAdd2, true);
		
		// Also verify city, state, postalCode & country for billing/shipping
		billingCity.assertContains(ds.get(1).get("billingAddressCity"), true);
		billingState.assertContains(ds.get(1).get("billingAddressState"), true);
		billingPostalCode.assertContains(ds.get(1).get("billingAddressPostalCode"), true);
		billingCountry.assertContains(ds.get(1).get("billingAddressCountry"), true);
		
		// TODO: VOOD-555 -need lib support for all account shipping address fields
		new VoodooControl("input", "css", "[name='shipping_address_city']").assertContains(ds.get(1).get("billingAddressCity"), true);
		new VoodooControl("input", "css", "[name='shipping_address_state']").assertContains(ds.get(1).get("billingAddressState"), true);
		new VoodooControl("input", "css", "[name='shipping_address_postalcode']").assertContains(ds.get(1).get("billingAddressPostalCode"), true);
		new VoodooControl("input", "css", "[name='shipping_address_country']").assertContains(ds.get(1).get("billingAddressCountry"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}