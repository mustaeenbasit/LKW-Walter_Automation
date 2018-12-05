package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23383 extends SugarTest {
	CaseRecord myCase;
	NoteRecord relNote;
	StandardSubpanel subNotes;

	public void setup() throws Exception {
		relNote = (NoteRecord)sugar().notes.api.create();
		myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();

		// Link a note to a case
		myCase.navToRecord();
		subNotes = sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		subNotes.linkExistingRecord(relNote);
	}

	/**
	 * Edit Note_Verify that modification of note for case can be canceled in "Notes" detail view.
	 * @throws Exception
	 */
	@Test
	public void Cases_23383_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet noteSubjData = new FieldSet();
		noteSubjData.put("subject", sugar().notes.getDefaultData().get("subject"));

		// Verify that the note is linked correctly, click subject link
		subNotes.scrollIntoView();
		subNotes.verify(1, noteSubjData, true);
		subNotes.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// Click "Edit" button & cancel
		sugar().notes.recordView.edit();
		sugar().notes.recordView.showMore();
		sugar().notes.recordView.setFields(testData.get(testName).get(0));
		sugar().notes.recordView.cancel();

		// The information of the note for the selected case is not changed
		myCase.navToRecord();
		subNotes.verify(1, noteSubjData, true);
		relNote.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
