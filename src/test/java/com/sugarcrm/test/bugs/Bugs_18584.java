package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.test.SugarTest;

public class Bugs_18584 extends SugarTest {
	BugRecord testBug;
	
	public void setup() throws Exception {
		testBug = (BugRecord)sugar.bugs.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Detail View_Verify that the duplicate records are displayed in list view.
	 * @throws Exception
	 */
	@Test
	public void Bugs_18584_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		testBug.navToRecord();
		sugar.bugs.recordView.copy();
		sugar.bugs.createDrawer.save();
		sugar.bugs.createDrawer.getControl("ignoreDuplicateAndSaveButton").waitForVisible();
		sugar.bugs.createDrawer.ignoreDuplicateAndSave();
		
		// Verify that both records appear on the list view
		sugar.bugs.navToListView();
		sugar.bugs.listView.verifyField(1, "name", testBug.getRecordIdentifier());
		sugar.bugs.listView.verifyField(2, "name", testBug.getRecordIdentifier());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}