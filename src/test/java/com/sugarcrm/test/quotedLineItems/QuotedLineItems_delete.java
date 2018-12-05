package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuotedLineItemRecord;
import static org.junit.Assert.assertEquals;

public class QuotedLineItems_delete extends SugarTest {
	QuotedLineItemRecord myQLI;

	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
		sugar().accounts.api.create();
		myQLI = (QuotedLineItemRecord)sugar().quotedLineItems.api.create();
	}

	@Test
	public void QuotedLineItems_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the quotedLineItem using the UI.
		myQLI.delete();

		// Verify the quoteLineItem was deleted.
		sugar().quotedLineItems.navToListView();
		assertEquals(VoodooUtils.contains(myQLI.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
