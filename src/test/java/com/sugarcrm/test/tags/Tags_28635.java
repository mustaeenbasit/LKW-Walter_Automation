package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28635 extends SugarTest {

	public void setup() throws Exception {
		// Tag record exist.
		sugar().tags.api.create();
		
		// login as admin
		sugar().login();
	}

	/**
	 * Verify that cancel button works in Tag Edit View
	 * @throws Exception
	 */
	@Test
	public void Tags_28635_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(1);
		
		// Edit Tag record.
		sugar().tags.recordView.edit();

		// Change values in name and description
		sugar().tags.recordView.getEditField("name").set(testName);
		sugar().tags.recordView.getEditField("description").set(testName);
		
		// Cancel editing
		sugar().tags.recordView.cancel();

		// Verify Cancel button works - it cancels out of Edit mode.
		sugar().tags.recordView.getEditField("name").assertVisible(false);

		// Verify the values edited are not saved upon cancellation
		sugar().tags.recordView.getDetailField("name").assertEquals(testName, false);
		sugar().tags.recordView.getDetailField("description").assertEquals(testName, false);
		
		// Verify the original values in fields are still displayed
		sugar().tags.recordView.getDetailField("name").assertEquals(sugar().tags.getDefaultData().get("name"), true);
		sugar().tags.recordView.getDetailField("description").assertEquals(sugar().tags.getDefaultData().get("description"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}