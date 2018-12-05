package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;

public class Notes_20992 extends SugarTest {
	NoteRecord myNoteRecord;
	
	public void setup() throws Exception {
		myNoteRecord = (NoteRecord)sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * Edit Note_Verify that editing a note from detail view can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_20992_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to note's detail view.
		myNoteRecord.navToRecord();
		
		// Click "Edit" button.
		sugar().notes.recordView.edit();
		
		// Modify the note.
		sugar().notes.recordView.getEditField("subject").set(testName);
		
		//  Click "Cancel" button.
		sugar().notes.recordView.cancel();
		
		// Verify that the note detail information is displayed as original.
		sugar().notes.recordView.getDetailField("subject").assertEquals(sugar().notes.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}