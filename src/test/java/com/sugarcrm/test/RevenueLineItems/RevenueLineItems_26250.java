package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26250 extends SugarTest {
	AccountRecord myAccount;
	OpportunityRecord myOpp;
	DataSource myDataSource; 
	FieldSet myTestData;
	
	public void setup() throws Exception {
		sugar().login();
		myDataSource = testData.get("RevenueLineItems_26250");
	}

	/**
	 * TC 26250 - ENT/ULT: Verify that input validation is functional for quantity, likely, best and worst fields on RLI module
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26250_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		
		for(FieldSet myTestData : myDataSource) { 
			sugar().revLineItems.listView.create();
			sugar().revLineItems.createDrawer.showMore();
			
			// Set Likely, Best, Worst, & Quantity fields 
			sugar().revLineItems.createDrawer.getEditField("quantity").set(myTestData.get("input"));
			sugar().revLineItems.createDrawer.getEditField("unitPrice").set(myTestData.get("input"));
			sugar().revLineItems.createDrawer.getEditField("likelyCase").set(myTestData.get("input"));
			sugar().revLineItems.createDrawer.getEditField("bestCase").set(myTestData.get("input"));
			sugar().revLineItems.createDrawer.getEditField("worstCase").set(myTestData.get("input"));
			
			 			
			 sugar().revLineItems.createDrawer.save();
			 
			// Verify that "i" icon appears on the screen and correct error message is presented when hover over it
			// Used VoodooControl() API cause the standard definition of fields in "RevLineItemsModuleFields.csv" 
			// doesn't  allow access to "span" & "i" elements in case of incorrect input.  

			// Likely Field
			new VoodooControl("i","css",".fld_likely_case.edit input[name=likely_case] + span i").assertVisible(true);
			new VoodooControl("span","css",".fld_likely_case.edit input[name=likely_case] + span").assertAttribute("data-original-title", myTestData.get("input_err"), true);
						 
			// Best Field
			new VoodooControl("i","css",".fld_best_case.edit input[name=best_case] + span i").assertVisible(true);
			new VoodooControl("span","css",".fld_best_case.edit input[name=best_case] + span").assertAttribute("data-original-title", myTestData.get("input_err"), true);

			// Worst Field
			new VoodooControl("i","css",".fld_worst_case.edit input[name=worst_case] + span i").assertVisible(true);
			new VoodooControl("span","css",".fld_worst_case.edit input[name=worst_case] + span").assertAttribute("data-original-title", myTestData.get("input_err"), true);

			// Quantity Field
			new VoodooControl("i","css",".fld_quantity.edit input[name=quantity] + span i").assertVisible(true);
			new VoodooControl("span","css",".fld_quantity.edit input[name=quantity] + span").assertAttribute("data-original-title", myTestData.get("input_err"), true);
			
			// Dismiss error message 
			sugar().alerts.getAlert().closeAlert();
			 
			sugar().revLineItems.createDrawer.showLess();
				
			// Cancel RLI creation process
			sugar().revLineItems.createDrawer.cancel();
		}
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}