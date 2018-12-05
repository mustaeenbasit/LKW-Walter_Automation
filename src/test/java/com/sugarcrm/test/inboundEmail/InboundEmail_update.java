package com.sugarcrm.test.inboundEmail;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.InboundEmailRecord;

public class InboundEmail_update extends SugarTest {
	InboundEmailRecord inbound1;

	public void setup() throws Exception {
		sugar.login();
		inbound1 = (InboundEmailRecord)sugar.inboundEmail.create();
	}

	@Test
	public void InboundEmail_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Edited name of Inbound Email");

		// Edit the inbound email using the UI.
		inbound1.edit(newData);
		
		// Verify the inbound email was edited.
		inbound1.verify(newData);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}