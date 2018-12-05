package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_18258 extends SugarTest {
	VoodooControl billingCtrl, cityCtrl, nameCtrl, moreColumn;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleSidebar();
		moreColumn = sugar().accounts.listView.getControl("moreColumn");
		moreColumn.click();

		// Disable or un-check, Billing Country, Email Address, and Date Created.
		// TODO: VOOD-1517
		billingCtrl = new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child(3) [data-field-toggle='billing_address_country']");
		billingCtrl.click();
		cityCtrl = new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child(2) [data-field-toggle='billing_address_city']");
		cityCtrl.click();
		nameCtrl = new VoodooControl("li", "css", ".dropdown-menu.left li:nth-child(1) [data-field-toggle='name']");
		nameCtrl.click();
		moreColumn.click();
	}

	/**
	 * Add columns on list view layout
	 * @throws Exception
	 */
	@Test
	public void Accounts_18258_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify that the unchecked items are no more shown in the listView
		// TODO: VOOD-1517
		VoodooControl listBillingCtrl = sugar().accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("billing_address_country")));
		listBillingCtrl.assertVisible(false);
		VoodooControl listcityCtrl = sugar().accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("billing_address_city")));
		listcityCtrl.assertVisible(false);
		VoodooControl listNameCtrl = sugar().accounts.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("name")));
		listNameCtrl.assertVisible(false);
		moreColumn.click();

		// Verifying that there is no checked sign before Billing Country, Email Address, and Date Created in dropdown.
		billingCtrl.assertAttribute("class", "active" , false);
		cityCtrl.assertAttribute("class", "active" , false);
		nameCtrl.assertAttribute("class", "active" , false);

		// Enable Billing Country, Email Address, and Date Created.
		billingCtrl.click();
		cityCtrl.click();
		nameCtrl.click();

		// Verifying that there is checked sign before Billing Country, Email Address, and Date Created in dropdown.
		billingCtrl.assertAttribute("class", "active" , true);
		cityCtrl.assertAttribute("class", "active" , true);
		nameCtrl.assertAttribute("class", "active" , true);
		moreColumn.click();

		// Verify that the items checked above shown in the listView
		listBillingCtrl.assertVisible(true);
		listcityCtrl.assertVisible(true);
		listNameCtrl.assertVisible(true);
		
		// Verify the data in above enabled fields
		FieldSet fs = sugar().accounts.getDefaultData();
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(fs.get("name"), true);
		sugar().accounts.listView.getDetailField(1, "billingAddressCountry").assertEquals(fs.get("billingAddressCountry"), true);
		sugar().accounts.listView.getDetailField(1, "billingAddressCity").assertEquals(fs.get("billingAddressCity"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}