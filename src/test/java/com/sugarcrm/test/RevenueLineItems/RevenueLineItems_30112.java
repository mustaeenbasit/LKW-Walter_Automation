package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class RevenueLineItems_30112 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify RevenueLineItem name, Opportunity name & Account name in RLI subpanel
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30112_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-444 - Once resolved, Opp should create via API with account and RLI association
		// Opportunity with account and RLI record
		sugar().opportunities.create();

		// Navigate to Accounts -> RLI subpanel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel rliSubpanel = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.scrollIntoViewIfNeeded(false);
		rliSubpanel.expandSubpanel();
		rliSubpanel.scrollIntoViewIfNeeded(false);

		// Verify RevenueLineItem, Opportunity name & Account name should not be showing blank
		// TODO: VOOD-1424 - Once resolved fields verification with verify() methods
		rliSubpanel.getDetailField(1, "name").assertEquals(sugar().opportunities.getDefaultData().get("rli_name"), true);
		rliSubpanel.getDetailField(1, "relOpportunityName").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);
		rliSubpanel.getDetailField(1, "relAccountName").assertEquals(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}