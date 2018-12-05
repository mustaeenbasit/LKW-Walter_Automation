package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26249 extends SugarTest {
	AccountRecord myAccount;
	OpportunityRecord myOpp;
	FieldSet myTestData;
	
	public void setup() throws Exception {
		sugar().login();
		myTestData = testData.get("RevenueLineItems_26249").get(0);
	}
	
	/**
	 * TC 26249 - ENT/ULT: Verify that RLI name, opportunity name, expected close date, and Likely are required fields
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26249_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.save();
			 
		// Used VoodooControl() API cause the standard definition of fields in "RevLineItemsModuleFields.csv" 
		// doesn't  allow access to "span" & "i" elements in case the required field left blank.   
		// Verify that "i" icon is visible and "name" is required field  
		new VoodooControl("i","css",".fld_name.edit span i").assertVisible(true);
		new VoodooControl("span","css",".fld_name.edit span").assertAttribute("data-original-title", myTestData.get("req_msg_long"), true);
		sugar().revLineItems.createDrawer.getEditField("name").assertAttribute("placeholder",myTestData.get("req_msg_short"), true);
				
		// Verify that "i" icon is visible and "Expected Close Date" is required field  
		new VoodooControl("i","css",".fld_date_closed.edit span i").assertVisible(true);
		new VoodooControl("span","css",".fld_date_closed.edit span").assertAttribute("data-original-title", myTestData.get("req_msg_long"), true);
		sugar().revLineItems.createDrawer.getEditField("date_closed").assertAttribute("placeholder",myTestData.get("req_msg_specialCase"), true);
						
		// Verify that "Opportunity Name" is required field
		new VoodooControl("i","css",".fld_opportunity_name.edit span i").assertVisible(true);
		new VoodooControl("span","css",".fld_opportunity_name.edit .error-tooltip.add-on").assertAttribute("data-original-title", myTestData.get("req_msg_long"), true);
		new VoodooControl("span","css",".fld_opportunity_name.edit input[name=opportunity_name]").assertAttribute("placeholder",myTestData.get("req_msg_short"), true);
		
		// Dismiss Error Message
		sugar().alerts.getError().closeAlert();
		
		// Cancel RLI creation process
		sugar().revLineItems.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}