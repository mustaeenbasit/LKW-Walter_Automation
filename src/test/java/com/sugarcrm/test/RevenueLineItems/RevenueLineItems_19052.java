package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_19052 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();

		// Login as admin user
		sugar().login();

		// Configure forecast module to use 4-5 custom ranges based on probability
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.toggleRangeSettings();
		sugar().forecasts.setup.getControl("rangeCustom").click();

		// TODO: VOOD-1459
		VoodooControl customRangeAddBtnCtrl = new VoodooControl("a", "id", "btnAddCustomRange");
		customRangeAddBtnCtrl.waitForVisible();
		customRangeAddBtnCtrl.click();
		for(int i = 1; i < 5; i++) {
			new VoodooControl("input", "css", ".fld_custom_"+i+" input[type='text']").set(testName+"_"+i);
			new VoodooControl("a", "css", "#custom_"+i+" .addCustomRange").click();
		}

		// Save the Forecast module settings
		sugar().forecasts.setup.saveSettings();
	}

	/**
	 * ENT/ULT: Verify that custom ranges are displayed in the Forecast dropdown in RLI edit mode 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_19052_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();

		// Go to RLI module and click Create button
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Look at all options available under Forecast dropdown
		VoodooSelect forecastFieldCtrl = (VoodooSelect) sugar().revLineItems.createDrawer.getEditField("forecast");
		forecastFieldCtrl.click();

		// Verify that all custom ranges based on probability are present under Forecast dropdown 
		// TODO: VOOD-1463
		VoodooSelect forecastDropdownList = new VoodooSelect("ul", "css", "#select2-drop ul");
		for (int i = 1; i < 5; i++) {
			forecastDropdownList.assertContains(testName+"_"+i, true);
		}

		// Select any custom ranges based on probability present under Forecast dropdown (Hack close dropdown)
		forecastFieldCtrl.selectWidget.getControl("searchBox").set(testName+"_"+1);
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[text()[contains(.,'" + testName+"_"+1 + "')]]").click();

		// Fill all the required Fields and select
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testName.substring(18));

		// Click Save and navigate to the RLI record created
		sugar().revLineItems.createDrawer.save();
		sugar().revLineItems.listView.clickRecord(1);

		// Verify the custom ranges based on probability value appears on the record view of the RLI record
		sugar().revLineItems.recordView.getDetailField("forecast").assertContains(testName+"_"+1, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}