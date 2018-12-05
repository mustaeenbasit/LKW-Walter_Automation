package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_28569 extends SugarTest{
	DataSource rliCreate;

	public void setup() throws Exception {
		rliCreate = testData.get(testName);
		FieldSet rliDefaultData = sugar().revLineItems.getDefaultData();

		// Create an account record
		AccountRecord oppAssociatedAccount = (AccountRecord)sugar().accounts.api.create();

		// Create an Opportunity record
		OpportunityRecord rliAssociatedOpportunity = (OpportunityRecord)sugar().opportunities.api.create();

		sugar().login();

		// Configuring the Forecasts setting
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		
		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();

		// Associating the Opportunity with an Account
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(oppAssociatedAccount.getRecordIdentifier());
		sugar().opportunities.listView.saveRecord(1);

		// Navigate to the Revenue Line Items module
		sugar().navbar.navToModule(sugar().revLineItems.moduleNamePlural);

		// Create 6 RLI Records, Some with Forecast : Include and some with Exclude and associating them to Opp.
		for(int i=0; i<rliCreate.size(); i++){
			sugar().revLineItems.listView.create();
			sugar().revLineItems.createDrawer.getEditField("name").set(rliCreate.get(i).get("name"));
			sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(rliAssociatedOpportunity.getRecordIdentifier());
			sugar().revLineItems.createDrawer.getEditField("date_closed").set(rliDefaultData.get("date_closed"));
			if(i==0 || i==2){
				sugar().revLineItems.createDrawer.getEditField("forecast").set(rliCreate.get(0).get("forecastValueInclude"));
			}
			sugar().revLineItems.createDrawer.getEditField("likelyCase").set(rliDefaultData.get("likelyCase"));
			sugar().revLineItems.createDrawer.save();
			if(sugar().alerts.getSuccess().queryVisible()){
				sugar().alerts.getSuccess().closeAlert();
			}
		}
	}

	/**
	 * Verify that warning message to update forecast appears when included RLI is deleted from RLI list view
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_28569_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sorting in ascending order so that the Appropriate(on the basis of Forecast) RLI can be deleted
		sugar().revLineItems.listView.sortBy("headerName", true);

		// Test 1:
		// 1. Click the rowactions to the right of the row for the RLI with forecast : Include, select Delete. 
		sugar().revLineItems.listView.verifyField(1, "name", rliCreate.get(0).get("name"));
		sugar().revLineItems.listView.deleteRecord(1);
		sugar().revLineItems.listView.confirmDelete();

		// Should see warning
		sugar().alerts.getWarning().assertContains(rliCreate.get(0).get("warningMessage"), true);
		if(sugar().alerts.getWarning().queryVisible()){
			sugar().alerts.getWarning().closeAlert();
		}

		// Test 2:
		// 1. Click the rowactions to the right of the row for the RLI with forecast : Exclude, select Delete
		sugar().revLineItems.listView.verifyField(1, "name", rliCreate.get(1).get("name"));
		sugar().revLineItems.listView.deleteRecord(1);
		sugar().revLineItems.listView.confirmDelete();

		// Should NOT see warning
		sugar().alerts.getWarning().assertVisible(false);

		// Test 3:
		// 1. Checkmark both created RLIs, one with forecast : Exclude and other with Include
		// 2. Click the top-left mass-update dropdown and select Delete
		sugar().revLineItems.listView.verifyField(1, "name", rliCreate.get(2).get("name"));
		sugar().revLineItems.listView.toggleRecordCheckbox(1);
		sugar().revLineItems.listView.verifyField(2, "name", rliCreate.get(3).get("name"));
		sugar().revLineItems.listView.toggleRecordCheckbox(2);
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.delete();
		sugar().revLineItems.listView.confirmDelete();

		// Should see warning
		sugar().alerts.getWarning().assertContains(rliCreate.get(0).get("warningMessage"), true);
		if(sugar().alerts.getWarning().queryVisible()){
			sugar().alerts.getWarning().closeAlert();
		}

		// Test 4:
		// 1. Checkmark both created RLIs with forecast : Exclude
		// 2. Click the top-left mass-update dropdown and select Delete
		sugar().revLineItems.listView.verifyField(1, "name", rliCreate.get(4).get("name"));
		sugar().revLineItems.listView.toggleRecordCheckbox(1);
		sugar().revLineItems.listView.verifyField(2, "name", rliCreate.get(5).get("name"));
		sugar().revLineItems.listView.toggleRecordCheckbox(2);
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.delete();
		sugar().revLineItems.listView.confirmDelete();

		// Should NOT see warning
		sugar().alerts.getWarning().assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}