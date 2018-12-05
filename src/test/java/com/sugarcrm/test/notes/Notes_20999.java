package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_20999 extends SugarTest {
	public void setup() throws Exception {
		sugar().notes.api.create();

		// Login as a valid user
		sugar().login();
	}

	/**
	 * Mass Update Note_Verify that note can be deleted by "Mass Update" function.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_20999_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Notes" link in navigation shortcuts.
		sugar().navbar.navToModule(sugar().notes.moduleNamePlural);

		// Select one or more notes by checking the check box in front of each note record.
		sugar().notes.listView.checkRecord(1);

		// Click "Delete" link of Actions menu
		sugar().notes.listView.openActionDropdown();
		sugar().notes.listView.delete();

		// Click 'Confirm' in warning popup
		sugar().alerts.getWarning().confirmAlert();

		// Verify that the selected notes are not displayed in the list
		sugar().notes.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}