package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Cases_23382 extends SugarTest {
	StandardSubpanel subNotes;

	public void setup() throws Exception {
		NoteRecord relNote = (NoteRecord)sugar().notes.api.create();
		CaseRecord myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();

		// Link a note to a case
		myCase.navToRecord();
		subNotes = sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		subNotes.linkExistingRecord(relNote);
	}

	/**
	 * Edit Note_Verify that note detail view is displayed after clicking the subject of a note in notes sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Cases_23382_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet noteSubjData = new FieldSet();
		noteSubjData.put("subject", sugar().notes.getDefaultData().get("subject"));

		// Verify that the note is linked correctly
		subNotes.verify(1, noteSubjData, true);

		// and click subject link
		subNotes.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify RecordView of the note is rendered
		sugar().notes.recordView.getDetailField("subject").assertVisible(true);
		sugar().notes.recordView.getDetailField("subject").assertEquals(noteSubjData.get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
