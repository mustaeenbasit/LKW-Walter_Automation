package com.sugarcrm.test.grimoire;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class ForecastModuleTests extends SugarTest {
	RevLineItemRecord myRLI;
	
	public void setup() throws Exception {
		sugar().login();
		
		FieldSet forecastSettings = new FieldSet();
		forecastSettings.put("timePeriodType", "Yearly (Quarterly sub-periods)");
		sugar().forecasts.setupForecasts(forecastSettings);

		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		
		FieldSet editedData = new FieldSet();
		editedData.put("forecast", "Include");
		myRLI = (RevLineItemRecord)sugar().revLineItems.create(editedData);
		VoodooUtils.waitForAlertExpiration(30000);

		VoodooUtils.pause(2000); // Pause to take care of CI only alert that I can not determine
	}

	@Ignore("SFA-4160 Active Timeouts in Sugar after Forecast Settings are saved prevents automation from taking place")
	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToModule("Forecasts");
		new VoodooControl("tr", "id", "forecastsWorksheetTotalsOverallTotals").waitForVisible(120000);
		VoodooUtils.pause(10000); // There is no loading message or any other alert to wait on and the above waitForVisible doesn't work 100% in CI
		new VoodooControl("div", "css", ".side.sidebar-content").assertVisible(true);
		sugar().forecasts.worksheet.toggleSidebar(); // Test
		new VoodooControl("div", "css", ".side.sidebar-content").assertVisible(false);
		sugar().forecasts.worksheet.toggleSidebar();
		new VoodooControl("div", "css", ".side.sidebar-content").assertVisible(true);
		
		sugar().forecasts.worksheet.setTimePeriod("2014 Q3"); // Test
		
		sugar().forecasts.worksheet.previewRecord(1); // Test
		sugar().previewPane.getPreviewPaneField("name").assertContains(sugar().revLineItems.defaultData.get("name"), true);
		
		FieldSet rliUpdate = new FieldSet();
		rliUpdate.put("likelyCase", "11.11");
		sugar().forecasts.worksheet.updateRecord(1, rliUpdate); // Test
		sugar().forecasts.worksheet.verifyField(1, "likelyCase", "11.11"); // Test
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}