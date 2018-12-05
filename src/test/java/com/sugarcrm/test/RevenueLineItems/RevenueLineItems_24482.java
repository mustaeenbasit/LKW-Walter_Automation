package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_24482 extends SugarTest {
	OpportunityRecord myOpp;
	RevLineItemRecord rli1;
	FieldSet myTestData;
	
	public void setup() throws Exception {
		sugar().login();
		myTestData = testData.get(testName).get(0);
		
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		rli1 = (RevLineItemRecord)sugar().revLineItems.api.create();
	}

	/**
	 * Verify that best and worst case numbers are not read-only and can be edited individually 
	 * when sales stage changes from "Closed Won" to any other not-closed one
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_24482_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		rli1.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.showMore();

		sugar().revLineItems.createDrawer.getEditField("name").set(myTestData.get("RLI_NAME"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(myTestData.get("RLI_WORST"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(myTestData.get("RLI_BEST"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(myTestData.get("RLI_LIKELY"));
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(myTestData.get("RLI_SALES_STAGE_CLOSED_WON"));
	
		sugar().revLineItems.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that Worst and Best amounts are equal to Likely amount in the record view
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(myTestData.get("RLI_LIKELY"), true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(myTestData.get("RLI_LIKELY"), true);
				
		// Edit created Revenue Line Item
		sugar().revLineItems.recordView.edit();
		
		// Change Sales Stage to any but not closed sales stage
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(myTestData.get("RLI_SALES_STAGE"));
		
		// Set values for worst and best amounts that are different from likely
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(myTestData.get("RLI_WORST"));
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(myTestData.get("RLI_BEST"));
		sugar().revLineItems.createDrawer.getEditField("description").set(myTestData.get("RLI_QUANTITY"));
		
		// Verify that Worst and Best amounts are different from Likely in the edit view  
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(myTestData.get("RLI_WORST"), true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(myTestData.get("RLI_BEST"), true);

		sugar().revLineItems.recordView.save();
		
		// Verify that Worst and Best amounts are different from Likely in the record view
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(myTestData.get("RLI_WORST"), true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(myTestData.get("RLI_BEST"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}