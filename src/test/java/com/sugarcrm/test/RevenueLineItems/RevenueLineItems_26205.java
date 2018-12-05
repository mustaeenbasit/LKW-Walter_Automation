package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26205 extends SugarTest {
	
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	FieldSet myTestData;
	
	public void setup() throws Exception {
		myTestData = testData.get(testName).get(0);
		
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		
		// Create Revenue Line Item with sales stage "Closed Lost" 
		FieldSet rliFieldSet = new FieldSet();
		rliFieldSet.put("name", myTestData.get("RLI_NAME"));
		rliFieldSet.put("likelyCase", myTestData.get("RLI_LIKELY"));
		// TODO: VOOD-1110. Jenkins is not able to store date from date picker.
		// rliFieldSet.put("date_closed", myTestData.get("RLI_CLOSE_DATE"));
		rliFieldSet.put("salesStage", myTestData.get("RLI_SALES_STAGE_CLOSED_LOST"));
		myRLI = (RevLineItemRecord)sugar().revLineItems.api.create(rliFieldSet);

		sugar().login();
	}

	/**
	 * Verify that Opportunity cannot be deleted in the record view if sales stage of one or more RLIs is "Closed Lost"
	 * 
	 * @author Alex Nisevich
	 */
	@Test
	public void RevenueLineItems_26205_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		// Link RLI to Opportunity
		FieldSet fs1 =  new FieldSet();
		fs1.put("relOpportunityName", myOpp.getRecordIdentifier());
		myRLI.edit(fs1);

		// Verify that Delete menu in the Revenue Line Items record view is disabled
		myRLI.navToRecord();
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("deleteButton").assertAttribute("class", "rowaction disabled");
				
		// Verify that Delete menu in the opportunity record view is disabled 
		myOpp.navToRecord();
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		sugar().opportunities.recordView.getControl("deleteButton").assertAttribute("class", "rowaction disabled");
				
		// Change sales stage of RLI record to not-closed one.
		FieldSet fs = new FieldSet();
		fs.put("salesStage", myTestData.get("RLI_SALES_STAGE_NOT_CLOSED"));
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.updateRecord(1, fs);
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that Delete menu in the Revenue Line Items record view is enabled
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.waitForVisible();
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("deleteButton").assertAttribute("class", "rowaction");

		// Verify that Delete menu in the opportunity record view is enabled 
		myOpp.navToRecord();
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		sugar().opportunities.recordView.getControl("deleteButton").assertAttribute("class", "rowaction");
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}