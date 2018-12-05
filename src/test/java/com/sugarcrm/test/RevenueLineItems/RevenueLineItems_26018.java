package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class RevenueLineItems_26018 extends SugarTest {
	AccountRecord myAcc;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI1, myRLI2, myRLI3;
	DataSource testDS;

	public void setup() throws Exception {
		sugar().login();

		testDS = testData.get(testName);
		myAcc = (AccountRecord) sugar().accounts.api.create();
		myOpp = (OpportunityRecord) sugar().opportunities.create();
		myRLI1 = (RevLineItemRecord) sugar().revLineItems.api.create();
		myRLI2 = (RevLineItemRecord) sugar().revLineItems.api.create();
		myRLI3 = (RevLineItemRecord) sugar().revLineItems.api.create();
	}

	/**
	 * Test Case 26018: Verify that Best and Worst amounts made read-only and equal to likely when closed won/lost sales stage is selected
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26018_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl bestCaseField = new VoodooControl("input", "css", "span.fld_best_case input");
		VoodooControl worstCaseField = new VoodooControl("input", "css", "span.fld_worst_case input");

		// Edit all RLIs - set different names, likely/best/worst values and add the related opportunity
		myRLI1.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.createDrawer.getEditField("name").set(testDS.get(0).get("name"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testDS.get(0).get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(testDS.get(0).get("bestCase"));
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(testDS.get(0).get("worstCase"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());

		// Change sales stage to closed status (Closed Won) and verify the Best Case and Worst Case fields are read-only and equal to likely value
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(testDS.get(0).get("salesStage"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(testDS.get(0).get("likelyCase"), true);
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(testDS.get(0).get("likelyCase"), true);
		assertTrue("Should be disabled", (Boolean.parseBoolean(bestCaseField.getAttribute("disabled"))));
		assertTrue("Should be disabled", (Boolean.parseBoolean(worstCaseField.getAttribute("disabled"))));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Repeat the same steps for second RLI using other closed sales stage (Closed Lost)
		myRLI2.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.createDrawer.getEditField("name").set(testDS.get(1).get("name"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testDS.get(1).get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(testDS.get(1).get("bestCase"));
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(testDS.get(1).get("worstCase"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());

		// Change sales stage and verify the Best Case and Worst Case fields are read-only and equal to likely value
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(testDS.get(1).get("salesStage"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(testDS.get(1).get("likelyCase"), true);
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(testDS.get(1).get("likelyCase"), true);
		// TODO: VOOD-1020 - verification of the text fields read-only status should be changed when story is implemented
		assertTrue("Should be disabled", (Boolean.parseBoolean(bestCaseField.getAttribute("disabled"))));
		assertTrue("Should be disabled", (Boolean.parseBoolean(worstCaseField.getAttribute("disabled"))));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Repeat the same for third RLI using not closed sales stage (Qualification)
		myRLI3.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.createDrawer.getEditField("name").set(testDS.get(2).get("name"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(testDS.get(2).get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(testDS.get(2).get("bestCase"));
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(testDS.get(2).get("worstCase"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());

		// Change sales stage and verify the Best Case and Worst Case fields are still editable and still contains their own values
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(testDS.get(2).get("salesStage"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(testDS.get(2).get("likelyCase"), false);
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(testDS.get(2).get("likelyCase"), false);
		// TODO: VOOD-1020 - verification of the text fields read-only status should be changed when story is implemented
		assertTrue("Shouldn't be disabled", (!Boolean.parseBoolean(bestCaseField.getAttribute("disabled"))));
		assertTrue("Shouldn't be disabled", (!Boolean.parseBoolean(worstCaseField.getAttribute("disabled"))));
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}