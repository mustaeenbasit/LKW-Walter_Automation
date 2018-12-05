package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_26985 extends SugarTest {	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Cancel button works correctly in Meeting create form
	 * @throws Exception
	 */
	@Test
	public void Meetings_26985_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Meeting with required field only and cancel
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.cancel();

		// Verify no meeting record is saved
		sugar().meetings.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}