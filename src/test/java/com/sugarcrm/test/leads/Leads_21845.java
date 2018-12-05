package com.sugarcrm.test.leads;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Leads_21845 extends SugarTest {
	LeadRecord myLead;

	public void setup() throws Exception {
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Test Case 21845: Create Lead_Verify that lead can be duplicated when using "Copy" function in "Lead" detail view.
	 */
	@Test
	public void Leads_21845_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myLead.navToRecord();

		// Click "Copy" button in "Lead" detail view and Save
		sugar().leads.recordView.copy();
		sugar().leads.createDrawer.save();
		// Duplicate lead panel appears.
		sugar().leads.createDrawer.getControl("duplicateCount").waitForVisible();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
