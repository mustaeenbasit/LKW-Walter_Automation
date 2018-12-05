package com.sugarcrm.test.meetings;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27690 extends SugarTest {	

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that no warning message appear when navigate away from meeting create drawer.
	 *
	 * @throws Exception
	 */
	@Ignore("SC-3506 - Unsaved Changes Warning appears in Quick Create upon Navigating Away")
	@Test
	public void Meetings_27690_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Meetings module and click on create button to open create meeting drawer
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// Verify that meeting create drawer is opened
		sugar().meetings.createDrawer.getEditField("name").assertExists(true);

		// Click on Calls module
		sugar().calls.navToListView();

		// Verify that no warning message appears
		sugar().alerts.getWarning().assertVisible(false);

		// Go back to Meetings listview 
		sugar().meetings.navToListView();

		// Click on + sign to quick create, select Schedule Meeting
		sugar().navbar.quickCreateAction(sugar().meetings.moduleNamePlural);
		
		// Verify that meeting create drawer is opened
		sugar().meetings.createDrawer.getEditField("name").assertExists(true);

		// Click on Cancel in meeting create form
		sugar().meetings.createDrawer.cancel();

		// Verify that no warning message appears
		sugar().alerts.getWarning().assertVisible(false);

		// Click on quick create->Schedule Meeting again
		sugar().navbar.quickCreateAction(sugar().meetings.moduleNamePlural);

		// Verify that meeting create drawer is opened
		sugar().meetings.createDrawer.getEditField("name").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}