package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class Opportunities_26126 extends SugarTest {
	OpportunityRecord myOpportunity;
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		
		// TODO: VOOD-444 Support creating relationships via API
		myOpportunity = (OpportunityRecord) sugar().opportunities.create();
	}

	/**
	 * Verify Navigation and display of the Opportunities Preview pane
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_26126_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click the Preview (eye icon) next to the Opportunity just created
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.previewRecord(1);
		
		// Examine the Preview panel
		myOpportunity.verifyPreview();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}