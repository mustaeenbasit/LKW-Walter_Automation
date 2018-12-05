package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_28315 extends SugarTest {

	public void setup() throws Exception {
		DataSource rliNames = testData.get(testName);

		// Create an account record 
		sugar().accounts.api.create();

		// Create an Opportunity record
		sugar().opportunities.api.create();

		// Create 5 RLI records
		sugar().revLineItems.api.create(rliNames);

		sugar().login();

		// Navigate to Opportunities module
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);

		// Add the Account name to an Opportunity
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").
		set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);

		// Navigate to the Revenue Line Items module
		sugar().navbar.navToModule(sugar().revLineItems.moduleNamePlural);

		// Mass Update the Opportunity name for the RLIs created via api
		sugar().navbar.navToModule(sugar().revLineItems.moduleNamePlural);
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();
		sugar().revLineItems.massUpdate.getControl("massUpdateField02").set("Opportunity Name");
		sugar().revLineItems.massUpdate.getControl("massUpdateValue02").
		set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.massUpdate.update();
	}

	/**
	 * Verify that RLI subpanel counts on any relationship module record view is displayed
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_28315_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet braces = testData.get(testName + "_bracesAroundRecordCount").get(0);

		// Navigate to the Opportunities module
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubpanel = (StandardSubpanel)sugar().opportunities.recordView.subpanels.
				get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.scrollIntoViewIfNeeded(false);
		rliSubpanel.expandSubpanel();

		// Assert that RLI Subpanel should display a count of the records related to the opportunity
		// TODO: VOOD-1578
		new VoodooControl("span", "css", ".layout_RevenueLineItems .count").assertEquals(braces.get("openBrace")+
				rliSubpanel.countRows() + braces.get("closeBrace"), true);

		// Navigate to the Accounts module
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel rliAccountsSubpanel = (StandardSubpanel)sugar().accounts.recordView.subpanels.
				get(sugar().revLineItems.moduleNamePlural);
		rliAccountsSubpanel.scrollIntoViewIfNeeded(false);
		rliAccountsSubpanel.expandSubpanel();
		// Assert that the RLI Subpanel should display a count of the records related to the opportunity i.e related to this Account
		// TODO: VOOD-1578
		new VoodooControl("span", "css", ".layout_RevenueLineItems .count").assertEquals(braces.get("openBrace")+
				rliAccountsSubpanel.countRows() + braces.get("closeBrace"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}