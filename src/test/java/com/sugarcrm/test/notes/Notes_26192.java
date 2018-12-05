package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.NoteRecord;

public class Notes_26192 extends SugarTest {
	FieldSet myTestData;
	NoteRecord myNote;

	public void setup() throws Exception {
		myTestData = testData.get("Notes_26192").get(0);
		sugar().login();
		myNote = (NoteRecord) sugar().notes.api.create();
		sugar().accounts.api.create();
		sugar().contacts.api.create();
	}

	/**
	 * Verify that the in line edit works for Note Items List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_26192_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().notes.navToListView();
		// Inline Edit the record and save
		// TODO VOOD-662 Create a Lib Function to move SugarCRM scroll bars
		sugar().notes.listView.updateRecord(1, myTestData);
		// Verify Record updated
		sugar().notes.listView.verifyField(1, "subject",
				myTestData.get("subject"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
