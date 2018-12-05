package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24342 extends SugarTest {
	LeadRecord myLead;
	OpportunityRecord myOpp;
	StandardSubpanel leadsSubpanel;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		myOpp.navToRecord();
		leadsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecord(myLead);
	}

	/**
	 * Test Case 24342: Unlink Lead_Verify that lead can be unlinked from "Leads" sub-panel of opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24342_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Unlink the lead
		leadsSubpanel.unlinkRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify that related lead is no longer exist in the leads subpanel
		leadsSubpanel.expandSubpanel();
		leadsSubpanel.assertContains(myLead.getRecordIdentifier(), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}