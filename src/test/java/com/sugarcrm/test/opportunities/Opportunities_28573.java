package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_28573 extends SugarTest{
	DataSource opportunityData = new DataSource();

	public void setup() throws Exception {
		// Create an account record
		sugar().accounts.api.create();
		opportunityData = testData.get(testName);

		// Log-in as an Administrator
		sugar().login();

		// Configuring Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation toOpportunity module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToAdminTools();

		// Navigate to the Opportunities module
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);

		// Create Opportunities record with Forecast value: Include/Exclude
		for(int i = 0; i <opportunityData.size(); i++) {
			sugar().opportunities.listView.create();
			sugar().opportunities.createDrawer.getEditField("name").set(opportunityData.get(i).get("name"));
			sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
			sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
			sugar().opportunities.createDrawer.getEditField("likelyCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));

			if(i == 0)
				sugar().opportunities.createDrawer.getEditField("forecast").set(opportunityData.get(0).get("forecastValueInclude"));
			sugar().opportunities.createDrawer.save();
			if(sugar().alerts.getSuccess().queryVisible()) 
				sugar().alerts.getSuccess().closeAlert();
		}
	}

	/**
	 * Opp ONLY mode: Verify that warning message to update forecast appears when included Opp is deleted from 
	 * Opp record view when project by Opportunities only
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28573_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sorting in ascending order so that the Appropriate(on the basis of Forecast) Opportunity can be deleted
		sugar().opportunities.listView.sortBy("headerName", true);

		// Test 1:
		// 1. click the dropdown next to Edit, select Delete
		sugar().opportunities.listView.verifyField(1, "name", opportunityData.get(0).get("name"));
		sugar().opportunities.listView.clickRecord(1);

		// Assert that the forecast is Include
		sugar().opportunities.recordView.getDetailField("forecast").
				assertContains(opportunityData.get(0).get("forecastValueInclude"), true);

		// Assert that the Sales stage is not Closed Won/Lost (is Prospecting)
		sugar().opportunities.recordView.getDetailField("salesStage").
				assertContains(sugar().opportunities.getDefaultData().get("rli_stage"), true);

		// Delete the Opportunity record from the record view
		sugar().opportunities.recordView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Should see warning
		sugar().alerts.getWarning().assertContains(opportunityData.get(0).get("warningMessage"), true);
		if(sugar().alerts.getWarning().queryVisible())
			sugar().alerts.getWarning().closeAlert();

		// Test 2:
		// 1. click the dropdown next to Edit, select Delete
		sugar().opportunities.listView.verifyField(1, "name", opportunityData.get(1).get("name"));
		sugar().opportunities.listView.clickRecord(1);

		// Assert that the forecast is Exclude
		sugar().opportunities.recordView.getDetailField("forecast").
				assertContains(opportunityData.get(0).get("forecastValueInclude"), false);

		// Assert that the Sales stage is not Closed Won/Lost (is Prospecting)
		sugar().opportunities.recordView.getDetailField("salesStage").
				assertContains(sugar().opportunities.getDefaultData().get("rli_stage"), true);

		// Delete the Opportunity record from the record view
		sugar().opportunities.recordView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Should NOT see warning
		sugar().alerts.getWarning().assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}