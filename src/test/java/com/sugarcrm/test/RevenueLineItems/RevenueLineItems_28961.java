package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_28961 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}
	/**
	 * Verify user is NOT able to create a RLI record with quantity set to 0.
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_28961_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to RLI list view
		sugar().revLineItems.navToListView();
		FieldSet fs = testData.get(testName).get(0);

		// Creating a RLI record with required fields and quantity as '0' 
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("quantity").set(fs.get("quantity"));
		sugar().revLineItems.createDrawer.save();

		// Asserting the error message in quantity field
		// TODO: VOOD-1292
		new VoodooControl("span", "css", ".error-tooltip.add-on").assertAttribute("data-original-title", fs.get("errorMessage"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}