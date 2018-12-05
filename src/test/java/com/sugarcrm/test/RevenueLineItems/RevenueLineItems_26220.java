package com.sugarcrm.test.RevenueLineItems;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class RevenueLineItems_26220 extends SugarTest {	
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	FieldSet myTestData;
	
	public void setup() throws Exception {
		myTestData = testData.get(testName).get(0);

		FieldSet fs =  new FieldSet();
		fs.put("name", myTestData.get("name"));

		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord)sugar().revLineItems.api.create(fs);
		
		sugar().login();
	}

	/**
	 * TC 26220 -  ENT/ULT: Verify that Copy action for Revenue Lie Item record works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26220_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Edit RLI record 
		myRLI.navToRecord();
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(myTestData.get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(myTestData.get("unitPrice"));
		sugar().revLineItems.createDrawer.getEditField("quantity").set(myTestData.get("quantity"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(myTestData.get("likelyCase")); // make Likely amount different from calculated RLI amount
		sugar().revLineItems.createDrawer.getEditField("name").click(); // need to commit data entered in previous line.

		// Verify that Calculated RLI amount is calculated correctly
		sugar().revLineItems.createDrawer.getEditField("calcRLIAmount").assertContains(myTestData.get("calcRLIAmount"), true);
		sugar().revLineItems.recordView.save();
		
		sugar().alerts.waitForLoadingExpiration();

		// Copy RLI record
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("copyButton").click();
		
		// Verify Calculated RLI amount for new RLI record
		//TODO: Change this to use library support when create drawer can handle detail mode fields on the background.   
		new VoodooControl("div", "css", "#drawers span[data-fieldname='total_amount'] .fld_total_amount.detail .currency-field")
			.assertContains(myTestData.get("calcRLIAmount"), true);
		
		// Verify Likely Case for new RLI record is equal to original likely amount
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertContains(myTestData.get("likelyCase"), true);
		
		// Provide unique name for new record created through copy and Save. 
		sugar().revLineItems.createDrawer.getEditField("name").set(myTestData.get("new_name"));
		// Save
		sugar().revLineItems.createDrawer.save();

	    // Verify new record exists on the list view 
		sugar().revLineItems.navToListView();

		sugar().revLineItems.listView.verifyField(1, "name", myTestData.get("new_name"));
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
