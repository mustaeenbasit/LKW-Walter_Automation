package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import static org.junit.Assert.assertEquals;

public class Bugs_delete extends SugarTest {
	BugRecord myBug;

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		myBug = (BugRecord) sugar.bugs.api.create();
	}

	@Test
	public void Bugs_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.bugs.navToListView();

		// Delete the bug using the UI.
		sugar.bugs.listView.deleteRecord(1);
		sugar.bugs.listView.confirmDelete();

		// Verify the bug was deleted.
		sugar.bugs.navToListView();
		assertEquals(VoodooUtils.contains(myBug.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
