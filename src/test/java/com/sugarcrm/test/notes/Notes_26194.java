package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;

public class Notes_26194 extends SugarTest {
	NoteRecord myNote;

	public void setup() throws Exception {
		myNote = (NoteRecord)sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * Verify Navigation and display of the Note Preview pane
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_26194_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet myTestData = testData.get(testName).get(0);
		sugar().notes.navToListView();
		sugar().notes.listView.previewRecord(1);
		myNote.verifyPreview(myTestData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}