package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21997 extends SugarTest {

	public void setup() throws Exception {
		// Create Account 
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Convert Lead_Verify that a duplicate account can be created for converting lead.
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_21997_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Leads" tab on navigation bar
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);

		// Create a new lead
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		String accountName = sugar().accounts.getDefaultData().get("name");
		// Enter account name same as created in set up
		sugar().leads.createDrawer.getEditField("accountName").set(accountName);

		// Save
		sugar().leads.createDrawer.save();

		// From Edit button select "Convert" option. 
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// Click into the "Ignore and Create a New" button.
		new VoodooControl("a", "css", "[data-module='Accounts'] .toggle-link.dupecheck").click();

		// Account name is pre-populated with the existing account data
		new VoodooControl("input", "css", "[data-module='Accounts'] [name='name']").assertEquals(accountName, true);

		// Click "Create Account"
		new VoodooControl("a", "css", "[data-module='Accounts'] [name='associate_button']").click();
		VoodooUtils.waitForReady();

		// Fill in Opportunity name and click Associate Opportunity
		// TODO: VOOD-585
		new VoodooControl("input", "css", "#collapseOpportunities .fld_name input").set(testName);
		new VoodooControl("span", "css", ".active [data-module='Opportunities'] .fld_associate_button a").click();

		// Click Save and Convert.
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Navigate to Accounts Module
		sugar().accounts.navToListView();

		// Verify two account records with same name are created
		Assert.assertTrue("Accounts list view doesn't have two records", sugar().accounts.listView.countRows() == 2);
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountName, true);
		sugar().accounts.listView.getDetailField(2, "name").assertEquals(accountName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}