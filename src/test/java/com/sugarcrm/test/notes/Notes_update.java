package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;

public class Notes_update extends SugarTest {
	NoteRecord myNote;

	public void setup() throws Exception {
		myNote = (NoteRecord)sugar().notes.api.create();
		sugar().login();
	}

	@Test
	public void Notes_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("subject", "New Modified Note");
		newData.put("description", "In Progress");

		// Edit the note using the UI.
		myNote.edit(newData);

		// Verify the note was edited.
		myNote.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}