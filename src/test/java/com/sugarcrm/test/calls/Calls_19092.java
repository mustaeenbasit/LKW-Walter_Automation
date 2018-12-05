package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Calls_19092 extends SugarTest {
	NoteRecord myNote;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		sugar.calls.api.create();
		myNote = (NoteRecord)sugar.notes.api.create();
		sugar.login();
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);

		// Notes subpanel
		notesSubpanel = sugar.calls.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.linkExistingRecord(myNote);
	}

	/**
	 * Edit Note_Verify that the information of a note for call can be modified when using "Edit" function in "Notes" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Calls_19092_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit and verify record in Notes subpanel
		FieldSet editData = new FieldSet();
		editData.put("subject", testName);
		notesSubpanel.editRecord(1, editData);
		if(sugar.alerts.getSuccess().queryVisible())
			sugar.alerts.closeAllSuccess();

		notesSubpanel.verify(1, editData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}