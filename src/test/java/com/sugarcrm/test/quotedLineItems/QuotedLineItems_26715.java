package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_26715 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that the only required fields to create Quoted Line Items is a name
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_26715_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the QLI and create a QLI record
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.create();
		sugar().quotedLineItems.createDrawer.getEditField("name").set(sugar().quotedLineItems.getDefaultData().get("name"));

		// Save the record and click on the record from list view to open the record view
		sugar().quotedLineItems.createDrawer.save();
		sugar().quotedLineItems.listView.clickRecord(1);

		// Verify that required field i.e name
		sugar().quotedLineItems.recordView.getDetailField("name").assertEquals(sugar().quotedLineItems.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}