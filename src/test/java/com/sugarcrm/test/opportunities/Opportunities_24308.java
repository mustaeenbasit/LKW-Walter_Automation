package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24308 extends SugarTest {
	CallRecord myCall;
	StandardSubpanel callsSubpanel;
	
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		myCall = (CallRecord) sugar().calls.api.create();
		sugar().login();
		
		// Navigate to Opportunities record view and link a call
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		callsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.linkExistingRecord(myCall);
	}

	/**
	 * Remove Scheduled Call_Verify that scheduled call can be removed from "Opportunity" detail 
	 * view using unlink function
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24308_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Unlink call record from subpanel
		callsSubpanel.unlinkRecord(1);
		
		// Verify that call related to the opportunity disappears in "Calls" sub-panel
		int recordCount = callsSubpanel.countRows();
		Assert.assertTrue("Record is still exist in Calls subpanel", recordCount == 0);
		
		// Verify that Opportunity name is not displayed for the unlinked call in 'Calls' module
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.getDetailField("relatedToParentName").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}