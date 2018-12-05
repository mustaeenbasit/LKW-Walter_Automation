package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_28572 extends SugarTest{
	DataSource opportunityData = new DataSource();
	VoodooControl opportunitiesStudioCtrl;

	public void setup() throws Exception {
		// Create an account record
		sugar().accounts.api.create();
		opportunityData = testData.get(testName);

		// Log-in as an Administrator
		sugar().login();

		// Configuring Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// TODO: VOOD-1666 - Not able to proceed after 'sugar().forecasts.setup.saveSettings();', Hence, need to 
		// navigate to admin module before navigating to Opportunities list view
		sugar().navbar.navToAdminTools();

		// Navigate to the Opportunities module		
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		FieldSet oppDefaultData = sugar().opportunities.getDefaultData();

		// Create Opportunities record with Forecast value: Include/Exclude
		for(int i=0; i<opportunityData.size(); i++) {
			sugar().opportunities.listView.create();
			sugar().opportunities.createDrawer.getEditField("name").set(opportunityData.get(i).get("name"));
			sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
			sugar().opportunities.createDrawer.getEditField("likelyCase").set(oppDefaultData.get("likelyCase"));
			sugar().opportunities.createDrawer.getEditField("date_closed").set(oppDefaultData.get("date_closed"));
			if(i==0 || i==2){

				// TODO: VOOD-1489
				new VoodooSelect("div", "css", ".fld_commit_stage.edit .select2-container").
				set(opportunityData.get(0).get("forecastValueInclude"));
			}
			sugar().opportunities.createDrawer.save();
			if(sugar().alerts.getSuccess().queryVisible()){
				sugar().alerts.getSuccess().closeAlert();
			}
		}
	}

	/**
	 * Opp ONLY: Verify that warning message to update forecast appears when Included Opp is deleted from 
	 * Opp list view when project by Opportunities only
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28572_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sorting in ascending order so that the Appropriate(on the basis of Forecast) Opportunity can be deleted
		sugar().opportunities.listView.sortBy("headerName", true);

		// Test 1:
		// 1. Click the rowactions to the right of the row for the Opp with forecast : Include, select Delete. 
		sugar().opportunities.listView.verifyField(1, "name", opportunityData.get(0).get("name"));
		sugar().opportunities.listView.deleteRecord(1);
		sugar().opportunities.listView.confirmDelete();

		// Should see warning
		sugar().alerts.getWarning().assertContains(opportunityData.get(0).get("warningMessage"), true);
		if(sugar().alerts.getWarning().queryVisible()){
			sugar().alerts.getWarning().closeAlert();
		}

		// Test 2:
		// 1. Click the rowactions to the right of the row for the Opp with forecast : Exclude, select Delete
		sugar().opportunities.listView.verifyField(1, "name", opportunityData.get(1).get("name"));
		sugar().opportunities.listView.deleteRecord(1);
		sugar().opportunities.listView.confirmDelete();

		// Should NOT see warning
		sugar().alerts.getWarning().assertVisible(false);

		// Test 3:
		// 1. Checkmark both created Opps i.e one with forecast : Exclude and other with Include
		// 2. Click the top-left mass-update dropdown and select Delete
		sugar().opportunities.listView.verifyField(1, "name", opportunityData.get(2).get("name"));
		sugar().opportunities.listView.toggleRecordCheckbox(1);
		sugar().opportunities.listView.verifyField(2, "name", opportunityData.get(3).get("name"));
		sugar().opportunities.listView.toggleRecordCheckbox(2);
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		sugar().opportunities.listView.confirmDelete();

		// Should see warning
		sugar().alerts.getWarning().assertContains(opportunityData.get(0).get("warningMessage"), true);
		if(sugar().alerts.getWarning().queryVisible()){
			sugar().alerts.getWarning().closeAlert();
		}

		// Test 4:
		// 1. Checkmark both created Opps i.e with forecast : Exclude
		// 2. Click the top-left mass-update dropdown and select Delete
		sugar().opportunities.listView.verifyField(1, "name", opportunityData.get(4).get("name"));
		sugar().opportunities.listView.toggleRecordCheckbox(1);
		sugar().opportunities.listView.verifyField(2, "name", opportunityData.get(5).get("name"));
		sugar().opportunities.listView.toggleRecordCheckbox(2);
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		sugar().opportunities.listView.confirmDelete();

		// Should NOT see warning
		sugar().alerts.getWarning().assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}