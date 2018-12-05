package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;

public class Cases_24204 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().cases.api.create(customData);
		sugar().login();
	}

	/**
	 * Verify that filtering works fine regardless of the leading and trailing whitespaces presence
	 * @throws Exception
	 */
	@Test
	public void Cases_24204_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		// Enter a string that begins and ends with a whitespaces
		sugar().cases.listView.getControl("searchFilter").set(" "+customData.get(0).get("name").substring(0, 2)+" ");
		sugar().alerts.waitForLoadingExpiration();

		// Verify search results, Search works fine regardless of the whitespaces presence
		// Sorting needed for consistent result
		sugar().cases.listView.sortBy("headerName", false);
		sugar().cases.listView.verifyField(1, "name", customData.get(1).get("name"));
		sugar().cases.listView.verifyField(2, "name", customData.get(0).get("name"));

		VoodooUtils.waitForReady();
		// Enter a number with leading and trailing whitespaces
		sugar().cases.listView.getControl("searchFilter").set(" "+customData.get(3).get("name").substring(0, 2)+" ");
		sugar().alerts.waitForLoadingExpiration();

		// Verify search results, Search works fine regardless of the whitespaces presence
		sugar().cases.listView.verifyField(1, "name", customData.get(3).get("name"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
