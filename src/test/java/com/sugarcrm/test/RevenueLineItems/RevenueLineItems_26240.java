package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Alex Nisevich <anisevich@sugarcrm.com>
 */
public class RevenueLineItems_26240 extends SugarTest {

	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	FieldSet myTestData = new FieldSet();
	DataSource myDataSource = new DataSource();

	public void setup() throws Exception {
		myDataSource = testData.get(testName);
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord)sugar().revLineItems.api.create();

		sugar().login();
	}

	/**
	 * TC 26240: ENT/ULT: Verify that probability is updated when sales stage is changed on the RLI record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26240_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link RLI to Opportunity
		FieldSet fs =  new FieldSet();
		fs.put("relOpportunityName", myOpp.getRecordIdentifier());
		myRLI.edit(fs);
		VoodooUtils.waitForReady();

		// Already on RLI recordview
		// Hover over pencil icon of Sales Stage control
		sugar().revLineItems.recordView.getDetailField("salesStage").hover();
		
		// Click on the pencil icon of Sales Stage
		new VoodooControl("span","css","span[data-name='sales_stage'] i.fa-pencil").click();
		VoodooUtils.waitForReady();
		
		// Set Sales Stage Once to setup DropDown field for Looping
		new VoodooControl("input", "css", "div#select2-drop div input").set(myDataSource.get(0).get("salesStage"));
		new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[contains(text(), '" + myDataSource.get(0).get("salesStage") + "')]").click();
		VoodooUtils.waitForReady();
		
		for(FieldSet myTestData : myDataSource) { 
			
			// Click on Sales Stage to open drop down
			new VoodooControl("span","css",".fld_sales_stage div").click();
			
			// Set Sales Stage
			new VoodooControl("input", "css", "div#select2-drop div input").set(myTestData.get("salesStage"));
			new VoodooControl("span", "XPATH", "/html//div[@id='select2-drop']//*[contains(text(), '" + myTestData.get("salesStage") + "')]").click();
			VoodooUtils.waitForReady();
			
			// Verify Probability is updated based on selected sales stage
			sugar().revLineItems.recordView.getDetailField("probability").assertContains(myTestData.get("probability"), true);
		}

		sugar().revLineItems.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}