package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_29042 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.create();
		sugar().leads.create();
	}
	/**
	 * Verify that fields should be appearing with associated data while merging identical leads record.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_29042_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating the Leads Record using Copy
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		sugar().leads.recordView.getControl("copyButton").click();
		sugar().leads.createDrawer.save();
		sugar().leads.createDrawer.ignoreDuplicateAndSave();

		// Navigating to leads List View
		sugar().leads.navToListView();

		// Selecting all records in ListView
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.getControl("actionDropdown").click();

		// Clicking the "Merge" Action
		// TODO: VOOD-689
		new VoodooControl("a", "css", ".fld_merge_button.list a").click();
		sugar().alerts.getWarning().cancelAlert();

		String teamName = "Global";

		// Verifying the Values in Merge Form on the Primary Panel
		// TODO: VOOD-721
		new VoodooControl("span", "css", ".fld_salutation span")
				.assertContains(sugar().leads.defaultData.get("salutation"),true);
		new VoodooControl("input", "css",".fld_first_name.edit input")
				.assertContains(sugar().leads.defaultData.get("firstName"),true);
		new VoodooControl("input", "css",".fld_last_name.edit input")
				.assertContains(sugar().leads.defaultData.get("lastName"),true);
		new VoodooControl("input", "css",".fld_title.edit input")
				.assertContains(sugar().leads.defaultData.get("title"),true);
		new VoodooControl("input", "css",".fld_phone_mobile.edit input")
				.assertContains(sugar().leads.defaultData.get("phoneMobile"),true);
		new VoodooControl("input", "css",".fld_website.edit input")
	    		.assertContains(sugar().leads.defaultData.get("website"),true);
		new VoodooControl("textarea", "css",".fld_primary_address_street textarea")
				.assertContains(sugar().leads.defaultData.get("primaryAddressStreet"),true);
		new VoodooControl("input", "css",".fld_primary_address_city input")
				.assertContains(sugar().leads.defaultData.get("primaryAddressCity"),true);
		new VoodooControl("input", "css",".fld_primary_address_state input")
				.assertContains(sugar().leads.defaultData.get("primaryAddressState"),true);
		new VoodooControl("input", "css",".fld_primary_address_postalcode input")
				.assertContains(sugar().leads.defaultData.get("primaryAddressPostalCode"),true);
		new VoodooControl("input", "css",".fld_primary_address_country input")
				.assertContains(sugar().leads.defaultData.get("primaryAddressCountry"),true);
		new VoodooControl("textarea", "css",".fld_description.edit textarea")
				.assertContains(sugar().leads.defaultData.get("description"),true);
		new VoodooControl("input", "css",".fld_phone_fax.edit input")
				.assertContains(sugar().leads.defaultData.get("phoneFax"),true);
		new VoodooControl("input", "css",".fld_phone_work.edit input")
				.assertContains(sugar().leads.defaultData.get("phoneWork"),true);
		new VoodooControl("input", "css",".fld_department.edit input")
				.assertContains(sugar().leads.defaultData.get("department"),true);
		new VoodooControl("span", "css",".fld_status.edit span")
				.assertContains(sugar().leads.defaultData.get("status"),true);
		new VoodooControl("textarea", "css",".fld_status_description textarea")
				.assertContains(sugar().leads.defaultData.get("statusDescription"),true);
		new VoodooControl("span", "css",".fld_lead_source span")
				.assertContains(sugar().leads.defaultData.get("leadSource"),true);
		new VoodooControl("textarea", "css",".fld_lead_source_description textarea")
				.assertContains(sugar().leads.defaultData.get("leadSourceDescription"),true);
		new VoodooControl("label", "css",".fld_team_name.edit label")
				.assertContains(teamName, true);

		// Verifying the values in Merge Form on non-Primary Panel
		new VoodooControl("div", "css", ".fld_salutation .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("salutation"),true);
		new VoodooControl("div", "css",".fld_first_name .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("firstName"),true);
		new VoodooControl("div", "css",".fld_last_name .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("lastName"),true);
		new VoodooControl("div", "css",".list.fld_title .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("title"),true);
		new VoodooControl("div", "css",".fld_phone_mobile .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("phoneMobile"),true);
		new VoodooControl("div", "css",".fld_website .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("website"),true);
		new VoodooControl("div", "css",".fld_primary_address_street .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("primaryAddressStreet"),true);
		new VoodooControl("div", "css",".fld_primary_address_city .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("primaryAddressCity"),true);
		new VoodooControl("div", "css",".fld_primary_address_state .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("primaryAddressState"),true);
		new VoodooControl("div", "css",".fld_primary_address_postalcode .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("primaryAddressPostalCode"),true);
		new VoodooControl("div", "css",".fld_primary_address_country .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("primaryAddressCountry"),true);
		new VoodooControl("div", "css",".fld_description .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("description"),true);
		new VoodooControl("div", "css",".fld_phone_fax .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("phoneFax"),true);
		new VoodooControl("div", "css",".controls .list.fld_phone_work .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("phoneWork"),true);
		new VoodooControl("div", "css",".fld_department .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("department"),true);
		new VoodooControl("div", "css",".controls .list.fld_status .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("status"),true);
		new VoodooControl("div", "css",".fld_status_description .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("statusDescription"),true);
		new VoodooControl("div", "css",".fld_lead_source .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("leadSource"),true);
		new VoodooControl("div", "css",".fld_lead_source_description .ellipsis_inline")
				.assertContains(sugar().leads.defaultData.get("leadSourceDescription"),true);
		new VoodooControl("label", "css",".list.fld_team_name label")
				.assertContains(teamName, true);

		// Canceling the Merge Form
		new VoodooControl("a", "css",".merge-duplicates-headerpane.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}