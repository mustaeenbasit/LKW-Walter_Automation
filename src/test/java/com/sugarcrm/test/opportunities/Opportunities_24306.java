package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24306 extends SugarTest {
	StandardSubpanel callsSubpanel;
	
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		CallRecord myCallsData = (CallRecord) sugar().calls.api.create();
		sugar().login();
		
		// Link Calls record with Opportunity 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		callsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.linkExistingRecord(myCallsData);	
	}

	/**
	 * Edit Scheduled Call_Verify that scheduled call related to an opportunity can be modified when using "Edit" 
	 * function in "Activities" sub-panel.
	 * 
	 */
	@Test
	public void Opportunities_24306_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to opportunity recordview
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		FieldSet newcallsData = new FieldSet();
		newcallsData.put("name", testName);
		newcallsData.put("status", "Held");
		
		// Edit the information of the call and save it
		callsSubpanel.editRecord(1, newcallsData);
		
		// Verify that Calls Subpanel is visible and expand it
		callsSubpanel.assertVisible(true);
		callsSubpanel.expandSubpanel();
		
		// Verify that the modified call is displayed in "Activities"  "Calls" sub-panel correctly
		// TODO: VOOD-1424
		// callsSubpanel.verify(1, newcallsData, true);
		callsSubpanel.assertContains(newcallsData.get("name"), true);
		callsSubpanel.assertContains(newcallsData.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}