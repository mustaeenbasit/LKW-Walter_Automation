package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21121 extends SugarTest {

	public void setup() throws Exception {
		sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * Update “Assigned to” field of Note with a valid value
	 * @throws Exception
	 */
	@Test
	public void Notes_21121_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet assignedToUser = testData.get(testName).get(0);

		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);

		// Assert that before Update, The Assigned user is Administrator
		sugar().notes.recordView.getDetailField("relAssignedTo").assertEquals(assignedToUser.get("assignedTo"), true);

		// Edit the Record
		sugar().notes.recordView.edit();

		// Update “Assigned to” field with a valid value i.e QAUser
		sugar().notes.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().notes.recordView.save();

		// Assert that the "Assigned To" field has been updated successfully to QAUser
		sugar().notes.recordView.getDetailField("relAssignedTo").assertEquals(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}