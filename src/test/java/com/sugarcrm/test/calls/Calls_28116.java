package com.sugarcrm.test.calls;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Calls_28116 extends SugarTest {
	FieldSet massUpdateValue;

	public void setup() throws Exception {
		massUpdateValue = testData.get(testName).get(0);
		for (int i = 0 ; i <= 3 ; i++){
			sugar.calls.api.create();
		}
		sugar.login();
	}

	/**
	 * Verify that Mass update on "assigned user" field  works for calls/meetings module
	 * @throws Exception
	 */
	@Test
	public void Calls_28116_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.toggleSelectAll();
		// Perform Mass Update
		sugar.calls.massUpdate.performMassUpdate(massUpdateValue);

		// Navigate to Calls list View
		sugar.calls.navToListView();

		// Verify the assigned user in the updated records
		for (int i = 1 ; i <= 4 ; i++ ){
			sugar.calls.listView.verifyField(i, "assignedTo", massUpdateValue.get("Assigned to"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}