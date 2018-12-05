package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.test.SugarTest;

public class Bugs_18585 extends SugarTest {
	BugRecord testBug;	
	
	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		testBug = (BugRecord)sugar.bugs.api.create();		
	}

	/**
	 * Test Case 18585: Detail View_Verify that duplicate records of the selected record are canceled from the list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18585_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		testBug.navToRecord();
		sugar.bugs.recordView.copy();		
		sugar.bugs.createDrawer.cancel();
		testBug.delete();
		sugar.bugs.navToListView();
		sugar.bugs.listView.getControl("emptyListViewMsg").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}