package com.sugarcrm.test.RevenueLineItems;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_delete extends SugarTest {
	RevLineItemRecord myRLI;

	public void setup() throws Exception {
		myRLI = (RevLineItemRecord)sugar().revLineItems.api.create();
		sugar().login();
	}

	@Test
	public void RevenueLineItems_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the revenue line item record using the UI.
		myRLI.delete();

		// Verify the revenue line item record was deleted.
		assertEquals(VoodooUtils.contains(myRLI.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 