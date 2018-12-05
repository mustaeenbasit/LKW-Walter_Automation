package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Subpanels_21055 extends SugarTest {

	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.login();
	}

	/**
	 * Verify that note for call can be created in "Notes" sub-panel of detail view page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_21055_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToModule(sugar.calls.moduleNamePlural);
		// Click any call subject link in "Call List" view
		sugar.calls.listView.getDetailField(1, "name").click();
		// Click "Create Note or Attachment" button in "Notes" sub-panel of "Call Detail View" page.
		StandardSubpanel notesSubpanel = sugar.calls.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		notesSubpanel.addRecord();
		// Enter all the required field, like "Team", "Subject" and text field
		sugar.notes.createDrawer.getEditField("subject").set(sugar.notes.getDefaultData().get("subject"));
		sugar.notes.createDrawer.getEditField("relTeam").set(sugar.users.getQAUser().get("userName"));
		sugar.notes.createDrawer.getEditField("description").set(sugar.notes.getDefaultData().get("description"));
		sugar.notes.createDrawer.save();
		// Verify created note for the call is displayed in "Note" sub-panel
		notesSubpanel.assertContains(sugar.notes.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}