package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;

public class Notes_20991 extends SugarTest {
	NoteRecord myNoteRecord;
	
	public void setup() throws Exception {
		myNoteRecord = (NoteRecord)sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * Edit Note_Verify that note can be edited from detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_20991_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to note's detail view.
		myNoteRecord.navToRecord();
		
		// Click "Edit" button.
		sugar().notes.recordView.edit();
		
		// Modify the note.
		sugar().notes.recordView.getEditField("subject").set(testName);
		
		//  Click "Save" button.
		sugar().notes.recordView.save();
		
		// Verify that the note detail information is displayed as modified.
		sugar().notes.recordView.getDetailField("subject").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}