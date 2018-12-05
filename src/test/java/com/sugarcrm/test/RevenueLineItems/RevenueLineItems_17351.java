package com.sugarcrm.test.RevenueLineItems;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17351 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that if forecasts module has not been set up, the Forecast field should not be shown in revenue Line Items edit/detail views.
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17351_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to revenue line items and click "Create"
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Verify that the Forecast dropdown should not be present in the edit view
		sugar().revLineItems.createDrawer.getEditField("forecast").assertExists(false);

		// Fill out all required fields and click on Save and view
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testName.substring(17));
		sugar().revLineItems.createDrawer.save();

		// Navigate to RLI record
		sugar().revLineItems.listView.clickRecord(1);

		// Verify that the Forecast field should not be present in the detail view
		sugar().revLineItems.recordView.getDetailField("forecast").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}