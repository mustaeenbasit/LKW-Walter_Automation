package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;

public class Bugs_18576 extends SugarTest {
	FieldSet bugRecord;
	
	public void setup() throws Exception {   	
		bugRecord = testData.get("Bugs_18576").get(0);

		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		sugar.bugs.api.create();
	}

	/**
	 * Create Bug: Verify that the DetailView of duplicated bug is displayed after saving.
	 */
	@Test
	public void Bugs_18576_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Bugs record view and copy a bug record
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);
		sugar.bugs.recordView.copy();
		sugar.bugs.recordView.getEditField("name").set(bugRecord.get("name"));
		sugar.bugs.createDrawer.save();
		VoodooUtils.pause(4000); // this 4 sec. wait is necessary for the cop operation to finish 
		VoodooUtils.waitForAlertExpiration(); // this wait is necessary for the great success method to go away in case of copy record.
		
		// Verify the detail of the duplicated bug is displayed.
		sugar.bugs.recordView.getDetailField("name").assertContains(bugRecord.get("name"), true);
		         
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}