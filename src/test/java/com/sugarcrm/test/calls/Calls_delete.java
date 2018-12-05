package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import static org.junit.Assert.assertEquals;

public class Calls_delete extends SugarTest {
	CallRecord myCall;

	public void setup() throws Exception {
		myCall = (CallRecord) sugar.calls.api.create();
		sugar.login();
	}

	@Test
	public void Calls_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the call using the UI.
		myCall.delete();

		// Verify the Call does not exist
		sugar.calls.navToListView();
		assertEquals(VoodooUtils.contains(myCall.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}