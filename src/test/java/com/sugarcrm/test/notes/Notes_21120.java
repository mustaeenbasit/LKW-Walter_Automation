package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21120 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Create a Note with Assigned to field blank
	 * @throws Exception
	 */
	@Test
	public void Notes_21120_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Note Module and Create a new Note.
		sugar().navbar.navToModule(sugar().notes.moduleNamePlural);
		sugar().notes.listView.create();
		sugar().notes.createDrawer.getEditField("subject").set(sugar().notes.getDefaultData().get("subject"));

		// TODO: VOOD-806 - Clear VoodooSelect value
		// Clearing the Assigned To field
		new VoodooControl("abbr", "css", ".fld_assigned_user_name.edit .select2-search-choice-close").click();

		// Save the record view
		sugar().notes.createDrawer.save();

		// Assert that no error when a record is saved without data in the "Assigned to" field
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().alerts.getError().assertVisible(false);

		// Click on the created record to open in recordview
		sugar().notes.listView.clickRecord(1);

		// Assert that the record does not has a User Assigned
		sugar().notes.recordView.getDetailField("relAssignedTo").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}