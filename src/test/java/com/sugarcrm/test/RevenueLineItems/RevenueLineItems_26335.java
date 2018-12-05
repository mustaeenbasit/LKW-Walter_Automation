package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.candybean.datasource.DataSource;

public class RevenueLineItems_26335 extends SugarTest {
	AccountRecord myAcc;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI, myRLI1, myRLI2;
	DataSource myDataSource;
	FieldSet myTestData, fs, fs1; 
	
	public void setup() throws Exception {
		sugar().login();
		
		myDataSource = testData.get(testName);
		fs = testData.get(testName+"_sep").get(0); 
		
		// Change thousands separator to "." and decimal separator to ","  in user preferences
		sugar().users.setPrefs(fs);

		// Create an opportunity record
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();

	}
	
	/**
	 * TC 26335: ENT/ULT: Verify that RLI and Opportunity values saved correctly when 
	 * the "1000s separator" and "Decimal Symbol" preferences are altered
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26335_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create Revenue Line Item with sales stage "Closed Lost" 
		FieldSet rliFieldSet1 = new FieldSet();
		rliFieldSet1.put("name", myDataSource.get(0).get("name"));
		rliFieldSet1.put("worstCase", myDataSource.get(0).get("worstCase"));
		rliFieldSet1.put("likelyCase", myDataSource.get(0).get("likelyCase"));
		rliFieldSet1.put("bestCase", myDataSource.get(0).get("bestCase"));
		rliFieldSet1.put("date_closed", myDataSource.get(0).get("date_closed"));

		myRLI1 = (RevLineItemRecord)sugar().revLineItems.create(rliFieldSet1);
		
		// Now goto the first RLI rec
		myRLI1.navToRecord();

		// Verify values on RLI1 Record View
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(myDataSource.get(0).get("worstCase_exp"), true);
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertContains(myDataSource.get(0).get("likelyCase_exp"), true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(myDataSource.get(0).get("bestCase_exp"), true);
		
		// Verify values on Opportunity Record View
		myOpp.navToRecord();
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(myDataSource.get(0).get("oppWorstCase_exp"), true);
		sugar().opportunities.recordView.getDetailField("oppAmount").assertContains(myDataSource.get(0).get("oppLikelyCase_exp"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(myDataSource.get(0).get("oppBestCase_exp"), true);

		// Create Revenue Line Item with sales stage "Closed Lost" 
		FieldSet rliFieldSet2 = new FieldSet();
		rliFieldSet2.put("name", myDataSource.get(1).get("name"));
		rliFieldSet2.put("worstCase", myDataSource.get(1).get("worstCase"));
		rliFieldSet2.put("likelyCase", myDataSource.get(1).get("likelyCase"));
		rliFieldSet2.put("bestCase", myDataSource.get(1).get("bestCase"));
		rliFieldSet2.put("date_closed", myDataSource.get(1).get("date_closed"));
		
		myRLI2 = (RevLineItemRecord)sugar().revLineItems.create(rliFieldSet2);

		// Now goto the second RLI rec
		myRLI2.navToRecord();

		// Verify values on RLI2 Record View
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
