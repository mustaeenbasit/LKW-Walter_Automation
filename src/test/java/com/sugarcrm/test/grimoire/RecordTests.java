package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class RecordTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void setGuidTest() throws Exception { // also covers getGuid()
		VoodooUtils.voodoo.log.info("Running setGuidTest()...");

		String guid = "test";

		AccountRecord myAccount = (AccountRecord)sugar().accounts.api.create();
		myAccount.setGuid(guid);

		myAccount.verify(); // Make sure setting the GUID didn't alter the record.
		assertEquals("getGuid() did not return the value set by setGuid()!",
				myAccount.getGuid(), guid);

		VoodooUtils.voodoo.log.info("setGuidTest() complete.");
	}

	public void cleanup() throws Exception {}
}