package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_25408 extends SugarTest {
	AccountRecord myAccount;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	
	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
		
		// Used UI because of lack of relationship setting functionality in Opportunity Create API
		// TODO: VOOD-444 Update API creates to be able to establish relationships
		myOpp = (OpportunityRecord)sugar().opportunities.create();
	}

	/**
	 * ENT/ULT: Verify that record in opportunity sub-panel is updated when corresponding 
	 * 			record in RLI sub-panel is changed in account record view   
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_25408_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Nav to Accounts RLI subpanel
		myAccount.navToRecord();
		StandardSubpanel rliSubpanel = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();
		rliSubpanel.editRecord(1);
		
		// Update fields inside sub-panel
		FieldSet myTestData = testData.get(testName).get(0);
		rliSubpanel.getEditField(1, "likelyCase").set(myTestData.get("likelyCase"));
		rliSubpanel.getEditField(1, "date_closed").set(myTestData.get("date_closed"));
 		
 		// Save changes 
 		rliSubpanel.saveAction(1);
 		VoodooUtils.waitForReady();
 		
 		// Verify that likely field and expected close date field are updated in opportunity subpanel 
 		StandardSubpanel oppSubpanel = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
 		oppSubpanel.expandSubpanel();
 		oppSubpanel.getDetailField(1, "oppAmount").assertContains(myTestData.get("likelyCase"), true);
 		oppSubpanel.getDetailField(1, "date_closed").assertContains(myTestData.get("date_closed"), true);
 		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
