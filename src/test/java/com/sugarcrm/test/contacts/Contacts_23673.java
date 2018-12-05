package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23673 extends SugarTest{
	public void setup() throws Exception {
		sugar().login();
		sugar().contacts.create();
	}

	/**
	 *  Test Case 23673: Delete note or attachment_Verify that a related note or attachment
	 *  can be deleted from contact detail view.
	 *
	 *  @throws Exception
	 */
	@Test
	public void Contacts_23673_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts record view
		sugar().contacts.listView.clickRecord(1);

		// Link Notes record to the Contact
		StandardSubpanel notesSub = sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		NoteRecord noteRecord = (NoteRecord) notesSub.create(sugar().notes.getDefaultData());

		// Go to Contacts record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Select "Unlink" action in Notes sub-panel.
		notesSub.unlinkRecord(1);

		// Verify that the Note record is not displayed in "Notes" sub-panel
		Assert.assertTrue("Notes subpanel is not empty", notesSub.isEmpty());

		// Verify that the Note record is still available in Notes module
		sugar().notes.navToListView();
		sugar().notes.listView.getDetailField(1, "subject").assertEquals(noteRecord.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
