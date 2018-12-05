package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuoteRecord;

import static org.junit.Assert.assertEquals;

public class Quotes_delete extends SugarTest {
	QuoteRecord myQuote;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		myQuote = (QuoteRecord)sugar().quotes.create();
	}

	@Test
	public void Quotes_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the quote using the UI.
		myQuote.delete();

		// Verify the quote was deleted.
		assertEquals(VoodooUtils.contains(myQuote.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}