package com.sugarcrm.test.leads;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Leads_18013 extends SugarTest {
	FieldSet leadData, newLeadName;
	LeadRecord myLead, leadRecordCopy;


	public void setup() throws Exception {
		leadData = sugar().leads.getDefaultData();
		newLeadName = testData.get(testName).get(0);
		leadData.put("status", "Converted");
		myLead = (LeadRecord) sugar().leads.api.create(leadData);
		sugar().login();
	}

	/**
	 * Test Case 18013: Duplicating converted record having status as new
	 */
	@Test
	public void Leads_18013_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Copy a converted record, name is updated to distinguish copied record from original
		leadRecordCopy = (LeadRecord) myLead.copy(newLeadName);

		// Verify that new record has status=New, all values from original record is copied over
		leadData.put("status", "New");
		leadData.put("firstName",newLeadName.get("firstName"));
		leadData.put("fullName",newLeadName.get("fullName"));
		leadRecordCopy.verify(leadData);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}