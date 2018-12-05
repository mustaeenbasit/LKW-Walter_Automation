package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_21848 extends SugarTest {
	FieldSet customData;
	LeadRecord myLead, editedLead;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		editedLead = new LeadRecord(customData);
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Update Lead_Verify that lead can be displayed as modified after saving editing with all the fields modified.
	 * @throws Exception
	 */
	@Test
	public void Leads_21848_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Go to created lead record
		myLead.navToRecord();

		// Click edit
		sugar().leads.recordView.edit();
		sugar().leads.recordView.showMore();

		// Update all fields
		sugar().leads.recordView.setFields(customData);

		// Save and view edited record
		sugar().leads.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Verify all fields were updated
		editedLead.verify();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}