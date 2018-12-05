package com.sugarcrm.test.notes;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_20993 extends SugarTest {
	public void setup() throws Exception {
		sugar().notes.api.create();

		// Login as a valid user
		sugar().login();
	}

	/**
	 * Duplicate Note_Verify that note can be duplicated.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_20993_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Notes" link in navigation shortcuts.
		sugar().navbar.navToModule(sugar().notes.moduleNamePlural);

		// Go to a note's detail view
		sugar().notes.listView.clickRecord(1);

		// Click on "Actions" icon near Edit button -> Click "Copy" button
		sugar().notes.recordView.copy();

		// Modify the note -> Click "Save" button
		sugar().notes.createDrawer.getEditField("subject").set(testName);
		sugar().notes.createDrawer.getEditField("description").set(testName + sugar().notes.getDefaultData().get("description"));
		sugar().notes.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// Verify that the duplicated note detail information is displayed as modified
		sugar().notes.recordView.getDetailField("subject").assertEquals(testName, true);
		sugar().notes.recordView.getDetailField("description").assertEquals(testName + sugar().notes.getDefaultData().get("description"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}