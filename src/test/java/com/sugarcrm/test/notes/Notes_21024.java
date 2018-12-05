package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21024 extends SugarTest {
	public void setup() throws Exception {
		// Login as a valid user
		sugar().login();
	}

	/**
	 * Duplicate Note_Verify that the attachment can be removed from the note through removing attachment on the note edit view after clicking "Duplicate" button.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21024_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a note with attachment.
		sugar().notes.navToListView();
		sugar().notes.listView.create();
		sugar().notes.createDrawer.showMore();
		VoodooControl subjectFieldCtrl = sugar().notes.createDrawer.getEditField("subject");
		subjectFieldCtrl.set(testName);
		VoodooControl attachmentFileCtrl = sugar().notes.recordView.getEditField("attachment");
		VoodooFileField browseToImport = new VoodooFileField(attachmentFileCtrl.getTag(), attachmentFileCtrl.getStrategyName(), attachmentFileCtrl.getHookString());

		// Getting the attachment file for Notes
		browseToImport.set("src/test/resources/data/" + testName + ".csv");
		VoodooUtils.waitForReady();

		// Save the Notes record
		sugar().notes.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// Go to the newly created note detail view
		sugar().notes.listView.clickRecord(1);

		// Click "Copy" button under Edit Action drop down 
		sugar().notes.recordView.copy();

		// Click "Remove" button next to the attachment on the edit view
		// TODO: VOOD-2125
		new VoodooControl("button", "css", ".fld_filename.edit .fa-times-circle").click();

		FieldSet notesData = testData.get(testName).get(0);

		// Update the name and click "Save" button
		subjectFieldCtrl.set(notesData.get("updatedSubject"));
		sugar().notes.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// Verify that the attachment is removed from the note
		sugar().notes.recordView.getDetailField("attachment").assertExists(false);
		sugar().notes.recordView.getDetailField("subject").assertEquals(notesData.get("updatedSubject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}