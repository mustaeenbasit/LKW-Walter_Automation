package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;

public class Notes_21119 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Create a Note with “Assigned to” field filled a valid value
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21119_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().notes.navToListView();
		sugar().notes.listView.create();
		// Fill “Assigned to” field with a valid value
		sugar().notes.createDrawer.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		//  Enter all the required field, like "Team", "Subject"& other text fields
		sugar().notes.createDrawer.getEditField("subject").set(sugar().notes.getDefaultData().get("subject"));
		sugar().notes.createDrawer.getEditField("relTeam").set(sugar().users.getQAUser().get("userName"));
		sugar().notes.createDrawer.getEditField("description").set(sugar().notes.getDefaultData().get("description"));
		sugar().notes.createDrawer.save();
		// Verify Save with "assigned to" field correctly
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.getDetailField("relAssignedTo").assertContains(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}