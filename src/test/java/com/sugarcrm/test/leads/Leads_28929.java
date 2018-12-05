package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_28929 extends SugarTest {

	public void setup() throws Exception {
		// Creating a Lead record via API
		sugar().leads.api.create();

		// Log-in via administrator
		sugar().login();
	}

	/**
	 * Verify that user is able to reset the contact-name during lead conversion
	 * @throws Exception
	 */
	@Test
	public void Leads_28929_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet contactDefaultData = sugar().contacts.getDefaultData();
		FieldSet leadsDefaultData = sugar().leads.getDefaultData();

		// Navigate to the Lead's record view
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);

		// click the primary button dropdown to reveal the options
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// Converting the Lead
		// TODO: VOOD-695 - Library support for accessing options i.e in primary button drop-down
		// Click the Convert option from the drop down
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		VoodooControl firstNameLeadConvert = new VoodooControl("input", "css", ".fld_first_name.edit input");
		VoodooControl lastNameLeadConvert = new VoodooControl("input", "css", ".fld_last_name.edit input");
		VoodooControl contactNameLeadConvertForm = new	VoodooControl("span", "css", "div[data-module='Contacts'] .title");

		// Assert that "Creating Contact" section contains lead's name
		contactNameLeadConvertForm.assertContains(leadsDefaultData.get("fullName"), true);

		// Click reset button to edit the contact
		new VoodooControl("a", "css", "div[data-module='Contacts'] .fld_reset_button a").click();

		// Set the firstName of the contact as firstName from Contact's default Data
		firstNameLeadConvert.set(contactDefaultData.get("firstName"));

		// Set the lastName of the contact as lastName from Contact's default Data
		lastNameLeadConvert.set(contactDefaultData.get("lastName"));

		// Click Create Contact button
		new VoodooControl("a", "css", "div[data-module='Contacts'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();

		// Used assertContains() because the contact name contains leading and trailing spaces
		// Assert that "Creating Contact" section contains new contact name  
		contactNameLeadConvertForm.assertContains(contactDefaultData.get("fullName"), true);

		// Assert that "Creating Contact" section does not contain old contact name i.e lead name  
		contactNameLeadConvertForm.assertContains(leadsDefaultData.get("fullName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}