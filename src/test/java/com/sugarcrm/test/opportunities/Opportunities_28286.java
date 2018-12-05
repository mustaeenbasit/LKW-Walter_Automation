package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = true)
public class Opportunities_28286 extends SugarTest {
	public void setup() throws Exception {
		// Account record is created
		sugar().accounts.api.create();

		// Login
		sugar().login();

		// Forecast module has to be configured in Admin -> Forecasts
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		VoodooUtils.waitForReady();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
	}

	/**
	 * ENT/ULT: Verify that Forecast field is present in opportunity record view and list view when project from Opportunities only
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28286_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet forecastData = testData.get(testName).get(0);

		// Go to Admin -> Opportunities and switch to project from opportunities only
		FieldSet switchData = new FieldSet();
		switchData.put("desiredView", "Opportunities");
		switchData.put("rollUp", "latestCloseDate");
		sugar().admin.switchOpportunityView(switchData);
		if (sugar().alerts.getWarning().queryVisible())
			sugar().alerts.getWarning().confirmAlert();

		// Go to Opportunities module and click Create button
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.recordView.showMore();

		// Verify that an editable 'Forecast' drop down field is present on opportunity create drawer.
		// TODO: VOOD-1445
		VoodooControl forecastFieldCtrl = new VoodooControl("span", "css", ".edit.fld_commit_stage");
		forecastFieldCtrl.assertAttribute("class", forecastData.get("disabled"), false);

		// Verify that 'Forecast' field It contains all ranges specified when forecast module was configured 
		// TODO: VOOD-1463
		forecastFieldCtrl.click();
		VoodooSelect forecastDropdownList = new VoodooSelect("ul", "css", "#select2-drop ul");
		forecastDropdownList.assertContains(forecastData.get("include"), true);
		forecastDropdownList.assertContains(forecastData.get("exclude"), true);

		// Select any Forecast range value present under Forecast drop down (Hack close dropdown)
		// TODO: VOOD-629 and VOOD-806
		new VoodooControl("div", "css", ".select2-result-label").click();

		// Create and save one opportunity record
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.getDefaultData().get("relAccountName"));
		sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
		sugar().opportunities.createDrawer.getEditField("likelyCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));
		sugar().opportunities.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Verify that the value of Forecast field is displayed correctly on the list view of Opportunity module 
		sugar().opportunities.listView.getDetailField(1, "name").assertEquals(testName, true);
		// TODO: VOOD-1359
		new VoodooControl("div", "css", ".list.fld_commit_stage div").assertEquals(forecastData.get("include"), true);

		// Navigates to the record view of the created Opportunity record
		sugar().opportunities.listView.clickRecord(1);

		// Verify that the value of Forecast field is displayed correctly on the record view of Opportunity module 
		sugar().opportunities.recordView.getDetailField("name").assertEquals(testName, true);
		// TODO: VOOD-1359
		new VoodooControl("div", "css", ".detail.fld_commit_stage div").assertEquals(forecastData.get("include"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}