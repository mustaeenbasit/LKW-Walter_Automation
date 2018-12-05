package com.sugarcrm.test.documents;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Documents_29124 extends SugarTest {
		
	public void setup() throws Exception {
		sugar().documents.api.create();
		sugar().login();
	}

	/**
	 * Verify that In All BWC modules, After deleting a record, A deletion related massage is appearing
	 * @throws Exception
	 */
	@Test
	public void Documents_29124_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().documents.navToListView();
		sugar().documents.listView.deleteRecord(1);
		sugar().documents.listView.confirmDelete();
		VoodooUtils.waitForReady();
		FieldSet customFS = testData.get(testName).get(0);
		
		// Verify that a deletion related massage "1 record(s) were deleted successfully." should be displayed instead of "All records were updated successfully." 
		new VoodooControl("span", "css", "span.error").assertContains(customFS.get("successMsg"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}