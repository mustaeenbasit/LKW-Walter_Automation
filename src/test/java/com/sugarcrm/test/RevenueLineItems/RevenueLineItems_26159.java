package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;

public class RevenueLineItems_26159 extends SugarTest {
	FieldSet myTestData;
	OpportunityRecord myOpp;
	RevLineItemRecord myRevenueLineItem;
	
	public void setup() throws Exception {
		myTestData = testData.get(testName).get(0);
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();

		sugar().login();
		myRevenueLineItem = (RevLineItemRecord) sugar().revLineItems.create();
	}

	/**
	 * Verify that the in line edit works for Revenue Line Items List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26159_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		
		// Inline Edit the record and save
		sugar().revLineItems.listView.updateRecord(1, myTestData);
		// Verify Record updated
		sugar().revLineItems.listView.verifyField(1, "name", myTestData.get("name"));
		sugar().revLineItems.listView.verifyField(1, "salesStage", myTestData.get("salesStage"));
		sugar().revLineItems.listView.verifyField(1, "likelyCase", "$"+ myTestData.get("likelyCase")+"");

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}