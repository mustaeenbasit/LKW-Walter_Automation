package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21070 extends SugarTest {
	DataSource callData = new DataSource();

	public void setup() throws Exception {
		callData = testData.get(testName);

		// Create Call records
		sugar().calls.api.create(callData);

		// Login
		sugar().login();
	}

	/**
	 * Search call_Verify that searching with a large number of calls works correctly
	 * @throws Exception
	 */
	@Test
	public void Calls_21070_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to 'Calls' module
		sugar().calls.navToListView();

		for(int i = 0; i < callData.size(); i++) {
			// Try to search a call with a specific subject
			sugar().calls.listView.setSearchString(callData.get(i).get("name"));
			VoodooUtils.waitForReady();

			// Verify that the match scheduled calls are displayed in the "Call List" view
			sugar().calls.listView.getDetailField(1, "name").assertEquals(callData.get(i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}