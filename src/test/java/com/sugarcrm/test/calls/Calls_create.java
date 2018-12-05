package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_create extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	@Test
	public void Calls_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		CallRecord myCall = (CallRecord)sugar.calls.create();
		myCall.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}