package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_19759 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Check that Meeting could be created from quick create (+)
	 * @throws Exception
	 */
	@Test
	public void Meetings_19759_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open QuickCreate and click "Create Meeting"
		sugar().navbar.quickCreateAction(sugar().meetings.moduleNamePlural);
		
		// Verify cancel, save button on drawer and fill required fields
		sugar().meetings.createDrawer.getControl("saveButton").assertVisible(true);
		sugar().meetings.createDrawer.getControl("cancelButton").assertVisible(true);
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.save();

		// Verify meeting record is saved and display in listview
		sugar().meetings.navToListView();
		sugar().meetings.listView.verifyField(1, "name", testName);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}