package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_19091 extends SugarTest {
	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * Create Note_Verify that note for call is not created in "Notes" sub-panel when using "Cancel" function.
	 * @throws Exception
	 */
	@Test
	public void Calls_19091_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);

		// Notes subpanel
		StandardSubpanel notesSubpanel = sugar.calls.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		notesSubpanel.addRecord();
		sugar.notes.createDrawer.cancel();

		// Verify no note record in Notes subpanel
		Assert.assertTrue("Number of rows did not equal zero.", notesSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}