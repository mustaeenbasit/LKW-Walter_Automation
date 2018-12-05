package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class RevenueLineItems_18755 extends SugarTest {
	AccountRecord myAcc;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	DataSource testDS;

	public void setup() throws Exception {
		sugar().login();
		testDS = testData.get(testName);
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord) sugar().revLineItems.api.create();

		// Edit RLI - set name, unit price and add the related opportunity
		myRLI.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("name").set(testDS.get(0).get("name"));
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set("");
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(testDS.get(0).get("unitPrice"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Test Case 18755: ENT/ULT Verify that value for "Unit Price" field in newly generated quote is taken from "Unit Price"
	 * field in RLI in case the field has some value
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18755_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select Generate Quote action
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("generateQuote").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Unit Price is populate with the value from the RLI's unit price TODO: VOOD-930
		new VoodooControl("input", "id", "discount_price_1").assertContains(testDS.get(0).get("unitPrice"), true);
		VoodooUtils.focusDefault();
		sugar().quotes.editView.cancel();
		sugar().alerts.waitForLoadingExpiration();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}