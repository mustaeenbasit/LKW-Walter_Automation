package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Forecast_28954 extends SugarTest {
	FieldSet forecastData = new FieldSet();

	public void setup() throws Exception {
		forecastData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		// Login as Admin
		sugar().login();

		// Go to Admin-> Forecasts and configure Forecast settings properly
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Login as Regular User
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	private void commitButtonTooltipVerificationMethod(VoodooControl commitButtonCtrl, VoodooControl tooltipCtrl) throws Exception {
		// Mouse hover on Commit button
		commitButtonCtrl.hover();

		// Verify that the User should not see any tool-tip on Commit button
		tooltipCtrl.assertExists(false);

		// Click Commit button
		sugar().forecasts.worksheet.commit();
		sugar().alerts.getSuccess().closeAlert();

		// Verify that the Commit button will be disabled 
		commitButtonCtrl.assertAttribute("class", forecastData.get("disabled"), true);

		// Mouse hover on Commit button
		commitButtonCtrl.hover();

		// Verify that the user would find a tool-tip: 'To enable Commit: Change a value in the worksheet'
		tooltipCtrl.assertEquals(forecastData.get("tooltipMessage"), true);
	}

	/**
	 * Verify that Commit button tool-tip should come only when it is enabled in Forecasts.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Forecast_28954_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunities-> Create Opportunity -> Enter required fields with proper values so that it could be seen in Forecast module -> Click Save.
		// TODO: VOOD-444
		FieldSet oppData = new FieldSet();
		oppData.put("rli_stage", forecastData.get("rliStage"));
		oppData.put("rli_expected_closed_date", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar().opportunities.create(oppData);
		sugar().alerts.getSuccess().closeAlert();

		// Go to 'Forecasts' module
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Define controls for Forecast Page Commit button and its tool-tip
		// TODO: VOOD-1292
		VoodooControl tooltipCtrl = new VoodooControl("div", "css", ".tooltip-inner");
		VoodooControl commitButtonCtrl = sugar().forecasts.worksheet.getControl("commitButton");

		// Commit Button Tool-tip Verification without making changes in the existing RLI record
		commitButtonTooltipVerificationMethod(commitButtonCtrl, tooltipCtrl);

		// Change 'likely' amount 
		VoodooControl firstRLILikelyCtrl = sugar().forecasts.worksheet.getControl("likelyCase01");
		sugar().forecasts.worksheet.getControl("bestCase01").scrollIntoView();
		firstRLILikelyCtrl.hover();
		// TODO: VOOD-854
		new VoodooControl("i", "css", firstRLILikelyCtrl.getHookString() + " i.fa-pencil").click();
		new VoodooControl("input", "css", firstRLILikelyCtrl.getHookString() + " input").set(forecastData.get("updatedLikelyCase"));
		sugar().forecasts.worksheet.getControl("currentForecastUser").click(); // need to click on the work sheet page to change focus 

		// Commit Button Tool-tip Verification after making changes in the existing RLI record
		commitButtonTooltipVerificationMethod(commitButtonCtrl, tooltipCtrl);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}