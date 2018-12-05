package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_17561 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that the Sales Stage column is not present in the Opportunities list view for ENT flavor or higher
	 * @throws Exception
	 */
	@Test
	public void Opportunities_17561_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();

		// TODO: VOOD-1359 - Once Resolved, set, get and remove header code removed from the script
		String salesStage = String.format("%s", "sales_stage");
		sugar().opportunities.listView.addHeader(salesStage);

		// Verify sales stage column header is present, when OPP view
		VoodooControl salesStageHeader = sugar().opportunities.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(salesStage)));
		salesStageHeader.assertExists(false);
		sugar().admin.api.switchToOpportunitiesView();

		// Verify sales stage column header is not present, when OPP + RLI view
		sugar().opportunities.navToListView();
		salesStageHeader.assertExists(true);
		sugar().opportunities.listView.removeHeader(salesStage);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}