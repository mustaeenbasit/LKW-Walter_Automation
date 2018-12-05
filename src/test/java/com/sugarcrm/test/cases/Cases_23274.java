package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;

public class Cases_23274 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify that case can be deleted when using "Delete" function in "Case"
	 * detail view.
	 */
	@Test
	public void Cases_23274_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Case record view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Delete record
		sugar().cases.recordView.delete();
		sugar().alerts.getAlert().confirmAlert();

		// Verify case is deleted
		sugar().cases.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
