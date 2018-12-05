package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.CallRecord;

public class Calls_update extends SugarTest {
	CallRecord myCall;

	public void setup() throws Exception {
		myCall = (CallRecord)sugar.calls.api.create();
		sugar.login();
	}

	@Test
	public void Calls_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "A different Sales Call");

		// Edit the call using the UI.
		myCall.edit(newData);
		
		// Verify the call was edited.
		myCall.verify();
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}