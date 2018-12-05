package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21998 extends SugarTest {

	public void setup() throws Exception {
		// Create account and lead
		sugar().accounts.api.create();
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Convert Lead_Verify that an existing account can be selected for converting a lead by using "Create Account" check box in "Related Record" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Leads_21998_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String accountName = sugar().accounts.getDefaultData().get("name");

		// Add existing account for the created lead
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		// Fill a existing account name
		sugar().leads.recordView.getEditField("accountName").set(accountName);
		sugar().contacts.recordView.save();

		// Convert Lead
		sugar().leads.recordView.openPrimaryButtonDropdown();
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();
		// Add account in the Account tab
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".active [data-module='Accounts'] .fld_associate_button a").click();
		VoodooUtils.waitForReady();
		// Click Save and Convert for converting lead
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Verfiy that there is no duplicate account on converting lead
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		Assert.assertTrue("Row count is not equal to 1 in Accounts list view", sugar().accounts.listView.countRows() == 1);
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(accountName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
