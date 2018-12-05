package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class RevenueLineItems_26647 extends SugarTest {
	AccountRecord myAcc;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI1, myRLI2;
	FieldSet updateFS1 = new FieldSet(), updateFS2 = new FieldSet(), fullFS1 = new FieldSet(), fullFS2 = new FieldSet();

	public void setup() throws Exception {
		updateFS1 = testData.get(testName).get(0);
		updateFS2 = testData.get(testName).get(1);
		fullFS1 = testData.get(testName + "_1").get(0);
		fullFS2 = testData.get(testName + "_1").get(1);

		// Create Opportunity record 
		myOpp = (OpportunityRecord) sugar().opportunities.api. create();

		// Create 2 RLI records
		myRLI1 = (RevLineItemRecord) sugar().revLineItems.api.create();
		myRLI2 = (RevLineItemRecord) sugar().revLineItems.api.create();

		sugar().login();

		// TODO: below initialization should be removed once VOOD-928 is fixed
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// TODO: VOOD-1666 NavTo RLI after Faorecast setup fails.
		// Go to admin page(//Hack: Direct navigation to opp module is not working so using this to change the focus)
		sugar().navbar.navToAdminTools();

		// Edit RLIs - set name and add the related opportunity
		myRLI1.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("name").set(fullFS1.get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.recordView.save();

		myRLI2.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("name").set(fullFS2.get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.recordView.save();
	}

	/**
	 * Test Case 26647: Verify Sugar Logic on inline edits for Revenue Line Items
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26647_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to RLIs listview, inline edit Sales Stage and verify the Probability and Forecast are changing respectively
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.updateRecord(1, updateFS1);
		sugar().revLineItems.listView.verifyField(1, "salesStage", (fullFS1.get("salesStage")));
		sugar().revLineItems.listView.verifyField(1, "probability", (fullFS1.get("probability")));
		sugar().revLineItems.listView.verifyField(1, "forecast", (fullFS1.get("forecast")));

		sugar().revLineItems.listView.updateRecord(2, updateFS2);
		sugar().revLineItems.listView.verifyField(2, "salesStage", fullFS2.get("salesStage"));
		sugar().revLineItems.listView.verifyField(2, "probability", fullFS2.get("probability"));
		sugar().revLineItems.listView.verifyField(2, "forecast", fullFS2.get("forecast"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}