package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26201 extends SugarTest {
	
	OpportunityRecord myOpp;
	RevLineItemRecord rli1;
	FieldSet myTestData;
	
	public void setup() throws Exception {
		myTestData = testData.get(testName).get(0);

		// Create Revenue Line Item: rli1 
		FieldSet data_rli1 = new FieldSet();
		data_rli1.put("name", myTestData.get("RLI_NAME"));
		data_rli1.put("worstCase", myTestData.get("RLI_WORST"));
		data_rli1.put("likelyCase", myTestData.get("RLI_LIKELY"));
		data_rli1.put("bestCase", myTestData.get("RLI_BEST"));
		// TODO: VOOD-1110. Jenkins is not able to store date from date picker.
		// data_rli1.put("date_closed", myTestData.get("RLI_CLOSEDATE"));
		
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		rli1 = (RevLineItemRecord)sugar().revLineItems.api.create(data_rli1);

		sugar().login();
	}

	/**
	 * Verify that Best case and Worst case numbers are updated to match RLI Likely amount when changing Sales Stage to "Closed Lost"
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26201_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		rli1.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.showMore();
		
		// Set sales stage to "Closed Lost"
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(myTestData.get("RLI_SALES_STAGE_CLOSED_LOST"));
		VoodooUtils.pause(1000);
		
		// Verify that Worst and Best amounts are equal to Likely amount
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(myTestData.get("RLI_LIKELY"), true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(myTestData.get("RLI_LIKELY"), true);
		
		// Change Likely amount
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(myTestData.get("RLI_LIKELY_NEW"));
		sugar().revLineItems.createDrawer.getEditField("description").set(myTestData.get("RLI_QUANTITY"));
		
		// Verify that Worst and Best amounts are equal to Likely 
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(myTestData.get("RLI_LIKELY_NEW"), true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(myTestData.get("RLI_LIKELY_NEW"), true);
		
		// Fill out the rest of the required fields 
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that Worst and Best amounts are equal to Likely amount in the record view
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(myTestData.get("RLI_LIKELY_NEW"), true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(myTestData.get("RLI_LIKELY_NEW"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}