package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_20989 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that creating a note can be canceled
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_20989_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().notes.navToListView();
		sugar().notes.listView.create();
		sugar().notes.createDrawer.getEditField("subject").set(testName);
		sugar().notes.createDrawer.cancel();

		// Verify that the record not saved
		sugar().notes.listView.assertIsEmpty();
			
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}