package com.sugarcrm.test.inboundEmail;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.InboundEmailRecord;
import com.sugarcrm.test.SugarTest;

public class InboundEmail_create extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	@Test
	public void InboundEmail_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		InboundEmailRecord inbound1 = (InboundEmailRecord)sugar.inboundEmail.create();
		inbound1.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
