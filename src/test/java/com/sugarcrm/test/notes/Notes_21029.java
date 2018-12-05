package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21029 extends SugarTest {
	public void setup() throws Exception {
		// Note record(s) exist.
		sugar().notes.api.create();

		// Login as Admin user
		sugar().login();
	}

	/**
	 * Delete Note_Verify that deleting a note from detail view can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21029_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Notes module -> Go to a note's detail view
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);

		// Click "Delete" button
		sugar().notes.recordView.delete();

		// Click "Cancel" button in pop-up dialog box
		sugar().alerts.getWarning().cancelAlert();

		// Verify that the note detail view is still displayed as current page
		// TODO: VOOD-1887 - Uncomment the line written below after this VOOD will resolved
		// sugar().notes.recordView.assertVisible(true);
		sugar().notes.recordView.getDetailField("subject").assertEquals(sugar().notes.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}