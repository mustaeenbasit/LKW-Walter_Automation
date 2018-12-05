package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23451 extends SugarTest {
	public void setup() throws Exception {
		FieldSet caseName = new FieldSet();

		// Create 5 Cases (The Case record created last is shown on the top of the list)
		for(int i = 5; i > 0; i--) {
			caseName.put("name", testName + "_" + i);
			sugar().cases.api.create(caseName);
		}

		// Login to sugar
		sugar().login();
	}

	/**
	 * Verify Cases list view is sorted by "Date Created" by default.
	 * @throws Exception
	 */
	@Test
	public void Cases_23451_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases list view.
		sugar().cases.navToListView();

		// "Date Created" is in list view by default.
		sugar().cases.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(true);

		// "Date Created" is the farthest to the right in list view.
		sugar().cases.listView.toggleHeaderColumn("date_entered");
		sugar().cases.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(false);

		// Reset default header settings
		sugar().cases.listView.toggleHeaderColumn("date_entered");
		sugar().cases.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(true);

		// List view is sorted by "Date Created" by default, in descending order.
		for(int i = 1; i <= 5; i++) {
			sugar().cases.listView.verifyField(i, "name", testName + "_" + i);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
