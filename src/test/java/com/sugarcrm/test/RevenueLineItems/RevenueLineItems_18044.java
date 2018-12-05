package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18044 extends SugarTest {
	AccountRecord myAccount;
	OpportunityRecord myOpp;

	public void setup() throws Exception {
		sugar().login();

		myAccount = (AccountRecord)sugar().accounts.api.create();
		myOpp = (OpportunityRecord)sugar().opportunities.create();
	}

	/** 
	 * TC 18044: ENT/ULT: Verify that account name is auto-populated when creating 
	 * new RLI via RLI subpanel of opportunity record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18044_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myOpp.navToRecord();
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get("RevenueLineItems");
		// Click on the plus button to create RLI linked to the opportunity 
		rliSubpanel.addRecord();
		
		// Verify Opportunity and Account values are pre-populated for new RLI record 
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").assertContains(myOpp.getRecordIdentifier(), true);
		
		// TODO: Change this to use library support when create drawer can handle detail mode fields on the background.
		new VoodooControl("a","css","#drawers .fld_account_name.detail a").assertContains(myAccount.getRecordIdentifier(), true);
		
		// Cancel RLI record creation
		sugar().revLineItems.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
