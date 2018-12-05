package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Notes_21086 extends SugarTest {
	MeetingRecord myMeeting;	
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		myMeeting = (MeetingRecord)sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Create Note_Verify that note is created for call/meeting from "Create Note or Attachment" in Navigation shortcuts.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21086_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().notes, "createNote");
		//  Enter all the required field, like "Team", "Subject"& other text fields
		sugar().notes.createDrawer.getEditField("subject").set(sugar().notes.getDefaultData().get("subject"));
		sugar().notes.createDrawer.getEditField("relTeam").set(sugar().users.getQAUser().get("userName"));
		sugar().notes.createDrawer.getEditField("description").set(sugar().notes.getDefaultData().get("description"));
		// Select a call/meeting module from Related to field 
		sugar().notes.createDrawer.getEditField("relRelatedToModule").set(sugar().meetings.moduleNameSingular);
		// Click on "Search for more" and select any existing call/meeting.
		VoodooSelect relatedValue  = (VoodooSelect)sugar().notes.createDrawer.getEditField("relRelatedToValue");
		relatedValue.clickSearchForMore();
		sugar().meetings.searchSelect.selectRecord(1);
		sugar().notes.createDrawer.save();
		// Go to the selected call/meeting detail view page.
		myMeeting.navToRecord();
		// Verify created note record is displayed in "Note" sub-panel of "Call/Meeting Detail View" page.
		notesSubpanel = sugar().meetings.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.expandSubpanel();
		notesSubpanel.assertContains(sugar().notes.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}