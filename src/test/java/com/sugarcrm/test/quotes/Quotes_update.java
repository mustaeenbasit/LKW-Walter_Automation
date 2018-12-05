package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.QuoteRecord;

public class Quotes_update extends SugarTest {
	QuoteRecord myQuote;

	public void setup() throws Exception {
		sugar().accounts.api.create();	
		sugar().login();	
		myQuote = (QuoteRecord)sugar().quotes.create();
	}

	@Test
	public void Quotes_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "International Business Machines, Inc.");

		// Edit the quote using the UI.
		myQuote.edit(newData);

		// Verify the quote was edited.
		myQuote.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}