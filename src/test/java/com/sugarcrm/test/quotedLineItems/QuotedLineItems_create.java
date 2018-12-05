package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuotedLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
		sugar().accounts.api.create();
	}

	@Test
	public void QuotedLineItems_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		QuotedLineItemRecord myQLI = (QuotedLineItemRecord)sugar().quotedLineItems.create();
		myQLI.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
