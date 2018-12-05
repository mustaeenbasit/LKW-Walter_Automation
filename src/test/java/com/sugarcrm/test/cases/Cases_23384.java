package com.sugarcrm.test.cases;

import org.junit.Assert;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23384 extends SugarTest {
	NoteRecord relNote;
	StandardSubpanel subNotes;

	public void setup() throws Exception {
		CaseRecord myCase = (CaseRecord)sugar().cases.api.create(sugar().cases.getDefaultData());
		relNote = (NoteRecord)sugar().notes.api.create();
		sugar().login();

		// Link a note to a case
		myCase.navToRecord();
		subNotes = sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		subNotes.linkExistingRecord(relNote);
	}

	/**
	 * Case Note_Verify that note can be unlinked form case
	 * @throws Exception
	 */
	@Test
	public void Cases_23384_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1424 - Once resolved, below line should replaced with verify method
		subNotes.getDetailField(1, "subject").assertEquals(relNote.get("subject"), true);

		// Unlink a note and verify
		subNotes.unlinkRecord(1);
		Assert.assertTrue("The subpanel is not empty", subNotes.isEmpty());

		// Verify The Note itself is still available in Notes module
		relNote.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
