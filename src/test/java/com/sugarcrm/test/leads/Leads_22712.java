package com.sugarcrm.test.leads;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.StandardRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Leads_22712 extends SugarTest {
	StandardRecord existingContact, myLead;
	FieldSet leadContactNameData = new FieldSet();

	public void setup() throws Exception {
		FieldSet defaultContactData = sugar().contacts.getDefaultData();
		
		// Create FieldSet for a lead with first and last name of a contact
		leadContactNameData = sugar().leads.getDefaultData();
		leadContactNameData.put("firstName", defaultContactData.get("firstName"));
		leadContactNameData.put("lastName", defaultContactData.get("lastName"));
		leadContactNameData.put("fullName", defaultContactData.get("fullName"));

		existingContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create(leadContactNameData);
		sugar().login();
	}

	/**
	 * Convert Lead_Verify that lead can be converted to an existing contact.
	 * @throws Exception
	 */
	@Test
	public void Leads_22712_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customAssertionsData = testData.get(testName).get(0);
		
		// Go to created lead Recordview
		myLead.navToRecord();
		
		// Click "Convert Lead" button in "Lead" detail view.
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();

		// See Contact subpanel
		// There should be "Contact: 1 duplicates found". Contact1 should be shown below
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", ".active .fld_Contacts_select input").waitForVisible();
		new VoodooControl("span", "css", ".active [data-module='Contacts']")
				.assertContains(customAssertionsData.get("dupContactAssert"), true);

		// Click Select radio button left to Contact1 name in the duplicates list
		VoodooControl contactField = new VoodooControl("input", "css", ".active .fld_Contacts_select input");
		contactField.waitForVisible();
		contactField.click();

		// Click Associate Account button
		new VoodooControl("span", "css", ".active [data-module='Contacts'] .fld_associate_button a").click();

		// Fill in Opportunity name and click Associate Opportunity
		VoodooControl accNameField = new VoodooControl("input", "css", "#collapseAccounts .fld_name input");
		accNameField.waitForVisible();
		accNameField.set(customAssertionsData.get("accountName"));
		new VoodooControl("span", "css", ".active [data-module='Accounts'] .fld_associate_button a").click();

		// Click Save and Convert.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Verify you are on a converted lead record view
		sugar().leads.recordView.getDetailField("fullName").assertContains(leadContactNameData.get("fullName"), true);
		new VoodooControl("span", "css", ".detail.fld_converted span").assertContains
			(customAssertionsData.get("convertedBadge"), true);

		// Verify detailed information for the existing Contact should not be changed
		existingContact.verify();

		// Verify new contact should not be created
		existingContact.delete();
		
		sugar().contacts.navToListView();
		sugar().contacts.listView.setSearchString(existingContact.get("fullName"));
		sugar().contacts.listView.getControl("emptyListViewMsg").waitForVisible();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}