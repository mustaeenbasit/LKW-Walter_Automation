package com.sugarcrm.test.RevenueLineItems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17340 extends SugarTest {
	FieldSet forecastData;
	String todaysDate;

	public void setup() throws Exception {
		forecastData = testData.get(testName).get(0);
		sugar().opportunities.api.create();

		// Calculate today's date
		Date date = new Date();
		DateFormat dateNew = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = dateNew.format(date);

		// Login as admin user
		sugar().login();

		// Forecasts module is configured to use 2 ranges(By default 2 ranges are set hence enable default Forecast settings)
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();
	}

	/**
	 * Verify that updating the forecast category from RLI edit/record view also changes RLI's forecast stage in Forecasts module 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17340_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to RLI module and Select "Create" item under "Revenue Line Items" with Forecast field select "Include" from the dropdown
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(todaysDate);
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testName.substring(17));
		sugar().revLineItems.createDrawer.getEditField("forecast").set(forecastData.get("include"));
		sugar().revLineItems.createDrawer.save();

		// Go to Forecasts module.
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Include filter is ON in Forecasts(By default Include filter is set)
		// Verify that the Revenue Line item is displayed in the worksheet for selected time period and included into the forecast 
		// TODO: VOOD-1459
		VoodooControl forecastStatusFieldCtrl = new VoodooControl("div", "css", ".fld_commit_stage .ellipsis_inline");
		VoodooControl rliNameInForecastWorksheet = new VoodooControl("a", "css", ".parent_name_field .ellipsis_inline a");
		forecastStatusFieldCtrl.assertContains(forecastData.get("include"), true);
		rliNameInForecastWorksheet.assertContains(testName, true);

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();

		// Go back to edit view of the "Revenue line item"
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();

		// Change Forecast field to "Exclude"
		sugar().revLineItems.recordView.getEditField("forecast").set(forecastData.get("exclude"));
		sugar().revLineItems.recordView.save();

		// Go to Forecasts module.
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Include filter is ON in Forecasts
		new VoodooControl("a", "css", ".layout_ForecastWorksheets .select2-search-choice-close").click(); //Cancel the filter so that all RLI records are appears
		new VoodooControl("input", "css", ".filter-view.search input").click();
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + forecastData.get("exclude") + "')]]").click();

		// Verify that the Revenue Line item is displayed in the worksheet for selected time period and Excluded into the forecast 
		forecastStatusFieldCtrl.assertContains(forecastData.get("exclude"), true);
		rliNameInForecastWorksheet.assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}