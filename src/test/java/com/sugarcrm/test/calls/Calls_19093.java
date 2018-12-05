package com.sugarcrm.test.calls;

import junit.framework.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Calls_19093 extends SugarTest {
	NoteRecord myNote;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		sugar.calls.api.create();
		myNote = (NoteRecord)sugar.notes.api.create();
		sugar.login();
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);

		// Notes subpanel
		notesSubpanel = sugar.calls.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.linkExistingRecord(myNote);
	}

	/**
	 * Remove Note_Verify that note can be removed from "Notes" sub-panel when using "rem" function.
	 * @throws Exception
	 */
	@Test
	public void Calls_19093_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Unlink and verify no record in Notes subpanel
		notesSubpanel.unlinkRecord(1);
		int row = notesSubpanel.countRows();
		Assert.assertTrue("Number of rows did not equal zero.", row == 0);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}