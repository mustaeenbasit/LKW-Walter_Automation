package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuotedLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Subpanels_17346 extends SugarTest {
	QuotedLineItemRecord qliRecord;

	public void setup() throws Exception {
		qliRecord = (QuotedLineItemRecord)sugar.quotedLineItems.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.quotedLineItems);
	}

	/**
	 * Verify default subpanels for module - Quoted Line Items
	 *
	 * @throws Exception
	 */
	@Test
	public void Subpanels_17346_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		qliRecord.navToRecord();

		sugar.quotedLineItems.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertVisible(true);
		sugar.quotedLineItems.recordView.subpanels.get(sugar().notes.moduleNamePlural).assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}