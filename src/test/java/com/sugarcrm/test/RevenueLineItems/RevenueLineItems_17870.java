package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;


import com.sugarcrm.test.SugarTest;

/**
 * @author Alex Nisevich <anisevich@sugarcrm.com>
 */
public class RevenueLineItems_17870 extends SugarTest {
	OpportunityRecord myOpp;
	RevLineItemRecord rli1, rli2;
	FieldSet myTestData;

	public void setup() throws Exception {
		myTestData = testData.get(testName).get(0);
		
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		rli1 = (RevLineItemRecord)sugar().revLineItems.api.create();
		rli2 = (RevLineItemRecord)sugar().revLineItems.api.create();

		sugar().login();
	}
	/** 
	 * Test Case 17870:
	 * Verify that an opportunity amount and expected close date are updated after 
	 * adding/removing revenue line items linked to the above opportunity
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17870_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create First Revenue Line Item: rli1 and Link RLI to Opportunity 
		FieldSet data_rli1 = new FieldSet();
		data_rli1.put("relOpportunityName", myOpp.getRecordIdentifier());
		data_rli1.put("name", myTestData.get("RLI1_NAME"));
		data_rli1.put("worstCase", myTestData.get("RLI1_WORST"));
		data_rli1.put("likelyCase", myTestData.get("RLI1_LIKELY"));
		data_rli1.put("bestCase", myTestData.get("RLI1_BEST"));
		data_rli1.put("date_closed",myTestData.get("RLI1_DATE"));
		rli1.edit(data_rli1);
		
		// Create Second  Revenue Line Item: rli2 and Link RLI to Opportunity
		FieldSet data_rli2 = new FieldSet();
		data_rli2.put("relOpportunityName", myOpp.getRecordIdentifier());
		data_rli2.put("name", myTestData.get("RLI2_NAME"));
		data_rli2.put("worstCase", myTestData.get("RLI2_WORST"));
		data_rli2.put("likelyCase",myTestData.get("RLI2_LIKELY"));
		data_rli2.put("bestCase", myTestData.get("RLI2_BEST"));
		data_rli2.put("date_closed", myTestData.get("RLI2_DATE"));
		rli2.edit(data_rli2);

		myOpp.navToRecord();
		
		/*
		 * Verification Part 1: Verify that amounts and expected close date on the opportunity record view
		 * are calculated correctly after creating 2 revenue line items linked to the above opportunity
		 */
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(myTestData.get("OPP_WORST"), true);
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(myTestData.get("OPP_LIKELY"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(myTestData.get("OPP_BEST"), true);
		sugar().opportunities.recordView.getDetailField("date_closed").assertContains(myTestData.get("OPP_DATE"),true);
		
		//Delete first revenue line item: rli1 
		rli1.delete();
		sugar().alerts.waitForLoadingExpiration();
		
		myOpp.navToRecord();

		/*
		 * Verification Part 2: Verify that amounts and expected close date on the opportunity record view 
		 * are updated correctly after one revenue line item - rli1 - is deleted
		 */
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(myTestData.get("RLI2_WORST"), true);
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(myTestData.get("RLI2_LIKELY"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(myTestData.get("RLI2_BEST"), true);
		sugar().opportunities.recordView.getDetailField("date_closed").assertContains(myTestData.get("RLI2_DATE"),true);
		
		//Delete second revenue line item: rli2
		rli2.delete();
		sugar().alerts.waitForLoadingExpiration();
		myOpp.navToRecord();

		/*
		 * Verification Part 3: Verify that amounts and expected close date on the opportunity record view 
		 * are updated correctly after one revenue line item - rli1 - is deleted
		 */
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(myTestData.get("zeroValue"), true);
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(myTestData.get("zeroValue"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(myTestData.get("zeroValue"), true);
		sugar().opportunities.recordView.getDetailField("date_closed").assertContains("", true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}