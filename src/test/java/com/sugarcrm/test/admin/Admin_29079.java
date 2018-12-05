package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Admin_29079 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		
		sugar().opportunities.create();
		
		// Enable default Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		VoodooUtils.waitForReady();	
	}

	/**
	 * Verify that warning appears only after clicking on Save button to enable "Opp+RLI" mode
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_29079_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Opportunities
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("opportunityManagement").click();
		VoodooUtils.focusDefault();
		
		// Click the "Opportunities and Revenue Line Items" radio button to change the mode
		// TODO: VOOD-2082
		new VoodooControl("input", "css", "[value='RevenueLineItems']").click();
		
		// Verify that warning message should not appear after selecting radio button
		sugar().alerts.getWarning().assertExists(false);
		
		// Click on Save button
		// TODO: VOOD-2082
		new VoodooControl("a", "css", ".pull-right.dropdown .fld_save_button a").click();

		// Verify that warning message should appear now
		sugar().alerts.getWarning().assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}