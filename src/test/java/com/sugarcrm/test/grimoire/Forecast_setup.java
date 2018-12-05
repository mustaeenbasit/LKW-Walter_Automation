package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Forecast_setup extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet forecastSettings = new FieldSet();
		forecastSettings.put("timePeriodType", "Yearly (Quarterly sub-periods)");
		forecastSettings.put("timePeriodFuture", "3");
		forecastSettings.put("timePeriodPast", "1");
		forecastSettings.put("range", "two,20");
		forecastSettings.put("scenario1", "Best");
		forecastSettings.put("scenario2", "Worst");
		forecastSettings.put("worksheet1", "Opportunity Name");
		forecastSettings.put("worksheet2", "Account Name");
		forecastSettings.put("worksheet3", "Expected Close Date");
		// TODO: Product is no longer a worksheet option, find out if this is intentional
		// forecastSettings.put("worksheet4", "Product");
		forecastSettings.put("worksheet4", "Type");
		forecastSettings.put("worksheet5", "Sales Stage");
		forecastSettings.put("worksheet6", "Probability");
		
		sugar().forecasts.setupForecasts(forecastSettings);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	@Test
	public void testRangeThreeSetup() throws Exception{
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet forecastSettings = new FieldSet();
		forecastSettings.put("timePeriodType", "Yearly (Quarterly sub-periods)");
		forecastSettings.put("rangeThree", "three,60,90");

		sugar().forecasts.setupForecasts(forecastSettings);

		forecastSettings.clear();
		forecastSettings.put("timePeriodType", "Yearly (Quarterly sub-periods)");
		forecastSettings.put("rangeThree", "three,20,80");

		sugar().forecasts.setupForecasts(forecastSettings);

		VoodooUtils.voodoo.log.info(testName + "  complete.");
	}

	public void cleanup() throws Exception {}
}
