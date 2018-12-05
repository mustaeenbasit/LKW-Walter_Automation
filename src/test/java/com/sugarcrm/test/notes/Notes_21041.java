package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Notes_21041 extends SugarTest {
	public void setup() throws Exception {
		sugar().calls.api.create();
		sugar().meetings.api.create();

		// Login as Admin user
		sugar().login();
	}

	// Links Calls/Meetings record with Notes record and navigate to Call/Meeting record view
	private void linkCallsAndMeetingsRecordWithNotesRecord(String notesSubject, StandardModule sugarModule) throws Exception {
		// Go to Notes module -> Click "Create Note or Attachment" link in navigation shortcuts
		sugar().navbar.selectMenuItem(sugar().notes, "createNote");
		sugar().notes.createDrawer.showMore();

		// Enter required information
		sugar().notes.createDrawer.getEditField("subject").set(notesSubject);

		// Select call/meetings in Relate to drop down and select a call/meeting accordingly
		sugar().notes.createDrawer.getEditField("relRelatedToModule").set(sugarModule.moduleNameSingular);
		sugar().notes.createDrawer.getEditField("relRelatedToValue").set(sugarModule.getDefaultData().get("name"));

		// Click "Save" button
		sugar().notes.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// Go to the selected call/meeting detail view page
		sugarModule.navToListView();
		sugarModule.listView.clickRecord(1);
	}

	/**
	 * Create Note_Verify that note is created for call/meeting from "Create Note or Attachment" in Navigation shortcuts.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21041_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// For Calls module
		String notesName = testName + "_" + sugar().calls.moduleNamePlural;
		linkCallsAndMeetingsRecordWithNotesRecord(notesName, sugar().calls);

		// Navigate to Notes sub-panel in Calls module
		StandardSubpanel notesSubpanelCtrlInCallsModule = sugar().calls.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanelCtrlInCallsModule.expandSubpanel();

		// Verify that the created Notes record is displayed in "Note" sub-panel of "Call/Meeting Detail View" page
		notesSubpanelCtrlInCallsModule.getDetailField(1, "subject").assertEquals(notesName, true);

		// For Meetings module
		notesName = testName + "_" + sugar().meetings.moduleNamePlural;
		linkCallsAndMeetingsRecordWithNotesRecord(notesName, sugar().meetings);

		// Navigate to Notes sub-panel in Meetings module
		StandardSubpanel notesSubpanelCtrlInMeetingsModule = sugar().meetings.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanelCtrlInMeetingsModule.expandSubpanel();

		// Verify that the created Notes record is displayed in "Note" sub-panel of "Call/Meeting Detail View" page
		notesSubpanelCtrlInMeetingsModule.getDetailField(1, "subject").assertEquals(notesName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}