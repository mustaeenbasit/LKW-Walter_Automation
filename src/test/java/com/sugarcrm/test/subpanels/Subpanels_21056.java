package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_21056 extends SugarTest {
	FieldSet noteData;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		sugar.login();
		noteData = testData.get(testName).get(0);

		// Create meeting
		sugar.meetings.api.create();
	}

	/**
	 * 21056 Verify that note for meeting can be created in "Notes" sub-panel of record view page 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_21056_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create record in 'Notes' subpanel
		sugar.meetings.navToListView();
		sugar.meetings.listView.clickRecord(1);

		notesSubpanel = sugar.meetings.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		notesSubpanel.create(noteData);

		// Verify, created note for the meeting is displayed in 'Notes' sub-panel
		notesSubpanel.assertContains(noteData.get("subject"), true);
		notesSubpanel.assertContains(noteData.get("relAssignedTo"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}