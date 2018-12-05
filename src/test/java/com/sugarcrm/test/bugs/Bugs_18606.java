package com.sugarcrm.test.bugs;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.test.SugarTest;

public class Bugs_18606 extends SugarTest {
	BugRecord bug1;

	public void setup() throws Exception {
		bug1 = (BugRecord) sugar().bugs.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
	}

	/** 
	 * Verify new action dropdown list in bug tracker detail view page
	 * @throws Exception
	 */
	@Test
	public void Bugs_18606_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to bug record view
		sugar().bugs.navToListView();
		sugar().bugs.listView.clickRecord(1);

		// Click action drop down list 
		sugar().bugs.recordView.openPrimaryButtonDropdown();

		// Verify the action list in the contacts record view
		sugar().bugs.recordView.getControl("editButton").assertVisible(true);  	// Edit 
		sugar().bugs.recordView.getControl("deleteButton").assertVisible(true);  	// Delete
		sugar().bugs.recordView.getControl("copyButton").assertVisible(true);		// Copy
		
		// TODO: Create a VOOD-691 to add ability to getControl following actions
		new VoodooControl("a","css","[name='share']").assertVisible(true); // Share
		new VoodooControl("a","css",".rowaction[name='create_button']").assertVisible(true); // Create Article
		new VoodooControl("a","css",".rowaction[name='find_duplicates_button']").assertVisible(true); //  Find Duplicates
		new VoodooControl("a","css",".rowaction[name='historical_summary_button']").assertVisible(true); // Historical Summary
		new VoodooControl("a","css",".rowaction[name='audit_button']").assertVisible(true); // View Change Log

		// Trigger the delete action
		sugar().bugs.recordView.openPrimaryButtonDropdown();
		sugar().bugs.recordView.delete();
		sugar().alerts.getAlert().confirmAlert();

		// Verify the bug record is deleted
		sugar().bugs.navToListView();
		assertEquals(VoodooUtils.contains(bug1.getRecordIdentifier(), true), false);
		sugar().bugs.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
