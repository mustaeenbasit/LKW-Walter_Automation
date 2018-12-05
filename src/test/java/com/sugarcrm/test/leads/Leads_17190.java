package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17190 extends SugarTest {
	FieldSet editedLead;

	public void setup() throws Exception {	
		editedLead = testData.get(testName).get(0);
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify leads record can be modified from Leads module's List View via Edit button.
	 * @throws Exception
	 */	
	@Test
	public void Leads_17190_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().leads.navToListView();
		sugar().leads.listView.editRecord(1);
		sugar().leads.listView.setEditFields(1, editedLead);
		sugar().leads.listView.saveRecord(1);

		// Verify that the fields were successfully edited.
		sugar().leads.listView.verifyField(1, "fullName", editedLead.get("firstName")+" "+editedLead.get("lastName"));
		sugar().leads.listView.verifyField(1, "phoneWork", editedLead.get("phoneWork"));
		sugar().leads.listView.verifyField(1, "status", editedLead.get("status"));
		sugar().leads.listView.verifyField(1, "relAssignedTo", editedLead.get("relAssignedTo"));

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}