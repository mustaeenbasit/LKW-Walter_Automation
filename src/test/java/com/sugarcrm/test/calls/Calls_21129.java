package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21129 extends SugarTest {
	public void setup() throws Exception {
		FieldSet callName = new FieldSet();

		// Create 5 calls (The Call record created last is shown on the top of the list)
		for(int i = 5; i > 0; i--) {
			callName.put("name", testName + "_" + i);
			sugar.calls.api.create(callName);
		}

		// Login to sugar
		sugar.login();
	}

	/**
	 * Verify Calls list view is sorted by "Date Created" by default.
	 * @throws Exception
	 */
	@Test
	public void Calls_21129_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Calls list view.
		sugar.calls.navToListView();

		//  "Date Created" is in list view by default.
		sugar.calls.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(true);

		// "Date Created" is the farthest to the right in list view.
		sugar.calls.listView.toggleHeaderColumn("date_entered");
		sugar.calls.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(false);

		// Reset default header settings
		sugar.calls.listView.toggleHeaderColumn("date_entered");
		sugar.calls.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(true);

		// List view is sorted by "Date Created" by default, in descending order.
		for(int i = 1; i <= 5; i++) {
			sugar.calls.listView.verifyField(i, "name", testName + "_" + i);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}