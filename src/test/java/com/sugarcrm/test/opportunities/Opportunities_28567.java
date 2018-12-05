package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28567 extends SugarTest{
	DataSource oppAndRliCreate = new DataSource();
	FieldSet forecastInfo = new FieldSet();

	public void setup() throws Exception {
		oppAndRliCreate = testData.get(testName);
		forecastInfo = testData.get(testName+"_forecastInfo").get(0);

		// Create an account record
		AccountRecord oppAssociatedAccount = (AccountRecord)sugar().accounts.api.create();

		// Create 6 Opportunity records
		sugar().opportunities.api.create(oppAndRliCreate);

		// Log-in as an Admin
		sugar().login();

		// Configuring the Forecasts setting
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation to opp module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToAdminTools();

		// Mass Update the account name for the opportunities created via api
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.toggleSelectAll();
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.massUpdate();

		// TODO: VOOD-1003 - Lib support needed for mass update controls on list view
		new VoodooSelect("div", "css", ".filter-field .select2-container").set("Account Name");
		new VoodooSelect("div", "css", ".massupdate.fld_account_name .select2-container").set(oppAssociatedAccount.getRecordIdentifier());
		new VoodooControl("a", "css", ".massupdate.fld_update_button a").click();
		VoodooUtils.waitForReady();

		// Navigate to the Revenue Line Items module
		sugar().navbar.navToModule(sugar().revLineItems.moduleNamePlural);

		// Create 6 RLI Records, Some with Forecast : Include and some with Exclude and associating them to Opp.
		for(int i = 0; i <oppAndRliCreate.size(); i++) {
			sugar().revLineItems.listView.create();
			sugar().revLineItems.createDrawer.getEditField("name").set(oppAndRliCreate.get(i).get("name"));
			sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(oppAndRliCreate.get(i).get("name"));
			sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
			if(i == 0 || i == 2) 
				sugar().revLineItems.createDrawer.getEditField("forecast").set(forecastInfo.get("forecastValueInclude"));
			sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
			sugar().revLineItems.createDrawer.save();
			if(sugar().alerts.getSuccess().queryVisible())
				sugar().alerts.getSuccess().closeAlert();
		}
	}

	/**
	 * Verify that warning message to update forecast appears when Opp with Included RLI is deleted from Opportunities list view
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28567_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.sortBy("headerName", true);

		// Test 1:
		// 1. Click the rowactions to the right of the row for the Opp i.e having RLI with forecast : Include, select Delete. 
		sugar().opportunities.listView.verifyField(1, "name", oppAndRliCreate.get(0).get("name"));
		sugar().opportunities.listView.deleteRecord(1);
		sugar().alerts.getWarning().confirmAlert();

		// Should see warning
		sugar().alerts.getWarning().assertContains(forecastInfo.get("warningMessage"), true);
		if(sugar().alerts.getWarning().queryVisible())
			sugar().alerts.getWarning().closeAlert();

		// Test 2:
		// 1. Click the rowactions to the right of the row for the Opp i.e having RLI with forecast : Exclude, select Delete
		sugar().opportunities.listView.verifyField(1, "name", oppAndRliCreate.get(1).get("name"));
		sugar().opportunities.listView.deleteRecord(1);
		sugar().alerts.getWarning().confirmAlert();

		// Should NOT see warning
		sugar().alerts.getWarning().assertVisible(false);

		// Test 3:
		// 1. Checkmark both created Opps i.e having RLIs, one with forecast : Exclude and other with Include
		// 2. Click the top-left mass-update dropdown and select Delete
		sugar().opportunities.listView.verifyField(1, "name", oppAndRliCreate.get(2).get("name"));
		sugar().opportunities.listView.toggleRecordCheckbox(1);
		sugar().opportunities.listView.verifyField(2, "name", oppAndRliCreate.get(3).get("name"));
		sugar().opportunities.listView.toggleRecordCheckbox(2);
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Should see warning
		sugar().alerts.getWarning().assertContains(forecastInfo.get("warningMessage"), true);
		if(sugar().alerts.getWarning().queryVisible())
			sugar().alerts.getWarning().closeAlert();

		// Test 4:
		// 1. Checkmark both created Opps i.e having RLIs with forecast : Exclude
		// 2. Click the top-left mass-update dropdown and select Delete
		sugar().opportunities.listView.verifyField(1, "name", oppAndRliCreate.get(4).get("name"));
		sugar().opportunities.listView.toggleRecordCheckbox(1);
		sugar().opportunities.listView.verifyField(2, "name", oppAndRliCreate.get(5).get("name"));
		sugar().opportunities.listView.toggleRecordCheckbox(2);
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		sugar().alerts.getWarning().confirmAlert();

		// Should NOT see warning
		sugar().alerts.getWarning().assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}