package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17349 extends SugarTest {
	FieldSet forecastData;
	VoodooControl forecastSaveBtnCtrl;

	public void setup() throws Exception {
		forecastData = testData.get(testName).get(0);
		sugar().opportunities.api.create();

		// Login as admin user
		sugar().login();
	}

	/**
	 * Verify that the values shown in the Forecast dropdown correspond to the settings for range types as specified in the forecasts configuration.
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17349_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Forecasts module
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		//  In the configuration wizard go to Ranges tab
		sugar().forecasts.setup.toggleRangeSettings();

		// Change ranges to three ranges
		sugar().forecasts.setup.getControl("rangeThree").click();

		// Accept all the defaults for other settings and click finish/Save the Forecast module settings
		sugar().forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();

		// Go to revenue line items and click "Create Revenue Line Item" from mega menu.
		sugar().revLineItems.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().revLineItems);
		VoodooControl createRevenueLineItem = sugar().revLineItems.menu.getControl("createRevenueLineItem");
		createRevenueLineItem.click();

		// Look under "Forecasts" dropdown
		VoodooSelect forecastFieldCtrl = (VoodooSelect) sugar().revLineItems.createDrawer.getEditField("forecast");
		forecastFieldCtrl.click();

		// Verify that there are 3 items in the dropdown: Include, Upside and Exclude.
		// TODO: VOOD-1463
		VoodooSelect forecastDropdownList = new VoodooSelect("ul", "css", "#select2-drop ul");
		forecastDropdownList.assertContains(forecastData.get("include"), true);
		forecastDropdownList.assertContains(forecastData.get("upside"), true);
		forecastDropdownList.assertContains(forecastData.get("exclude"), true);

		// Select any custom ranges based on probability present under Forecast dropdown (Hack close dropdown)
		forecastFieldCtrl.selectWidget.getControl("searchBox").set(forecastData.get("include"));
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + forecastData.get("include") + "')]]").click();

		// Fill all the required Fields and select
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testName.substring(17));

		// Click Save
		sugar().revLineItems.createDrawer.save();

		// Navigate to RLI record
		sugar().revLineItems.listView.clickRecord(1);

		// Verify the custom ranges based on probability value appears on the record view of the RLI record
		sugar().revLineItems.recordView.getDetailField("forecast").assertContains(forecastData.get("include"), true);

		
		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();
		// Repeat the same steps using Custom Ranges 
		// Navigate to the Forecast module and update the settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// TODO: VOOD-1459
		new VoodooControl("a", "css", ".fld_main_dropdown .dropdown-toggle.btn-primary").click();
		new VoodooControl("a", "css", "[name='settings_button']").click();

		// Configure forecast module to use 4-5 custom ranges based on probability
		sugar().forecasts.setup.toggleRangeSettings();
		sugar().forecasts.setup.getControl("rangeCustom").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1459
		VoodooControl customRangeAddBtnCtrl = new VoodooControl("a", "id", "btnAddCustomRange");
		customRangeAddBtnCtrl.click();
		VoodooUtils.waitForReady();
		for(int i = 1; i < 5; i++) {
			new VoodooControl("input", "css", ".fld_custom_"+i+" input[type='text']").set(testName+"_"+i);
			new VoodooControl("a", "css", "#custom_"+i+" .addCustomRange").click();
		}

		// Save the Forecast module settings
		forecastSaveBtnCtrl = new VoodooControl("a", "css", ".config-header-buttons.fld_save_button a[name='save_button']");
		forecastSaveBtnCtrl.click();
		VoodooUtils.pause(1000);
		new VoodooControl("div", "css", "#alerts").waitForVisible(); // Takes a couple of seconds before the save actually executes in Sugar
		VoodooUtils.waitForAlertExpiration(); // VoodooUtils.waitForReady(); not working fine

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();

		// Go to revenue line items and click "Create Revenue Line Item" from mega menu.
		sugar().revLineItems.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().revLineItems);
		createRevenueLineItem.click();

		// Look at all options available under Forecast dropdown
		forecastFieldCtrl.click();

		// Verify that all custom ranges based on probability are present under Forecast dropdown 
		for (int i = 1; i < 5; i++) {
			forecastDropdownList.assertContains(testName+"_"+i, true);
		}

		// Select any custom ranges based on probability present under Forecast dropdown (Hack close dropdown)
		forecastFieldCtrl.selectWidget.getControl("searchBox").set(testName+"_"+1);
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + testName+"_"+1 + "')]]").click();

		// Fill all the required Fields and select
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testName.substring(17));

		// Click Save
		sugar().revLineItems.createDrawer.save();

		// Navigate to RLI record
		sugar().revLineItems.listView.clickRecord(1);

		// Verify the custom ranges based on probability value appears on the record view of the RLI record
		sugar().revLineItems.recordView.getDetailField("forecast").assertContains(testName+"_"+1, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}