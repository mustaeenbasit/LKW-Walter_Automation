package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.LeadRecord;

public class Leads_update extends SugarTest {
	LeadRecord myLead;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	@Test
	public void Leads_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("website", "http://www.ibm.com/");

		// Edit the lead using the UI.
		myLead.edit(newData);
		
		// Verify the lead was edited.
		myLead.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
