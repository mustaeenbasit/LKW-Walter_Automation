package com.sugarcrm.test.products;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Products_20908 extends SugarTest {
	FieldSet customData;
	DataSource systemSettings, manuData;

	public void setup() throws Exception {
		systemSettings = testData.get(testName+"_settings");
		customData = testData.get(testName).get(0);
		manuData = testData.get(testName+"_1");
		// Create Manufacturer
		sugar().manufacturers.api.create(manuData);

		sugar().login();

		// Change system settings to display 5 items per page
		sugar().admin.setSystemSettings(systemSettings.get(0));

		// Navigate to Manu ListView
		sugar().manufacturers.navToListView();
	}

	/**
	 * Manufacturer - Paginate_Verify that the corresponding page is displayed after clicking pagination control link in manufacturer list view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Products_20908_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click pagination control link in list view "Next"
		VoodooUtils.focusFrame("bwc-frame");
		sugar().manufacturers.listView.getControl("nextButton").click();
		// Verify that corresponding page is displayed after clicking the pagination control link in manufacture list view.
		sugar().manufacturers.listView.getControl("pageNumbers").assertContains(customData.get("nextPageValue"), true);

		// Click pagination control link in list view "Previous"
		sugar().manufacturers.listView.getControl("prevButton").click();
		VoodooUtils.waitForReady();
		// Verify that corresponding page is displayed after clicking the pagination control link in manufacture list view.
		sugar().manufacturers.listView.getControl("pageNumbers").assertContains(customData.get("prevPageValue"), true);

		// Click pagination control link in list view "Start"
		sugar().manufacturers.listView.getControl("startButton").click();
		VoodooUtils.waitForReady();
		// Verify that corresponding page is displayed after clicking the pagination control link in manufacture list view.
		sugar().manufacturers.listView.getControl("pageNumbers").assertContains(customData.get("prevPageValue"), true);

		// Click pagination control link in list view "End"
		sugar().manufacturers.listView.getControl("endButton").click();
		VoodooUtils.waitForReady();
		// Verify that corresponding page is displayed after clicking the pagination control link in manufacture list view.
		sugar().manufacturers.listView.getControl("pageNumbers").assertContains(customData.get("endPageValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}