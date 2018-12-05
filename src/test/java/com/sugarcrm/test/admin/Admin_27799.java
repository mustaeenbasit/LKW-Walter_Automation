package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_27799 extends SugarTest {
	FieldSet fsData;
	public void setup() throws Exception {
		fsData = testData.get(testName).get(0);
		sugar().login();
		// setup forecast
		FieldSet forecastSettings = new FieldSet();
		forecastSettings.put("timePeriodType", fsData.get("timePeriodType"));
		sugar().forecasts.setupForecasts(forecastSettings);
	}

	/**
	 * Verify that an alert informs user that all Forecast data will be reset when I change tracking from Opps + RLIs to Opps only
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_27799_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();

		// TODO: VOOD-1403: Need Library support of UI switching of RLI vs Opp view in Admin Tools
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("opportunityManagement").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		new VoodooControl("input", "css", "[name='opps_view_by'][value='Opportunities']").click();
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		
		// verify warning message alert
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().assertContains(fsData.get("assertion_message"), true);
		sugar().alerts.getWarning().cancelAlert();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}