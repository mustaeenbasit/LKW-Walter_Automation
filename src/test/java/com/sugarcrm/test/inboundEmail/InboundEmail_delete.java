package com.sugarcrm.test.inboundEmail;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.InboundEmailRecord;

import static org.junit.Assert.assertEquals;

public class InboundEmail_delete extends SugarTest {
	InboundEmailRecord inboundEmail;
	
	public void setup() throws Exception {
		sugar.login();
		inboundEmail = (InboundEmailRecord)sugar.inboundEmail.create();
	}

	@Test
	public void InboundEmail_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the inbound email using the UI.
		inboundEmail.delete();
		
		// Verify the inbound email record was deleted.
		assertEquals(VoodooUtils.contains(inboundEmail.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
