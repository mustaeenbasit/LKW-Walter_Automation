package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27575 extends SugarTest {
	CallRecord myCall;
	
	public void setup() throws Exception {
		sugar.login();
		myCall = (CallRecord) sugar.calls.create();
	}

	/**
	 * Verify that no warning appear when user navigates away from edit mode nothing is edited
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_27575_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		myCall.navToRecord();
		sugar.calls.recordView.edit();
		
		// Verify that Opportunity list view loads, no any warn message. 
		sugar.opportunities.navToListView();
		sugar.alerts.assertContains("Warning", false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}