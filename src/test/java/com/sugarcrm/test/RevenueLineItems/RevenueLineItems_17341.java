package com.sugarcrm.test.RevenueLineItems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17341 extends SugarTest {
	FieldSet forecastData;

	public void setup() throws Exception {
		forecastData = testData.get(testName).get(0);
		sugar().opportunities.api.create();

		// Calculate today's date
		Date date = new Date();
		DateFormat dateNew = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = dateNew.format(date);

		// Login as admin user
		sugar().login();

		// Forecasts module is configured to use 2 ranges(By default 2 ranges are set hence enable default Forecast settings)
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();

		// At least one line item is created for the specified time period and included into forecast.
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.showMore();
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(todaysDate);
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testName.substring(17));
		sugar().revLineItems.createDrawer.getEditField("forecast").set(forecastData.get("include"));
		sugar().revLineItems.createDrawer.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().revLineItems.createDrawer.save();

		// Logout from the admin user
		sugar().logout();
	}

	/**
	 * Verify that updating the forecast category from the forecast module should correspond to the same field in the Revenue Line Items module
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17341_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Login to Sugar as sales rep and go to Forecasts module(QAUser)
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Exclude corresponding RLI from the forecast
		// TODO: VOOD-1459
		VoodooControl forecastStatusFieldCtrl = new VoodooControl("div", "css", ".fld_commit_stage");
		VoodooControl commitBtnCtrl = new VoodooControl("a", "css", ".fld_commit_button a");
		forecastStatusFieldCtrl.click();
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + forecastData.get("exclude") + "')]]").click();

		// Click Save Draft or Commit button to commit a new forecasting value. 
		commitBtnCtrl.click();
		VoodooUtils.pause(1000);
		new VoodooControl("div", "css", "#alerts").waitForVisible(); // Takes a couple of seconds before the save actually executes in Sugar
		VoodooUtils.waitForAlertExpiration(); 

		//  Go to Revenue Line Items module and click on the record modified
		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.focusDefault();

		// Navigate to RLI record
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();

		// Verify that the Forecast field value is "Excluded"
		VoodooControl forecastField = sugar().revLineItems.recordView.getDetailField("forecast");
		forecastField.assertContains(forecastData.get("exclude"), true);

		// Go back to Forecasting module
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Include corresponding RLI from the forecast
		// TODO: VOOD-1459
		new VoodooControl("a", "css", ".layout_ForecastWorksheets .select2-search-choice-close").click(); //Cancel the filter so that all RLI records are appears
		forecastStatusFieldCtrl.click();
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + forecastData.get("include") + "')]]").click();

		// Click Save Draft or Commit button to commit a new forecasting value. 
		commitBtnCtrl.click();
		VoodooUtils.pause(1000);
		new VoodooControl("div", "css", "#alerts").waitForVisible(); // Takes a couple of seconds before the save actually executes in Sugar
		VoodooUtils.waitForAlertExpiration(); 

		//  Go to Revenue Line Items module and click on the record modified
		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.focusDefault();

		// Navigate to RLI record
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();

		// Verify that the Forecast field value is "Include"
		forecastField.assertContains(forecastData.get("include"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}