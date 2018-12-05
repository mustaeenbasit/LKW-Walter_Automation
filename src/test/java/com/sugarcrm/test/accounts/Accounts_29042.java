package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_29042 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.create();
	}

	/**
	 * Verify that fields should be appearing with associated data while merge identical record.
	 * @throws Exception
	 */
	@Test
	public void Accounts_29042_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating the Account Record using Copy
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		sugar().accounts.recordView.getControl("copyButton").click();
		sugar().accounts.createDrawer.save();
		sugar().accounts.createDrawer.ignoreDuplicateAndSave();

		// Navigating to Accounts List View
		sugar().accounts.navToListView();

		// Selecting all records in ListView
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.getControl("actionDropdown").click();
		
		// Clicking the "Merge" Action
		// TODO: VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list a").click();
		sugar().alerts.getWarning().cancelAlert();

		String teamName = "Global";

		// Verifying the Values in Merge Form on the Primary Panel
		// TODO: VOOD-721
		new VoodooControl("input", "css", ".primary-edit-mode .fld_name input").assertContains(sugar().accounts.defaultData.get("name"), true);
		new VoodooControl("input", "css",".primary-edit-mode .fld_website.edit input").assertContains(sugar().accounts.defaultData.get("website"), true);
		new VoodooControl("span", "css",".primary-edit-mode .fld_industry.edit").assertContains(sugar().accounts.defaultData.get("industry"), true);
		new VoodooControl("span", "css",".primary-edit-mode .fld_account_type.edit").assertContains(sugar().accounts.defaultData.get("type"), true);
		new VoodooControl("input", "css",".primary-edit-mode .fld_phone_office.edit input").assertContains(sugar().accounts.defaultData.get("workPhone"),true);
		new VoodooControl("textarea", "css",".primary-edit-mode .address_street.fld_billing_address_street.edit textarea").assertContains(sugar().accounts.defaultData.get("billingAddressStreet"),true);
		new VoodooControl("input", "css",".primary-edit-mode .address_city.fld_billing_address_city.edit input").assertContains(sugar().accounts.defaultData.get("billingAddressCity"),true);

		// Scrolling down the page to view all the Fields
		VoodooControl teamCtrl = new VoodooControl("label", "css",".primary-edit-mode .fld_team_name.edit label");
		teamCtrl.scrollIntoViewIfNeeded(false);
		teamCtrl.assertContains(teamName, true);
		new VoodooControl("input", "css",".primary-edit-mode .address_state.fld_billing_address_state.edit input").assertContains(sugar().accounts.defaultData.get("billingAddressState"),true);
		new VoodooControl("input", "css",".primary-edit-mode .address_zip.fld_billing_address_postalcode.edit input").assertContains(sugar().accounts.defaultData.get("billingAddressPostalCode"), true);
		new VoodooControl("input", "css",".primary-edit-mode .address_country.fld_billing_address_country.edit input").assertContains(sugar().accounts.defaultData.get("billingAddressCountry"),true);
		new VoodooControl("input", "css",".primary-edit-mode .fld_phone_fax.edit input").assertContains(sugar().accounts.defaultData.get("fax"), true);
		new VoodooControl("input", "css",".primary-edit-mode .fld_sic_code.edit input").assertContains(sugar().accounts.defaultData.get("sicCode"), true);

		// Verifying the values in Merge Form on non-Primary Panel
		new VoodooControl("div", "css", ".col:nth-child(2) .fld_name div").assertContains(sugar().accounts.defaultData.get("name"), true);
		new VoodooControl("a", "css", ".col:nth-child(2) .fld_website a").assertContains(sugar().accounts.defaultData.get("website"), true);
		new VoodooControl("div", "css", ".col:nth-child(2) .fld_industry div").assertContains(sugar().accounts.defaultData.get("industry"),true);
		new VoodooControl("div", "css",".col:nth-child(2) .fld_account_type div").assertContains(sugar().accounts.defaultData.get("type"), true);
		new VoodooControl("div", "css",".col:nth-child(2) .fld_phone_office div").assertContains(sugar().accounts.defaultData.get("workPhone"), true);
		new VoodooControl("div", "css",".col:nth-child(2) .address_street.fld_billing_address_street div").assertContains(sugar().accounts.defaultData.get("billingAddressStreet"),true);
		new VoodooControl("div", "css",".col:nth-child(2) .address_city.fld_billing_address_city div").assertContains(sugar().accounts.defaultData.get("billingAddressCity"),true);
		new VoodooControl("div", "css",".col:nth-child(2) .address_state.fld_billing_address_state div").assertContains(sugar().accounts.defaultData.get("billingAddressState"),true);
		new VoodooControl("div", "css",".col:nth-child(2) .address_zip.fld_billing_address_postalcode div").assertContains(sugar().accounts.defaultData.get("billingAddressPostalCode"), true);
		new VoodooControl("div", "css",".col:nth-child(2) .address_country.fld_billing_address_country div").assertContains(sugar().accounts.defaultData.get("billingAddressCountry"),true);
		new VoodooControl("div", "css", ".col:nth-child(2) .fld_phone_fax div").assertContains(sugar().accounts.defaultData.get("fax"), true);
		new VoodooControl("div", "css", ".col:nth-child(2) .fld_sic_code div").assertContains(sugar().accounts.defaultData.get("sicCode"), true);
		new VoodooControl("label", "css",".col:nth-child(2) .fld_team_name label").assertContains(teamName, true);

		// Canceling the Merge Form
		new VoodooControl("a", "css",".merge-duplicates-headerpane.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}