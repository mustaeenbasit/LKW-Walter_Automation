package com.sugarcrm.test.dashlets;

import java.util.Calendar;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_27790  extends SugarTest {
	public void setup() throws Exception {
		// Create an account record 
		sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		sugar().login();
		fs.put("rli_expected_closed_date", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));

		// Create Opportunity record with expected close date = Current Date
		sugar().opportunities.create(fs);
	}

	/**
	 * Verify that pipeline dashlet is functional when forecast module is not configured
	 * @throws Exception
	 */
	@Test
	public void Dashlets_27790_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource dataSet = testData.get(testName);

		// Go to Home dashboard and check Pipeline dashlet
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// TODO: VOOD-1376 - Need library support for Pipeline Dashlet on Home Dashboard
		VoodooControl dropDownCtrl = new VoodooControl("span", "css", "[data-voodoo-name='forecast-pipeline'] .dashlet-options .select2-arrow");

		// Open the pipeline dashlet's time periods dropdown
		dropDownCtrl.click();

		// Verify 3 default time periods available in time periods dropdown: This quarter, Next Quarter and This Year
		for(int i = 0; i < 3; i++ ){
			new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-child("+(i+1)+") div").assertElementContains(dataSet.get(i).get("time_periods"), true);
		}

		// click on 'This Year' in  in time periods dropdown 
		new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-child(3) div").click();
		VoodooUtils.waitForReady();
		VoodooControl dataCtrl = new VoodooControl("g", "css", ".nv-label-value");

		// Assert that The created opportunity is displayed in the correct time period based on the expected closed date of opportunity.
		dataCtrl.assertContains(dataSet.get(0).get("data"), true);
		dropDownCtrl.click();

		// Go to admin -> Forecasts and configure forecast module
		FieldSet forecastSettings = new FieldSet();
		forecastSettings.put("timePeriodType", dataSet.get(4).get("time_periods"));
		sugar().forecasts.setupForecasts(forecastSettings);

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToAdminTools();

		// Navigate to pipeline dashlet on Home dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();
		dropDownCtrl.click();

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH); /* 0 through 11 */
		int quarter = (month / 3) + 1; 

		// TODO: VOOD-1376
		new VoodooControl("div", "xpath", "//div[@id='select2-drop']/ul/li/div/div[contains(text(),'"+ cal.get(Calendar.YEAR) + " " + dataSet.get(3).get("time_periods") + quarter + "')]").click();

		// Assert that created opportunity is displayed in the correct time period based on the expected closed date of opportunity.
		dataCtrl.assertContains(dataSet.get(0).get("data"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}