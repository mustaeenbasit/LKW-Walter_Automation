package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_19092 extends SugarTest {
	NoteRecord myNote;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		sugar().meetings.api.create();
		myNote = (NoteRecord)sugar().notes.api.create();
		sugar().login();
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);

		// Notes subpanel
		notesSubpanel = sugar().meetings.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.linkExistingRecord(myNote);
	}

	/**
	 * Edit Note_Verify that the information of a note for meeting can be modified when using "Edit" function in "Notes" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Meetings_19092_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit and verify record in Notes subpanel
		FieldSet editData = new FieldSet();
		editData.put("subject", testName);
		notesSubpanel.editRecord(1, editData);
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.closeAllSuccess();

		notesSubpanel.verify(1, editData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}