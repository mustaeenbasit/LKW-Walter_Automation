package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.candybean.datasource.DataSource;

public class RevenueLineItems_26341 extends SugarTest {
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI1, myRLI2;
	DataSource myDataSource;
	FieldSet myTestData, sigDigits, fs = new FieldSet(); 
	
	public void setup() throws Exception {
		sugar().login();
		myDataSource = testData.get(testName);
		sigDigits = testData.get(testName+"_sep").get(0); 
		// Change Currency Significant Digits number
		fs.put("advanced_significant_digits", sigDigits.get("sigDigits_custom"));
		sugar().users.setPrefs(fs);
		
		// Create an opportunity record
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
	}
	
	/**
	 * TC 26341: ENT/ULT: Verify that RLI and Opportunity values saved correctly when currency significant 
	 * digits number is changed in user preferences
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26341_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create Revenue Line Item1 with sales stage "Closed Lost" 
		FieldSet rliFieldSet1 = new FieldSet();
		rliFieldSet1.put("name", myDataSource.get(0).get("name"));
		rliFieldSet1.put("worstCase", myDataSource.get(0).get("worstCase"));
		rliFieldSet1.put("likelyCase", myDataSource.get(0).get("likelyCase"));
		rliFieldSet1.put("bestCase", myDataSource.get(0).get("bestCase"));
		// TODO: VOOD-1110. Jenkins is not able to store date from date picker.
		// rliFieldSet1.put("date_closed", myDataSource.get(0).get("date_closed"));
		myRLI1 = (RevLineItemRecord)sugar().revLineItems.api.create(rliFieldSet1);
		
		// Link RLI1 to Opportunity
		FieldSet fs =  new FieldSet();
		fs.put("relOpportunityName", myOpp.getRecordIdentifier());
		myRLI1.edit(fs);
		
		// Verify values on RLI Record View
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(myDataSource.get(0).get("worstCase_exp"), true);
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertContains(myDataSource.get(0).get("likelyCase_exp"), true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(myDataSource.get(0).get("bestCase_exp"), true);
		
		// Verify values on Opportunity Record View
		myOpp.navToRecord();
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(myDataSource.get(0).get("oppWorstCase_exp"), true);
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(myDataSource.get(0).get("oppLikelyCase_exp"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(myDataSource.get(0).get("oppBestCase_exp"), true);

		// Create Revenue Line Item2 with sales stage "Closed Lost" 
		FieldSet rliFieldSet2 = new FieldSet();
		rliFieldSet2.put("name", myDataSource.get(1).get("name"));
		rliFieldSet2.put("worstCase", myDataSource.get(1).get("worstCase"));
		rliFieldSet2.put("likelyCase", myDataSource.get(1).get("likelyCase"));
		rliFieldSet2.put("bestCase", myDataSource.get(1).get("bestCase"));
		// TODO: VOOD-1110. Jenkins is not able to store date from date picker.
		// rliFieldSet2.put("date_closed", myDataSource.get(1).get("date_closed"));
		myRLI2 = (RevLineItemRecord)sugar().revLineItems.api.create(rliFieldSet2);
		
		// Link RLI2 to Opportunity
		fs =  new FieldSet();
		fs.put("relOpportunityName", myOpp.getRecordIdentifier());
		myRLI2.edit(fs);
		
		// Verify values on RLI Record View
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(myDataSource.get(1).get("worstCase_exp"), true);
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertContains(myDataSource.get(1).get("likelyCase_exp"), true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(myDataSource.get(1).get("bestCase_exp"), true);
		
		// Verify values on Opportunity Record View
		myOpp.navToRecord();
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(myDataSource.get(1).get("oppWorstCase_exp"), true);
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(myDataSource.get(1).get("oppLikelyCase_exp"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(myDataSource.get(1).get("oppBestCase_exp"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
