package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_28847 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enabling module QLI
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that no error message is displayed on create "Quoted Line Items" page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_28847_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Quoted Line Items" Module
		sugar().quotedLineItems.navToListView();

		// Click on Create
		sugar().quotedLineItems.listView.create();

		//  No error message should be displayed on create "Quoted Line Items" page
		sugar().alerts.getError().assertExists(false);

		// Cancel the "Quoted Line Items" Module create drawer
		sugar().quotedLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}