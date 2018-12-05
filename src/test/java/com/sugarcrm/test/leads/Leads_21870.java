package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.*;
import com.sugarcrm.test.SugarTest;

public class Leads_21870 extends SugarTest {
	LeadRecord myLead;
	FieldSet leadDefaultData = new FieldSet();

	public void setup() throws Exception {
		// Create FieldSet for a lead with related account name
		leadDefaultData = sugar().leads.getDefaultData();
		leadDefaultData.put("accountName", sugar().accounts.getDefaultData().get("name"));
		sugar().login();
		myLead = (LeadRecord) sugar().leads.create(leadDefaultData);
	}

	/**
	 * Convert Lead_Verify that lead can be converted to a contact with creating account and opportunity
	 * @throws Exception
	 */
	@Test
	public void Leads_21870_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set required fields for account, contact and opportunity
		FieldSet accountReducedData = new FieldSet();
		accountReducedData.put("name", leadDefaultData.get("accountName"));
		FieldSet contactReducedData = new FieldSet();
		contactReducedData.put("firstName", leadDefaultData.get("firstName"));
		contactReducedData.put("lastName", leadDefaultData.get("lastName"));
		contactReducedData.put("fullName", leadDefaultData.get("fullName"));
		FieldSet oppReducedData = new FieldSet();
		oppReducedData.put("name", sugar().opportunities.getDefaultData().get("name"));

		// Go to created lead Record view
		myLead.navToRecord();
		
		// Click "Convert Lead" button in "Lead" detail view.
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// See Contact subpanel and verify it contains lead's first and last name
		new VoodooControl("span", "css", ".layout_Leads [data-module=Contacts] span.title")
			.assertContains(contactReducedData.get("fullName"), true);

		// Wait until account form is loaded, verify it contains account name from the lead
		new VoodooControl("span", "css", ".layout_Leads [data-module=Accounts] span.title")
			.assertContains(accountReducedData.get("name"), true);

		// Fill in Opportunity name and click Associate Opportunity
		new VoodooControl("input", "css", "#collapseOpportunities .fld_name input").
			set(oppReducedData.get("name"));
		new VoodooControl("span", "css", ".active [data-module='Opportunities'] .fld_associate"
				+ "_button a").click();

		// Click Save and Convert.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify you are on a converted lead record view
		sugar().leads.recordView.getDetailField("fullName").assertContains(leadDefaultData.get
				("fullName"), true);
		new VoodooControl("span", "css", ".detail.fld_converted span").assertContains("Converted", true);

		// Verify links to conversion results are available into converted lead record view
		VoodooControl convResults = new VoodooControl("table","css",".converted-results");
		convResults.assertContains(accountReducedData.get("name"), true);
		convResults.assertContains(contactReducedData.get("lastName"), true);
		convResults.assertContains(oppReducedData.get("name"), true);

		// Verify created records
		new AccountRecord(accountReducedData).verify(accountReducedData);
		new ContactRecord(contactReducedData).verify(contactReducedData);
		new OpportunityRecord(oppReducedData).verify(oppReducedData);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}