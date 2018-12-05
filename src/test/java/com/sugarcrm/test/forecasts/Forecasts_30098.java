package com.sugarcrm.test.forecasts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Forecasts_30098 extends SugarTest {
	DataSource testDataDS = new DataSource();
	public void setup() throws Exception {

		// Initialize Test Data
		testDataDS = testData.get(testName);
		// Opp only Test
		sugar().admin.api.switchToOpportunitiesView();

		// Create 2 opp (to test sorting) + account (to relate via UI)
		sugar().opportunities.api.create();
		sugar().opportunities.api.create();
		sugar().accounts.api.create();

		// Admin login
		sugar().login();

		// Update 2 opp names, close date to today - to include in forecast
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		for (int i = 1; i < testDataDS.size() + 1; i++) {
			sugar().opportunities.listView.editRecord(i);
			sugar().opportunities.listView.getEditField(i, "name").set(testName + "_" + i);
			sugar().opportunities.listView.getEditField(i, "salesStage").set(testDataDS.get(i-1).get("salesStageValue"));
			sugar().opportunities.listView.getEditField(i, "date_closed").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
			sugar().opportunities.listView.getEditField(i, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
			sugar().opportunities.listView.saveRecord(i);
		}
	}

	/**
	 * Verify that "Sales Stage" field in Forecast Module is sort-able (opp only)
	 * @throws Exception
	 */
	@Test
	public void Forecasts_30098_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enable default Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		
		// Cancel the Include option from the filter	// TODO: VOOD-1385
		new VoodooControl("a", "css", ".filter-view .select2-search-choice-close").click();
		VoodooUtils.waitForReady();
		
		// Assert Forecast field SalesStage header is sortable
		sugar().forecasts.worksheet.getControl("headerSalesstage").assertAttribute("class", "sorting", true);

		for (int i = 1; i < testDataDS.size()+1; i++) {
			// Click header - first time to sort desc - second time to sort asc
			sugar().forecasts.worksheet.getControl("headerSalesstage").click();
			VoodooUtils.waitForReady();
			
			// Assert Forecast field SalesStage header is sorted desc / asc
			sugar().forecasts.worksheet.getControl("headerSalesstage").assertAttribute("class", testDataDS.get(i-1).get("sortingClassValue"), true);
			
			// Assert a Row in worksheet
			sugar().forecasts.worksheet.getDetailField(i, "salesStage").assertEquals(testDataDS.get(1).get("salesStageValue"), true);
			
			// Assert other Row in worksheet
			sugar().forecasts.worksheet.getDetailField(testDataDS.size()+1-i, "salesStage").assertEquals(testDataDS.get(0).get("salesStageValue"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}