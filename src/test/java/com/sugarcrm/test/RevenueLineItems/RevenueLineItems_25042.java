package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_25042 extends SugarTest {
	public void setup() throws Exception {
		// Opportunity record is created
		sugar().opportunities.api.create();

		// Login
		sugar().login();

		// Forecast module has to be configured in Admin -> Forecasts
		// Enable default Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		VoodooUtils.waitForReady();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToAdminTools();
	}

	/**
	 * Verify that setting sales stage of RLI to Closed Lost, Forecast range changes to Excluded and set to read only
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_25042_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet rliData = testData.get(testName).get(0);

		// Go to RLI module and click Create button
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.showMore();

		// Define required controls
		VoodooControl forecastDetailFieldCtrl = sugar().revLineItems.recordView.getDetailField("forecast");
		VoodooControl saleStageCreateFieldCtrl = sugar().revLineItems.createDrawer.getEditField("salesStage");
		VoodooSelect saleStageEditFieldCtrl = (VoodooSelect) sugar().revLineItems.recordView.getEditField("salesStage");
		VoodooControl saleStageDetailFieldCtrl = sugar().revLineItems.recordView.getDetailField("salesStage");
		VoodooControl forecastCreateFieldCtrl = sugar().revLineItems.createDrawer.getEditField("forecast");

		// Change the sales stage to Closed Lost
		saleStageCreateFieldCtrl.set(rliData.get("closedLost"));

		// Verify that the Forecast range is changed to excluded
		forecastCreateFieldCtrl.assertContains(rliData.get("exclude"), true);
		VoodooUtils.waitForReady();

		// Verify that the Forecast field is set to read only
		// TODO: VOOD-1445
		VoodooControl forecastFieldCtrl = new VoodooControl("span", "css", ".edit.fld_commit_stage");
		forecastFieldCtrl.assertAttribute("class", rliData.get("disabled"), true);

		// Change the sales stage to any other not-closed stage (taking prospecting)
		saleStageCreateFieldCtrl.set(rliData.get("prospecting"));

		// Verify that Forecast range is no longer set to read only and can be modified
		forecastFieldCtrl.assertAttribute("class", rliData.get("disabled"), false);
		forecastCreateFieldCtrl.set(rliData.get("include"));

		// Fill out all required fields and save RLI
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().revLineItems.getDefaultData().get("relOpportunityName"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Verify that the RLI record is saved successfully
		sugar().revLineItems.listView.getDetailField(1, "name").assertEquals(testName, true);
		sugar().revLineItems.listView.getDetailField(1, "salesStage").assertContains(rliData.get("prospecting"), true);
		sugar().revLineItems.listView.getDetailField(1, "forecast").assertContains(rliData.get("include"), true);

		// Select the created RLI record from the List view
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();

		// Click on the pencil Icon
		saleStageDetailFieldCtrl.hover();
		// TODO: VOOD-854
		new VoodooControl ("i", "css", "span[data-name='sales_stage'] .fa.fa-pencil").click();

		// Change the sales stage to Closed Lost
		// Hack used here as drop down is remains opened, to close it select at least one record
		saleStageEditFieldCtrl.selectWidget.getControl("searchBox").set(rliData.get("closedLost")); // need to close the drop down first
		VoodooUtils.waitForReady();
		// TODO: VOOD-629 and VOOD-806
		VoodooControl selectFirstRecordCtrl = new VoodooControl("div", "css", ".select2-result-label");
		selectFirstRecordCtrl.click();

		// Verify that the Forecast range is changed to excluded
		forecastDetailFieldCtrl.assertContains(rliData.get("exclude"), true);
		VoodooUtils.waitForReady();

		// Verify that the Forecast field is set to read only (on hovering if pencil Icon is not visible, it shows read only)
		forecastDetailFieldCtrl.hover();
		// TODO: VOOD-854
		VoodooControl forecastPencilCtrl = new VoodooControl ("i", "css", "span[data-name='commit_stage'] .fa.fa-pencil");
		forecastPencilCtrl.assertVisible(false);

		// Change the sales stage to any other not-closed stage (taking prospecting)
		saleStageEditFieldCtrl.set(rliData.get("prospecting"));

		// Verify that Forecast range is no longer set to read only and can be modified
		forecastDetailFieldCtrl.hover();
		forecastPencilCtrl.click();
		selectFirstRecordCtrl.click();

		// Save the RLI record
		sugar().revLineItems.recordView.save();

		// Verify that the record is saved accordingly
		sugar().revLineItems.recordView.getDetailField("name").assertEquals(testName, true);
		saleStageDetailFieldCtrl.assertEquals(rliData.get("prospecting"), true);
		forecastDetailFieldCtrl.assertEquals(rliData.get("include"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}