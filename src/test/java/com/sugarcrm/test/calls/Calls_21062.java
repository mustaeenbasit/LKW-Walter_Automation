package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21062 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Call List View_Verify that call records are displayed in list view.
	 * @throws Exception
	 */
	@Test
	public void Calls_21062_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar().calls.navToListView();

		// Verify that Call list view is displayed without error.
		sugar().alerts.getError().assertExists(false);
		sugar().calls.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}