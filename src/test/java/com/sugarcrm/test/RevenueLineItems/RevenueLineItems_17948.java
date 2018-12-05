package com.sugarcrm.test.RevenueLineItems;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17948 extends SugarTest {
	OpportunityRecord myOpp;
	FieldSet myRLIData;

	public void setup() throws Exception {
		myRLIData = sugar().revLineItems.getDefaultData();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();

		// Login as Admin
		sugar().login();
	}

	/**
	 * Test Case 17948: Verify Revenue Line Item can't be saved if opportunity field is empty
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17948_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.showMore();
		// Set all fields
		sugar().revLineItems.recordView.setFields(myRLIData);
		// but clear related Opportunity
		// TODO: VOOD-806
		new VoodooControl("a", "css", ".fld_opportunity_name.edit abbr").click();
		sugar().revLineItems.createDrawer.save();

		// Verify that record is not saved and warning message appears
		sugar().alerts.getError().closeAlert();
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").waitForVisible();

		// Set relationship to opportunity record and save
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.createDrawer.save();

		// Navigate to the RLI record
		sugar().revLineItems.listView.clickRecord(1);

		// Verify that RLI is correctly saved
		sugar().revLineItems.recordView.getDetailField("name").assertContains(myRLIData.get("name"), true);
		sugar().revLineItems.recordView.getDetailField("relOpportunityName").assertContains(myOpp.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}