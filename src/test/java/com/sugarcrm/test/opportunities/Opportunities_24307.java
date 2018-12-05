package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24307 extends SugarTest {
	StandardSubpanel callsSubpanel;
	
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		CallRecord myCall = (CallRecord) sugar().calls.api.create();
		sugar().login();
		
		// Schedule Call record for an opportunity exist
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		callsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.linkExistingRecord(myCall);
	}

	/**
	 * Edit Scheduled Call_Verify that editing scheduled call related to an opportunity can be canceled
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24307_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// In Opportunity list view, click "edit" link for a scheduled call record in "Activities" sub-panel
		callsSubpanel.editRecord(1);
		
		// Change Subject of Calls record and click 'Cancel'
		callsSubpanel.getEditField(1, "name").set(testName);
		callsSubpanel.cancelAction(1);
		
		// Verify that the information of the call is not changed
		callsSubpanel.getDetailField(1, "name").assertContains(sugar().calls.getDefaultData().get("name"), true);
		callsSubpanel.getDetailField(1, "status").assertContains(sugar().calls.getDefaultData().get("status"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
