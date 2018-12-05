package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.QuoteRecord;
import com.sugarcrm.test.SugarTest;

public class Quotes_create extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	@Test
	public void Quotes_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		QuoteRecord myQuote = (QuoteRecord)sugar().quotes.create();
		myQuote.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}