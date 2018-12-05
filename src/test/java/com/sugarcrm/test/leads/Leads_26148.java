package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Leads_26148 extends SugarTest {
	FieldSet leadsRecord;

	public void setup() throws Exception {
		sugar().leads.api.create();
		leadsRecord = testData.get("Leads_26148").get(0);
		sugar().login();
	}

	/**
	 * Verify that the in line edit works for Leads List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_26148_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		sugar().leads.navToListView();
		// Inline Edit the record and save
		sugar().leads.listView.updateRecord(1, leadsRecord);
		// Verify Record updated
		sugar().leads.listView.verifyField(1, "status",
				leadsRecord.get("status"));
		sugar().leads.listView.verifyField(1, "phoneWork",
				leadsRecord.get("phoneWork"));
	}

	public void cleanup() throws Exception {}
}
