package com.sugarcrm.test.RevenueLineItems;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Ignore;
import org.junit.Test;


public class RevenueLineItems_26017 extends SugarTest {
	AccountRecord myAcc;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI1, myRLI2, myRLI3;
	DataSource myDataSource;
	FieldSet myFieldSet;
	
	public void setup() throws Exception {
		sugar().login();

		myDataSource = testData.get(testName);

		myAcc = (AccountRecord)sugar().accounts.api.create();
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();

		FieldSet fs1 = new FieldSet();
		fs1.put("relOpportunityName", myOpp.getRecordIdentifier());
		fs1.put("name", myDataSource.get(0).get("RLI_NAME"));
		fs1.put("likelyCase", myDataSource.get(0).get("RLI_LIKELY"));
		fs1.put("date_closed", myDataSource.get(0).get("RLI_DATE"));
		fs1.put("salesStage", myDataSource.get(0).get("RLI_SALES_STAGE"));
		myRLI1 = (RevLineItemRecord) sugar().revLineItems.create(fs1);

		FieldSet fs2 = new FieldSet();
		fs2.put("name", myDataSource.get(1).get("RLI_NAME"));
		fs2.put("likelyCase", myDataSource.get(1).get("RLI_LIKELY"));
		fs2.put("date_closed", myDataSource.get(1).get("RLI_DATE"));
		fs2.put("salesStage", myDataSource.get(1).get("RLI_SALES_STAGE"));
		myRLI2 = (RevLineItemRecord) sugar().revLineItems.create(fs2);
	}
	
	/** 
	 *Verify that status of the opportunity is changed to Closed Won/Lost if all RLIs linked to 
	 *the opportunity have sales stage "Close Won/Lost" 
	 * 
	 * @throws Exception
	 */
	@Ignore("VOOD-1394")
	@Test
	public void RevenueLineItems_26017_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOpp.navToRecord();
		// Verify that status of the opportunity is "Closed Won"
		sugar().opportunities.recordView.getDetailField("status").assertContains(myDataSource.get(0).get("OPP_STATUS"), true);

		// Change myRLI1 sales stage to "Closed Lost"
		myRLI1.navToRecord();
		// Edit created Revenue Line Item
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(myDataSource.get(1).get("OPP_STATUS"));
		sugar().revLineItems.recordView.save();

		sugar().alerts.waitForLoadingExpiration();

		myOpp.navToRecord();
		// Verify that status of the opportunity is "Closed Won"
		sugar().opportunities.recordView.getDetailField("status").assertContains(myDataSource.get(0).get("OPP_STATUS"), true);

		// Change myRLI2 sales stage to "Closed Lost"
		myRLI2.navToRecord();
		// Edit created Revenue Line Item record
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(myDataSource.get(1).get("OPP_STATUS"));
		sugar().revLineItems.recordView.save();

		sugar().alerts.waitForLoadingExpiration();

		myOpp.navToRecord();
		// Verify that status of the opportunity is changed to "Closed Lost"
		sugar().opportunities.recordView.getDetailField("status").assertContains(myDataSource.get(1).get("OPP_STATUS"), true);

		// Create RLI record with sale stage not equal to "Closed Won" or "Closed Lost"
		FieldSet fs3 = new FieldSet();
		fs3.put("name", myDataSource.get(2).get("RLI_NAME"));
		fs3.put("likelyCase", myDataSource.get(2).get("RLI_LIKELY"));
		fs3.put("date_closed", myDataSource.get(2).get("RLI_DATE"));
		fs3.put("salesStage", myDataSource.get(2).get("RLI_SALES_STAGE"));
		myRLI3 = (RevLineItemRecord) sugar().revLineItems.create(fs3);

		myOpp.navToRecord();
		// Verify that status of the opportunity is "In Progress"
		sugar().opportunities.recordView.getDetailField("status").assertContains(myDataSource.get(2).get("OPP_STATUS"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}