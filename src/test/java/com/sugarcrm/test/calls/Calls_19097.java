package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_19097 extends SugarTest {
	CallRecord myCall;
	
	public void setup() throws Exception {
		myCall = (CallRecord)sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * Edit call_Verify that "Select" button is displayed under "Related To" field on 
	 * the call edit view without any notice.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_19097_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCall.navToRecord();
		sugar.calls.recordView.edit();
		
		// Verify that "Select" button is displayed under "Related To" field on the call edit view without any notice.
		sugar.calls.recordView.getEditField("relatedToParentName").assertContains("Select", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}