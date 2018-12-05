package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Notes_21103 extends SugarTest {
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().calls.api.create(fs);
		sugar().login();
	}

	/**
	 * Create Note_Verify that note is created for call/meeting from "Create Note or Attachment" in Navigation shortcuts.
	 * @throws Exception
	 */
	@Test
	public void Notes_21103_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click Create Note or Attachment through Navigation Bar
		sugar().navbar.selectMenuItem(sugar().notes, "createNote");
		sugar().notes.createDrawer.getEditField("subject").set(testName);

		// Add an Attachment
		VoodooFileField chooseFile = new VoodooFileField("input", "css", "[name='filename']");
		chooseFile.set("src/test/resources/data/" + testName + ".csv");
		VoodooUtils.waitForReady();

		// Add Related Module as Calls 
		sugar().notes.createDrawer.getEditField("relRelatedToModule").set(sugar().calls.moduleNameSingular);

		// Select Call Record 
		sugar().notes.createDrawer.getEditField("relRelatedToValue").set(sugar().calls.getDefaultData().get("name"));
		sugar().notes.createDrawer.save();

		// Navigate to Related Call Record's Record view
		sugar().notes.listView.getDetailField(1, "relRelatedToValue").click();

		// Verify the created note record is displayed in "Note" sub-panel of Call Record View page.
		StandardSubpanel notesSubpanel = sugar().calls.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.expandSubpanel();
		notesSubpanel.getDetailField(1, "subject").assertEquals(testName, true);

		// Click Edit on the Call Record
		sugar().calls.recordView.edit();

		// Verify Status is not changed after Edit
		sugar().calls.recordView.getEditField("status").assertEquals(fs.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}