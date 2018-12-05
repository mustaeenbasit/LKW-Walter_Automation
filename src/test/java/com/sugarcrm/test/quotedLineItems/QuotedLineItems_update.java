package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.QuotedLineItemRecord;

public class QuotedLineItems_update extends SugarTest {
	QuotedLineItemRecord myQLI;

	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
		sugar().accounts.api.create();
		myQLI = (QuotedLineItemRecord) sugar().quotedLineItems.api.create();
	}

	@Test
	public void QuotedLineItems_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Edited Quoted Line Item");

		// Edit the quotedLineItem using the UI.
		myQLI.edit(newData);

		// Verify the quotedLineItem was edited.
		myQLI.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}