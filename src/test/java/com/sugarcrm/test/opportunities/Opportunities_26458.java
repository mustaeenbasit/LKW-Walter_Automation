package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_26458 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().leads.api.create();
		sugar().login();

		// Create test Currency
		FieldSet currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", customData.get("rate"));
		currencyData.put("currencySymbol", customData.get("symbol"));
		currencyData.put("ISOcode", customData.get("code"));
		sugar().admin.setCurrency(currencyData);

		// Logout from admin user
		sugar().logout();
	}

	/**
	 * Verify that user preferred currency is saved for a new opportunity created after lead conversion
	 * @throws Exception
	 */
	@Test
	public void Opportunities_26458_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// login as QAUser
		sugar().login( sugar().users.getQAUser() );

		FieldSet fs = new FieldSet();
		fs.put("advanced_preferedCurrency", customData.get("name") + " : " + customData.get("symbol"));
		sugar().users.setPrefs(fs);

		// Navigates to Lead record and open primary button
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		VoodooUtils.waitForReady();

		// Associate Account
		new VoodooControl("input", "css","div[data-module='Accounts'] .fld_name.edit input").set(customData.get("acc_name"));
		new VoodooControl("a", "css","div[data-module='Accounts'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();

		// Fill in Opportunity name and click Associate Opportunity
		new VoodooControl("input", "css", "#collapseOpportunities .fld_name input").set(testName);
		new VoodooControl("a", "css", ".active [data-module='Opportunities'] .fld_associate_button a").click();		
		VoodooUtils.waitForReady();

		// Save & Convert
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Verify the values of the created opportunity are displayed using the default currency - Euro
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// TODO: VOOD-1402,VOOD-1445
		new VoodooControl("label", "css", ".fld_amount.detail.disabled .currency-field label").assertContains(customData.get("symbol"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}