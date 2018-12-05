package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_26518 extends SugarTest {
	DataSource myCase;

	public void setup() throws Exception {
		myCase = testData.get("Cases_26518");

		sugar().login();
		sugar().cases.api.create(myCase);
	}

	/**
	 * Verify cases search filter works with several words with a space
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_26518_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases list view
		sugar().cases.navToListView();

		// Search on the specific filter
		sugar().cases.listView.getControl("searchFilter").set(myCase.get(0).get("name").substring(0, 7));

		// Pause is required in this case, otherwise test will fail
		VoodooUtils.pause(1000);

		// Verify the case that match search conditions are displayed in case list view.
		sugar().cases.listView.verifyField(1, "name", myCase.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
