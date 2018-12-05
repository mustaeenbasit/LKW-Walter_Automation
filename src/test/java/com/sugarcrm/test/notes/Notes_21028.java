package com.sugarcrm.test.notes;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_21028 extends SugarTest {
	public void setup() throws Exception {
		sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * Duplicate Note_Verify that duplicating a note can be canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_21028_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to Note's record view
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		
		// Click 'Copy' button
		sugar().notes.recordView.copy();
		
		// Modify the note and Click 'Cancel' button
		sugar().notes.createDrawer.getEditField("subject").set(testName);
		sugar().notes.createDrawer.cancel();
		
		// Verify that note detail view is displayed as current page
		sugar().notes.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(sugar().notes.moduleNameSingular, true);
		sugar().notes.recordView.getDetailField("subject").assertEquals(sugar().notes.getDefaultData().get("subject"), true);
		
		// Go to Notes list view and verify that canceled duplicating note is not displayed in the list view
		sugar().notes.navToListView();
		Assert.assertTrue("Record count in Notes Listview is not equal to 1", sugar().notes.listView.countRows() == 1);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}