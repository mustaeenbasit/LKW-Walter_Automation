package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class  Meetings_27588 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that cancel a meeting create form will not save a new meeting record
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27588_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// In Meeting module, click on Schedule Meeting
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// Fill in all required fields
		sugar().meetings.createDrawer.getEditField("name").set(testName);

		// Click on Cancel
		sugar().meetings.createDrawer.cancel();

		// Verify that click cancel button close the create drawer and the meeting will not be saved
		sugar().meetings.listView.assertVisible(true);
		sugar().meetings.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");	
	}

	public void cleanup() throws Exception {}
}