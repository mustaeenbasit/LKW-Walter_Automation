package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import static org.junit.Assert.assertEquals;

public class Leads_delete extends SugarTest {
	LeadRecord myLead;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	@Test
	public void Leads_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the lead using the UI.
		myLead.delete();

		// Verify the lead was deleted.
		sugar().leads.navToListView();
		assertEquals(VoodooUtils.contains(myLead.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
