package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.test.SugarTest;

public class Notes_21164 extends SugarTest {
	NoteRecord noteRecord;

	public void setup() throws Exception {
		noteRecord  = (NoteRecord) sugar().notes.api.create();
		sugar().login();
	}
	/**
	 * Verify (all) actions (and execute any 1 of the action) in action dropdown list on notes record view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21164_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		noteRecord.navToRecord();

		// Asserting the action Dropdown & list options in action dropdown on Notes record view
		VoodooControl actionDropDown =sugar().notes.recordView.getControl("actionDropDown");
		actionDropDown.click();
		sugar().notes.recordView.getControl("deleteButton").assertVisible(true);
		sugar().notes.recordView.getControl("copyButton").assertVisible(true);

		// TODO: VOOD-738 - Verify remaining options in the action list
		FieldSet myTestData = testData.get(testName).get(0);
		new VoodooControl("a","css",".fld_share a").assertEquals(myTestData.get("share"), true);

		// Trigger the delete action
		sugar().notes.recordView.openPrimaryButtonDropdown();
		sugar().notes.recordView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Verify the notes record is deleted
		sugar().notes.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}