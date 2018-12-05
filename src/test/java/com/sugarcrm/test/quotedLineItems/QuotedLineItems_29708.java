package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_29708 extends SugarTest {
	public void setup() throws Exception {
		sugar().quotedLineItems.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that user should be able to click 'Cancel' link while editing a QLI record
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_29708_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to quotedLine Items list view
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.clickRecord(1);

		// Update some fields on the QLI record view page.
		sugar().quotedLineItems.recordView.edit();
		sugar().quotedLineItems.recordView.getEditField("name").set(testName);
		sugar().quotedLineItems.recordView.showMore();
		sugar().quotedLineItems.recordView.getEditField("description").set(testName);

		// Cancel editing of QLI edit view to navigate back to QLI record view.
		sugar().quotedLineItems.recordView.cancel();
		sugar().quotedLineItems.recordView.assertVisible(true);

		// Verify edited data is not saved for the QLI record 
		sugar().quotedLineItems.recordView.getDetailField("name").assertEquals(sugar().quotedLineItems.getDefaultData().get("name"), true);
		sugar().quotedLineItems.recordView.showMore();
		sugar().quotedLineItems.recordView.getDetailField("description").assertEquals(sugar().quotedLineItems.getDefaultData().get("description"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}