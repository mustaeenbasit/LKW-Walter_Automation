package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;

public class Notes_create extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Notes_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		NoteRecord myNote = (NoteRecord)sugar().notes.create();
		myNote.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}