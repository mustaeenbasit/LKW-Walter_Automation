package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Forecasts_28838 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * 'Forecast' page should not crash while make changes in Forecast settings 
	 * @throws Exception
	 */
	@Test
	public void Forecasts_28838_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		FieldSet forecastSettings = new FieldSet();
		forecastSettings.put("timePeriodType", customFS.get("timePeriodType"));
		sugar.forecasts.setupForecasts(forecastSettings);
		
		// Verify that Forecast should be configured without any error
		sugar.alerts.getSuccess().assertContains(customFS.get("errorMsg"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}