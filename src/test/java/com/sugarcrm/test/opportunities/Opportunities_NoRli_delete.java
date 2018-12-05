package com.sugarcrm.test.opportunities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_NoRli_delete extends SugarTest {
	OpportunityRecord myOpp;
	
	public void setup() throws Exception {
		sugar().login();
		sugar().accounts.api.create();
		myOpp = (OpportunityRecord)sugar().opportunities.create();
	}

	@Test
	public void Opportunities_NoRli_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the opportunity using the UI.
		myOpp.delete();
		
		// Verify the opportunity was deleted.
		sugar().opportunities.navToListView();
		assertEquals(VoodooUtils.contains(myOpp.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
} 