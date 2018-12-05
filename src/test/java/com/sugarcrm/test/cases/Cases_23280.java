package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23280 extends SugarTest {
	DataSource cases;

	public void setup() throws Exception {
		cases = testData.get("Cases_23280");

		sugar().login();
		sugar().cases.api.create(cases);
	}

	/**
	 * Verify that case can be searched by Subject by default.
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23280_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases list view
		sugar().cases.navToListView();
		sugar().cases.listView.getControl("searchFilter").set(
				cases.get(1).get("name"));
		// Pause is required in this case, otherwise test will fail
		VoodooUtils.pause(5000);

		// Verify the case that match search conditions are displayed in case
		// list view.
		sugar().cases.listView.verifyField(1, "name", cases.get(1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
