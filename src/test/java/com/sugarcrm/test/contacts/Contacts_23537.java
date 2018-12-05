package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23537 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().contacts.create();
	}

	/**
	 * Test Case 23537: Create note_Verify that a related note can be created from contact detail view.
	 */
	@Test
	public void Contacts_23537_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts record view
		sugar().contacts.listView.clickRecord(1);

		// Click "Create Note" button in "Notes" sub-panel.
		StandardSubpanel subNotes = sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		subNotes.addRecord();

		// Enter all the required fields and click save button
		sugar().notes.createDrawer.getEditField("subject").set(testName);
		sugar().notes.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Verify the note related to the case has been created
		subNotes.getDetailField(1, "subject").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
