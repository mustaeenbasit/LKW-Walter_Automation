package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_18437 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that 'In Forecasts' dashlet can be added to the home page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_18437_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960 Dashlet selection
		VoodooControl forecastSearch = new VoodooControl("input", "css", ".span4.search");
		VoodooControl forecastDetails = new VoodooControl("div", "css", "div[data-voodoo-name='forecastdetails']");

		sugar.home.dashboard.edit();
		sugar.home.dashboard.addRow();
		// TODO: VOOD-958 sugar.accounts.dashboard.addDashlet(1, 3); fails
		new VoodooControl("div", "css", ".add-dashlet").click();
		// TODO: VOOD-960 Dashlet selection
		forecastSearch.waitForVisible();
		forecastSearch.set(testData.get(testName).get(0).get("forecastValue"));
		new VoodooControl("a", "css", ".list.fld_title .ellipsis_inline a").click();
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("a","css","#content a[name='save_button']").click();
		sugar.alerts.getSuccess().closeAlert();
		forecastDetails.assertVisible(true);
		sugar.accounts.navToListView();
		sugar.logout();

		// Verify that 'In Forecasts' dashlet added to the home page
		sugar.login();
		forecastDetails.assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}