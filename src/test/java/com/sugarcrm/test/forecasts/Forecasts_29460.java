package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Forecasts_29460 extends SugarTest {
	public void setup() throws Exception {
		// Login as admin user
		sugar().login();
	}

	/**
	 * Verify that forecast ranges are updated in Forecast worksheet filter after ranges are changed in Forecasts config
	 * 
	 * @throws Exception
	 */
	@Test
	public void Forecasts_29460_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet forecastData = testData.get(testName).get(0);

		// Navigate to Forecast Settings and set ranges to be 3 ranges. Save
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.toggleRangeSettings();
		sugar().forecasts.setup.setRanges(forecastData.get("rangeInfo"));
		sugar().forecasts.setup.saveSettings();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify that the work-sheet filters. You will see 3 ranges there. So far so good.
		// TODO: VOOD-1459, VOOD-929 and VOOD-1463
		VoodooControl selectedFilterCtrl = new VoodooControl("a", "css", ".filter-view.search .select2-choice-filter");
		VoodooControl selectToSearchCtrl = new VoodooControl("li", "css", ".filter-view.search .select2-search-field");
		VoodooControl secondFilterCtrl = new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-child(2) div");
		VoodooControl thirdFilterCtrl = new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-child(3) div");
		VoodooControl closeSelctedFilterCtrl = new VoodooControl("a", "css", ".select2-search-choice:nth-child(2) .select2-search-choice-close");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".config-header-buttons a[name='save_button'");
		selectedFilterCtrl.assertEquals(forecastData.get("include"), true);
		selectToSearchCtrl.click();
		secondFilterCtrl.assertContains(forecastData.get("upside"), true);
		thirdFilterCtrl.assertContains(forecastData.get("exclude"), true);

		// Select any filter under Forecast drop down(to close drop down)
		secondFilterCtrl.click();

		// Go to forecast config again and change ranges to be custom. Create a few custom ranges and save
		sugar().forecasts.worksheet.getControl("actionDropdown").click();
		sugar().forecasts.worksheet.getControl("settingsButton").click();
		VoodooUtils.waitForReady();
		sugar().forecasts.setup.toggleRangeSettings();
		sugar().forecasts.setup.getControl("rangeCustom").click();
		// TODO: VOOD-1459
		VoodooControl customRangeAddBtnCtrl = new VoodooControl("a", "id", "btnAddCustomRange");
		customRangeAddBtnCtrl.waitForVisible();
		customRangeAddBtnCtrl.click();
		for(int i = 1; i < 3; i++) {
			new VoodooControl("input", "css", ".fld_custom_"+i+" input[type='text']").set(testName+"_"+i);
			new VoodooControl("a", "css", "#custom_"+i+" .addCustomRange").click();
		}
		// Save
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(60000);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify that the filters are updated to custom filters based on the ranges created in the config
		closeSelctedFilterCtrl.click();
		selectedFilterCtrl.assertEquals(forecastData.get("include"), true);
		selectToSearchCtrl.click();
		secondFilterCtrl.assertContains(forecastData.get("upside"), true);
		thirdFilterCtrl.assertContains(forecastData.get("exclude"), true);
		// TODO: VOOD-1463
		new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-child(4) div").assertContains(testName+"_"+1, true);
		new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-child(5) div").assertContains(testName+"_"+2, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}