package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.NoteRecord;

public class Notes_26193 extends SugarTest {
	FieldSet myTestData;
	NoteRecord myNote;

	public void setup() throws Exception {
		myTestData = testData.get("Notes_26193").get(0);
		sugar().login();
		myNote = (NoteRecord) sugar().notes.api.create();
		sugar().contacts.api.create();
	}

	/**
	 * Verify that the in line Cancel edit works for Note Items List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_26193_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().notes.navToListView();

		// Inline edit the record and cancel
		sugar().notes.listView.editRecord(1);
		sugar().notes.listView.setEditFields(1, myTestData);
		
		// Cancel editing 
		sugar().notes.listView.cancelRecord(1);
		
		// Assert the original default subject is still present
		sugar().notes.listView.verifyField(1, "subject",myNote.get("subject"));
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
