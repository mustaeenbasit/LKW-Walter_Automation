package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_28923 extends SugarTest {

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}
	/**
	 * Verify that "Assigned To" field is appearing in RLI record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_28923_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go RLI listView and Create an RLI and save it.
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.save();

		// Open newly created RLI.
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();

		// TODO: VOOD-1931
		// Verify that One of the fields should be "Assigned To".
		FieldSet customFS = testData.get(testName).get(0);
		new VoodooControl("div", "css", "div[data-name='assigned_user_name'] .record-label").assertEquals(customFS.get("assignedTo"), true);
		sugar().revLineItems.recordView.getDetailField("relAssignedTo").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}