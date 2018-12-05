package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Meetings_19091 extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Create Note_Verify that note for call/meeting is not created in "Notes" sub-panel when using "Cancel" function.  
	 * @throws Exception
	 */
	@Test
	public void Meetings_19091_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);

		// Notes subpanel
		StandardSubpanel notesSubpanel = sugar().meetings.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.addRecord();
		sugar().notes.createDrawer.cancel();
		
		// Verify no notes in subpanels
		Assert.assertTrue("Number of rows did not equal zero.", notesSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}