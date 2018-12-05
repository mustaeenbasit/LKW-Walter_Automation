package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.test.SugarTest;

public class Cases_23378 extends SugarTest {
	CaseRecord myCase;
	FieldSet defaultNoteData;

	public void setup() throws Exception {
		defaultNoteData = sugar().notes.getDefaultData();
		sugar().login();
		sugar().accounts.api.create();
		myCase = (CaseRecord)sugar().cases.api.create(sugar().cases.getDefaultData());
	}

	/**
	 * Test Case 23378: Create Note_Verify that note for case is not created in the Notes subpanel when using "Cancel" function.
	 */
	@Test
	public void Cases_23378_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCase.navToRecord();

		StandardSubpanel subNotes = sugar().cases.recordView.subpanels.get("Notes");
		subNotes.addRecord();

		// Put default data and Cancel
		sugar().notes.createDrawer.showMore();
		sugar().notes.createDrawer.setFields(defaultNoteData);
		sugar().notes.createDrawer.cancel();

		// but create an object with default note data
		NoteRecord noteRecord = new NoteRecord(defaultNoteData);

		// Verify the note related to the case hasn't been created
		// TODO: VOOD-908
		subNotes.assertContains(noteRecord.getRecordIdentifier(), false);

		// Verify a note hasn't been created
		sugar().notes.navToListView();
		sugar().notes.listView.setSearchString(noteRecord.getRecordIdentifier());
		sugar().notes.listView.getControl("emptyListViewMsg").waitForVisible();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}