package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuotedLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_21498 extends SugarTest {
	QuotedLineItemRecord myQLI;

	public void setup() throws Exception {
		myQLI = (QuotedLineItemRecord)sugar().quotedLineItems.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * New action dropdown list in Quoted Line Item detail view page
	 *
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_21498_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myQLI.navToRecord();
		sugar().quotedLineItems.recordView.openPrimaryButtonDropdown();
		// Verify all of the actions in the dropdown list: Edit, Share, Download PDF, Email PDF, Find Duplicate, Copy, View Change Log, Delete.
		sugar().quotedLineItems.recordView.getControl("editButton").assertVisible(true);
		// TODO: VOOD-738, 695
		// Share
		new VoodooControl("a", "css", ".fld_share.detail a").assertVisible(true);
		// Find Duplicates
		new VoodooControl("a", "css", ".fld_find_duplicates_button.detail a").assertVisible(true);
		// View Change Log
		new VoodooControl("a", "css", ".fld_audit_button.detail a").assertVisible(true);
		// Delete
		sugar().quotedLineItems.recordView.getControl("deleteButton").assertVisible(true);
		// Copy
		sugar().quotedLineItems.recordView.getControl("copyButton").assertVisible(true);

		// Click "Delete"
		sugar().quotedLineItems.recordView.getControl("deleteButton").click();
		sugar().alerts.getWarning().confirmAlert();
		// Verify action is triggered
		sugar().quotedLineItems.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}