package com.sugarcrm.test.opportunities;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Opportunities_27829 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that RLI module is not available in Module Name dropdown in Web Logic Hooks create mode when project from Opportunities only
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27829_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.api.switchToRevenueLineItemsView();
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		// Confirm that Revenue subpanel is visible
		new VoodooControl("div", "css", ".layout_RevenueLineItems").assertExists(true);
		
		sugar().admin.api.switchToOpportunitiesView();
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		// Confirm that Revenue subpanel is not visible
		new VoodooControl("div", "css", ".layout_RevenueLineItems").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}