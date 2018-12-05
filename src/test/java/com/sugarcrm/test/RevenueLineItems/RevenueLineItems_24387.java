package com.sugarcrm.test.RevenueLineItems;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Ignore;
import org.junit.Test;

public class RevenueLineItems_24387 extends SugarTest {
	OpportunityRecord myOpp;
	FieldSet rliCustomData;
	StandardSubpanel RLISubpanel;

	public void setup() throws Exception {
		RLISubpanel = sugar().opportunities.recordView.subpanels.get("");
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		rliCustomData = testData.get(testName).get(0);
	}

	/**
	 * Test Case 24387: Create Opportunity_Verify that the amount with large number (such as "99999999999999999999")
	 * for an opportunity can be saved correctly.
	 *
	 * @throws Exception
	 */
	@Ignore("SFA-2733")
	@Test
	public void RevenueLineItems_24387_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a RLI with custom large amount
		sugar().revLineItems.create(rliCustomData);

		// Nav to opportunity
		myOpp.navToRecord();

		// And verify its amount is equal to RLI amount
		sugar().opportunities.recordView.showMore();
		sugar().opportunities.recordView.getDetailField("oppAmount")
				.assertContains(rliCustomData.get("likelyCase"), true);
		sugar().opportunities.recordView.getDetailField("bestCase")
				.assertContains(rliCustomData.get("likelyCase"), true);
		sugar().opportunities.recordView.getDetailField("worstCase")
				.assertContains(rliCustomData.get("likelyCase"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}