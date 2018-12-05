package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21858 extends SugarTest {
	DataSource leadsData;

	public void setup() throws Exception {
		sugar().login();
		leadsData = testData.get(testName);
		sugar().leads.create(leadsData);
	}
	
	/**
	 * Sort Leads_Verify that leads can be sorted by column in "Leads" list view.
	 * @throws Exception
	 */	
	@Test
	public void Leads_21858_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set Name in Ascending Order.
		sugar().leads.listView.sortBy("headerFullname", true);

		// Verification of Name in Ascending Order
		sugar().leads.listView.verifyField(1, "fullName", (sugar().leads.getDefaultData().get("salutation")+" "+leadsData.get(2).get("firstName") + " " + leadsData.get(2).get("lastName")));
		sugar().leads.listView.verifyField(2, "fullName", (sugar().leads.getDefaultData().get("salutation")+" "+leadsData.get(1).get("firstName") + " " + leadsData.get(1).get("lastName")));
		sugar().leads.listView.verifyField(3, "fullName",  (sugar().leads.getDefaultData().get("salutation")+" "+leadsData.get(0).get("firstName") + " " + leadsData.get(0).get("lastName")));

		// Set Status in Ascending Order
		sugar().leads.listView.sortBy("headerStatus", true);

		// Verification of Status in Ascending Order
		sugar().leads.listView.verifyField(1, "status", leadsData.get(1).get("status"));
		sugar().leads.listView.verifyField(2, "status", leadsData.get(2).get("status"));
		sugar().leads.listView.verifyField(3, "status", leadsData.get(0).get("status"));

		// Set Office Phone in Ascending Order.
		sugar().leads.listView.sortBy("headerPhonework", true);

		// Verification of Office Phone in Ascending Order
		sugar().leads.listView.verifyField(1, "phoneWork", leadsData.get(2).get("phoneWork"));
		sugar().leads.listView.verifyField(2, "phoneWork", leadsData.get(1).get("phoneWork"));
		sugar().leads.listView.verifyField(3, "phoneWork", leadsData.get(0).get("phoneWork"));

		// Set Name in Descending Order
		sugar().leads.listView.sortBy("headerFullname", false);

		// Verification of Name in Descending Order
		sugar().leads.listView.verifyField(1, "fullName", (sugar().leads.getDefaultData().get("salutation")+" "+leadsData.get(0).get("firstName") + " " + leadsData.get(0).get("lastName")));
		sugar().leads.listView.verifyField(2, "fullName", (sugar().leads.getDefaultData().get("salutation")+" "+leadsData.get(1).get("firstName") + " " + leadsData.get(1).get("lastName")));
		sugar().leads.listView.verifyField(3, "fullName",  (sugar().leads.getDefaultData().get("salutation")+" "+leadsData.get(2).get("firstName") + " " + leadsData.get(2).get("lastName")));

		// Set Status in Descending Order
		sugar().leads.listView.sortBy("headerStatus", false);

		// Verification of Status in Descending Order
		sugar().leads.listView.verifyField(1, "status", leadsData.get(0).get("status"));
		sugar().leads.listView.verifyField(2, "status", leadsData.get(2).get("status"));
		sugar().leads.listView.verifyField(3, "status", leadsData.get(1).get("status"));

		// Set Office Phone in Descending Order
		sugar().leads.listView.sortBy("headerPhonework", false);

		// Verification of Office Phone in Descending Order
		sugar().leads.listView.verifyField(1, "phoneWork", leadsData.get(0).get("phoneWork"));
		sugar().leads.listView.verifyField(2, "phoneWork", leadsData.get(1).get("phoneWork"));
		sugar().leads.listView.verifyField(3, "phoneWork", leadsData.get(2).get("phoneWork"));

		// TODO: VOOD-1450
		// Verification through Date Created can be done once VOOD-1450 gets fixed.

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}