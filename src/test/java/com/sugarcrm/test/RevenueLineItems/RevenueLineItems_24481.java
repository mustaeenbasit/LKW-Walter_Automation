package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_24481 extends SugarTest {
	
	OpportunityRecord myOpp;
	RevLineItemRecord rli1;
	FieldSet myTestData;
	
	public void setup() throws Exception {
		myTestData = testData.get(testName).get(0);
		
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		rli1 = (RevLineItemRecord)sugar().revLineItems.api.create();

		sugar().login();
	}

	/**
	 * Verify that Best case and Worst case numbers are updated to match RLI Likely amount when changing Sales Stage to "Closed Won"
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_24481_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		rli1.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.showMore();

		sugar().revLineItems.createDrawer.getEditField("worstCase").set(myTestData.get("RLI_WORST"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(myTestData.get("RLI_BEST"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(myTestData.get("RLI_LIKELY"));
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(myTestData.get("RLI_SALES_STAGE_CLOSED_WON"));
		// TODO: VOOD-1110. Jenkins is not able to store date from date picker.
		// sugar().revLineItems.createDrawer.getEditField("date_closed").set(myTestData.get("RLI_CLOSEDATE"));
	
		// Verify that Worst and Best amounts are now equal to Likely amount 
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(myTestData.get("RLI_LIKELY"), true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(myTestData.get("RLI_LIKELY"), true);
		
		// Verify that Best and Worst fields are read-only
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertAttribute("disabled","true");
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertAttribute("disabled","true");
		
		// Change Likely amount
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(myTestData.get("RLI_LIKELY_NEW"));
		sugar().revLineItems.createDrawer.getEditField("description").set(myTestData.get("RLI_QUANTITY"));
		
		// Verify that Worst and Best amounts are updated to match Likely amount 
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(myTestData.get("RLI_LIKELY_NEW"), true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(myTestData.get("RLI_LIKELY_NEW"), true);
		
		// Set relation to Opportunity record  
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