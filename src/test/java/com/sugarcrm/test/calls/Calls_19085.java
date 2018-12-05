package com.sugarcrm.test.calls;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Calls_19085 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.calls.api.create(ds);
		sugar.login();
	}

	/**
	 * Search call_Verify that searching calls with special characters works correctly.
	 * @throws Exception
	 */
	@Test
	public void Calls_19085_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();

		for (int i = 0;  i < ds.size(); i++){
			String callName = ds.get(i).get("name");
			// Search the call
			sugar.calls.listView.setSearchString(callName);

			// Verify call's name
			sugar.calls.listView.verifyField(1, "name", callName);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}