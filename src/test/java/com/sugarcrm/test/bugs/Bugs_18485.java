package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_18485 extends SugarTest {
	BugRecord myBug;

	public void setup() throws Exception {
		myBug = (BugRecord) sugar.bugs.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	@Test
	// verify the bugs Header row exists
	public void Bugs_18485_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit the bug using the UI.
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);

		// Verify the bugs label, bug name and Edit menu exist
		sugar.bugs.recordView.getDetailField("name").assertContains((myBug.get("name")), true);
		sugar.bugs.recordView.getControl("editButton").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
