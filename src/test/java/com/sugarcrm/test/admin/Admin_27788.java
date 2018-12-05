package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_27788 extends SugarTest {
	FieldSet fsData;

	public void setup() throws Exception {
		fsData = testData.get(testName).get(0);
		sugar().login();
		// setup forecast
		sugar().forecasts.setupForecasts(fsData);
	}

	/**
	 * Verify that the alert clearly states that all Forecast data will be reset when switch 
	 * from Opps to Opps + RLIs and back.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_27788_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Switch from Opportunities + RLIs to Opportunities
		FieldSet switchData = new FieldSet();
		switchData.put("desiredView", "Opportunities");
		switchData.put("rollUp", "latestCloseDate");
		sugar().admin.switchOpportunityView(switchData);

		// verify warning message alert
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().assertContains(fsData.get("assertion_message"), true);
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady(40000);

		// Switch from Opportunities to Opportunities + RLIs.
		switchData.clear();
		switchData.put("desiredView", "RevenueLineItems");
		sugar().admin.switchOpportunityView(switchData);

		// verify warning message alert
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().assertContains(fsData.get("assertion_message"), true);
		sugar().alerts.getWarning().cancelAlert();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}