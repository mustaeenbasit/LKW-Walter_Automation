package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Leads_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		LeadRecord myLead = (LeadRecord)sugar().leads.create();
		myLead.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
