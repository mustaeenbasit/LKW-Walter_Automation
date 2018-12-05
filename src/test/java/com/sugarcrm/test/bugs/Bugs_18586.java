package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;

public class Bugs_18586 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		sugar.bugs.api.create();
	}

	/**
	 * Detail View_Verify that the editing record is canceled with the page
	 * transferring to its detail view.
	 */
	@Test
	public void Bugs_18586_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs record view
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);

		// Click on "Edit" button
		sugar.bugs.recordView.edit();

		// Click on "Cancel" button
		sugar.bugs.recordView.cancel();

		// Verify back to detail view by verify edit button exist
		sugar.bugs.recordView.getControl("editButton").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}