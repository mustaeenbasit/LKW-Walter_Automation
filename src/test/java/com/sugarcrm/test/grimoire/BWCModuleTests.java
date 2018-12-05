package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class BWCModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void createTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running createTest()...");

		sugar().accounts.api.create();
		QuoteRecord myQuote = (QuoteRecord)sugar().quotes.create();
		myQuote.verify();
		assertFalse(myQuote.getGuid().isEmpty());

		VoodooUtils.voodoo.log.info("createTest() test complete.");
	}

	public void cleanup() throws Exception {}
}