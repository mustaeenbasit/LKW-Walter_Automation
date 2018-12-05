package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22656 extends SugarTest{
	StandardSubpanel callSubpanel;

	public void setup() throws Exception {
		// Create Lead and Call records via REST API.
		sugar().leads.api.create();
		CallRecord myCall = (CallRecord)sugar().calls.api.create();
		
		// Login to system as valid user, here logging in as QAUser for coverage.
		sugar().login(sugar().users.getQAUser());
		
		// Call record scheduled for a lead exist.
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		callSubpanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callSubpanel.linkExistingRecord(myCall);
	}

	/**
	 * Edit Scheduled Call_Verify that editing scheduled call related to a lead can be canceled.
	 * @throws Exception
	 */
	@Test
	public void Leads_22656_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click "Leads" tab on navigation bar.
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);
		
		// Click "edit" link for a scheduled call record in Calls sub-panel.
		callSubpanel.editRecord(1);
		
		// Modify a field of the call and cancel it.
		callSubpanel.getEditField(1, "name").set(testName);
		callSubpanel.cancelAction(1);
		
		// Verify the information of the call is not changed.
		callSubpanel.getDetailField(1, "name").assertEquals(sugar().calls.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
