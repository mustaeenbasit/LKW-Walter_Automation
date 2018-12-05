package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26204 extends SugarTest {
	
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	FieldSet myTestData;
	
	public void setup() throws Exception {
		myTestData = testData.get(testName).get(0);
		
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();

		// Create Revenue Line Item 
		FieldSet rliFieldSet = new FieldSet();
		rliFieldSet.put("name", myTestData.get("RLI_NAME"));
		rliFieldSet.put("likelyCase", myTestData.get("RLI_LIKELY"));
		// TODO: VOOD-1110. Jenkins is not able to store date from date picker.
		// rliFieldSet.put("date_closed", myTestData.get("RLI_CLOSE_DATE"));
		rliFieldSet.put("salesStage", myTestData.get("RLI_SALES_STAGE_CLOSED_WON"));

		myRLI = (RevLineItemRecord)sugar().revLineItems.api.create(rliFieldSet);
				
		sugar().login();
	}

	/**
	 * TC 206204: ENT/ULT: Verify that RLI cannot be deleted in the record view if sales stage of the RLI is "Closed Won"
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26204_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link RLI to Opportunity
		FieldSet fs =  new FieldSet();
		fs.put("relOpportunityName", myOpp.getRecordIdentifier());
		myRLI.edit(fs);
		
		// Verify that Delete menu in the Revenue Line Items record view is disabled
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("deleteButton").assertAttribute("class", "rowaction disabled");
								
		// Change sales stage to not-closed one.
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(myTestData.get("RLI_SALES_STAGE_NOT_CLOSED"));
		sugar().revLineItems.recordView.save();
		
		// Verify that Delete menu in the Revenue Line Items record view is enabled
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("deleteButton").assertAttribute("class", "rowaction");
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}