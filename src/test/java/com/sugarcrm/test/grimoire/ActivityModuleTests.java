package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class ActivityModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void ActivityModuleTests_create() throws Exception {
		VoodooUtils.voodoo.log.info("Running create()...");

		CallRecord myCall = (CallRecord)sugar().calls.create();
		myCall.verify();

		VoodooUtils.voodoo.log.info("create() complete.");
	}

	public void cleanup() throws Exception {}
}