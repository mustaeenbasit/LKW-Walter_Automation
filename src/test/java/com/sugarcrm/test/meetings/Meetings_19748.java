package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Meetings_19748 extends SugarTest {	
	StandardSubpanel notesSubpanel; 
	public void setup() throws Exception {
		NoteRecord myNote = (NoteRecord) sugar().notes.api.create();
		MeetingRecord myMeeting = (MeetingRecord) sugar().meetings.api.create();
		sugar().login();
		
		// Relate meeting with note		
		myMeeting.navToRecord();
		notesSubpanel = sugar().meetings.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.linkExistingRecord(myNote);
	}

	/**
	 * Edit Note_Verify that modification of note for meeting can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_19748_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Edit Note's subject field in Meeting sub=panel and then cancel
		notesSubpanel.expandSubpanelRowActions(1);
		notesSubpanel.getControl("editActionRow01").click();
		notesSubpanel.getEditField(1, "subject").set(testName);
		notesSubpanel.cancelAction(1);
		
		// Verify that after cancel, note is unchanged
		FieldSet notesFS = new FieldSet();
		notesFS.put("subject", sugar().notes.getDefaultData().get("subject"));
		notesSubpanel.verify(1, notesFS, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}