package com.sugarcrm.test.sweetspot;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Sweetspot_28952 extends SugarTest {
	DataSource accountData= new DataSource();

	public void setup() throws Exception {
		accountData = testData.get(testName);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Verify that search result shouldn't be shown blank while searching through sweet spot 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28952_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Activate Sweetspot
		sugar().sweetspot.show();

		// Entering 'Acc' in sweetspot search panel
		sugar().sweetspot.search(accountData.get(0).get("name").substring(4, 7));

		// Click "View All Results..." in sweetspot
		sugar().sweetspot.getRecordsResult(4).click();
		VoodooUtils.waitForReady();

		// Asserting the existence of records in sweetspot search results
		sugar().globalSearch.getRow(1).assertVisible(true);
		sugar().globalSearch.clickRecord(1);
		sugar().accounts.recordView.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}