package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_28395 extends SugarTest {
	FieldSet roleRecord = new FieldSet();
	public void setup() throws Exception {
		sugar().accounts.api.create();
		roleRecord = testData.get(testName).get(0);
		sugar().revLineItems.api.create();
		sugar().opportunities.api.create();
		sugar().login();
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
	}
	/**
	 * Verify that changing sales stage of Opportunity/RLI does not expose probability field if it set to "No access"
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_28395_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		VoodooUtils.waitForReady();
		// Create Role 
		// TODO: VOOD-856
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".edit.view tr:nth-of-type(21) td a").click();
		new VoodooControl("div", "id", "probabilitylink").click();
		new VoodooControl("select", "id", "flc_guidprobability").set(roleRecord.get("permission"));
		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assigning role to qauser 
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		sugar().logout();

		// Logging through qasuer
		sugar().login(sugar().users.getQAUser());
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Asserting the value of Probability field equals to "no access" while Creating the RLI record.
		// TODO: VOOD-1349
		VoodooControl rliProbabilityRecordViewCtrl = new VoodooControl("span", "css", "span[data-fieldname ='probability'] .noaccess.fld_probability");
		rliProbabilityRecordViewCtrl.assertEquals(roleRecord.get("probabilityValue"), true);
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(roleRecord.get("salesStage1"));
		rliProbabilityRecordViewCtrl.assertEquals(roleRecord.get("probabilityValue"), true);
		sugar().revLineItems.createDrawer.cancel();

		// Asserting the value of Probability field remains equals to "no access" while editing the salesstage in the record View
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("salesStage").set(roleRecord.get("salesStage1"));
		rliProbabilityRecordViewCtrl.assertEquals(roleRecord.get("probabilityValue"), true);
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));

		// Using the current date for closeDate of RLI so that it can be seen in Forecasts Module.
		String currDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		System.out.println(currDate);
		sugar().revLineItems.recordView.getEditField("date_closed").set(currDate);
		sugar().revLineItems.recordView.save();

		// Asserting the value of Probability field remains equals to "no access" while editing the salesstage in the List View
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.editRecord(1);
		sugar().revLineItems.listView.getEditField(1, "salesStage").set(roleRecord.get("salesStage2"));

		// TODO: VOOD-1349
		VoodooControl rliProbabilityListViewCtrl = new VoodooControl("span", "css", ".noaccess.fld_probability span");
		rliProbabilityListViewCtrl.assertEquals(roleRecord.get("probabilityValue"), true);
		sugar().revLineItems.listView.saveRecord(1);

		// Navigating to Forecasts module
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Cancel the filter so that all RLI records are appears
		// TODO: VOOD-1459
		new VoodooControl("a", "css", ".layout_ForecastWorksheets .select2-search-choice-close").click(); 
		VoodooSelect salesStageCtrl = new VoodooSelect ("i", "css", "span[data-voodoo-name='sales_stage'] .clickToEdit");
		salesStageCtrl.set(roleRecord.get("salesStage1"));

		// Asserting the value of Probability field remains equals to "no access" while editing the salesstage in the Forecasts Module
		VoodooControl forecastProbabilityCtrl = new VoodooControl ("span", "css", "span[data-voodoo-name='probability']");
		forecastProbabilityCtrl.assertAttribute("class", "noaccess", true);
		forecastProbabilityCtrl.assertContains(roleRecord.get("probabilityValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}