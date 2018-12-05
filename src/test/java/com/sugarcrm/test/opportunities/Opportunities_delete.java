package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;

import static org.junit.Assert.assertEquals;

public class Opportunities_delete extends SugarTest {
	OpportunityRecord myOpp;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		myOpp = (OpportunityRecord)sugar().opportunities.create();
	}

	@Test
	public void Opportunities_delete_execute() throws Exception {
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