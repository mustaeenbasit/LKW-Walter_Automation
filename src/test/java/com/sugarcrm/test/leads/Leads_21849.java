package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_21849 extends SugarTest {
	FieldSet customData;
	LeadRecord myLead;

	public void setup() throws Exception {
		customData = testData.get("Leads_21849").get(0);
		sugar().login();
		myLead = (LeadRecord) sugar().leads.api.create();
	}

	/**
	 * Test Case 21849: Update Lead_Verify that the lead is not changed when using "Cancel" function for editing a lead.
	 */
	@Test
	public void Leads_21849_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Go to created lead
		myLead.navToRecord();
		// Click edit
		sugar().leads.recordView.edit();
		sugar().leads.recordView.showMore();
		// Update all fields
		sugar().leads.recordView.setFields(customData);
		// Cancel editions
		sugar().leads.recordView.cancel();

		// Verify that fields weren't updated
		myLead.verify();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}