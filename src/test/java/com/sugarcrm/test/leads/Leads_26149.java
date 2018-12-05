package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.LeadRecord;

public class Leads_26149 extends SugarTest {
	LeadRecord myLead;
	FieldSet leadsRecord;

	public void setup() throws Exception {
		leadsRecord = testData.get("Leads_26149").get(0);
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify the user can Cancel the inline editing from the record level
	 * action drop down on Leads List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_26149_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.editRecord(1);
		sugar().leads.listView.setEditFields(1, leadsRecord);
		sugar().leads.listView.cancelRecord(1);
		// Assert the original values are present
		sugar().leads.listView.verifyField(1, "status", myLead.get("status"));
		sugar().leads.listView.verifyField(1, "phoneWork",
				myLead.get("phoneWork"));
	}

	public void cleanup() throws Exception {}
}
