package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_28314 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().revLineItems.api.create();
		customData = testData.get(testName).get(0);
		sugar().login();
		// Enable Forecasts
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		VoodooUtils.waitForReady();
		sugar().forecasts.setup.saveSettings();
		
		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();
		
		// Link Opp to an account
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.editRecord(1);
		sugar().opportunities.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.listView.saveRecord(1);
	}	

	/**
	 * Verify that Worst and Best fields can accept "0" when edit RLI record  
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_28314_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();
		// Click Edit button to edit existing RLI record 
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		// Update the “Worst” (or Best) to 0 in “Revenue Line Items
		sugar().revLineItems.recordView.getEditField("worstCase").set(customData.get("data"));

		// Click on “Save”
		sugar().revLineItems.recordView.save();

		// Verify The “Worst”(or "Best") fields are saved with 0.
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(customData.get("data"), true);
		
		// Click Edit button to edit existing RLI record 
		sugar().revLineItems.recordView.edit();
		// Update the “Best” to 0 in “Revenue Line Items
		sugar().revLineItems.recordView.getEditField("bestCase").set(customData.get("data"));

		// Click on “Save”
		sugar().revLineItems.recordView.save();

		// Verify The "Best" fields are saved with 0.
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(customData.get("data"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}