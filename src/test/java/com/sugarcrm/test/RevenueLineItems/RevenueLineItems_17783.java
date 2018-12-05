package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17783 extends SugarTest {
	OpportunityRecord myOpp;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		// create an account record
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
		// create opportunities record via ui to link Account and rli record
		myOpp = (OpportunityRecord)sugar().opportunities.create();
	}

	/**
	 * Unlink should not be available in Revenue Line Items subpanel
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17783_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// open opportunity record view 
		myOpp.navToRecord();
		// Go to "Revenue Line Items" subpanel.
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.scrollIntoView();
		
		//Assert Subpanel action dropdown is disabled
		rliSubPanel.getControl("expandSubpanelActions").assertAttribute("class", "disabled", true);
		
		// Click on action drop down list of an revenue line item. (the one next to preview icon).
		rliSubPanel.expandSubpanel();
		rliSubPanel.expandSubpanelRowActions(1);
		// Verify it shows "Edit".  No "Unlink" option.  
		rliSubPanel.getControl("editActionRow01").assertVisible(true);
		rliSubPanel.getControl("unlinkActionRow01").assertVisible(false);
		
		// open account record view 
		myAccount.navToRecord();
		// Go to "Opportunities" subpanel.
		StandardSubpanel myOppPanel = sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		myOppPanel.scrollIntoView();
		
		//Assert Subpanel action dropdown is disabled
		myOppPanel.getControl("expandSubpanelActions").assertAttribute("class", "disabled", true);
		
		// Click on action drop down list of an revenue line item. (the one next to preview icon).
		myOppPanel.expandSubpanel();
		myOppPanel.expandSubpanelRowActions(1);
		// Verify it shows "Edit".  No "Unlink" option. 
		myOppPanel.getControl("editActionRow01").assertVisible(true);
		myOppPanel.getControl("unlinkActionRow01").assertVisible(false);
		
		// Go to "RLI" subpanel.
		StandardSubpanel myRliPanel = sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		myRliPanel.scrollIntoView();
		
		//Assert Subpanel action dropdown does not exist
		myRliPanel.getControl("expandSubpanelActions").assertExists(false);
		
		// Click on action drop down list of an RLI. (the one next to preview icon).
		myRliPanel.expandSubpanel();
		myRliPanel.expandSubpanelRowActions(1);
		// Verify it shows "Edit".  No "Unlink" option. 
		myRliPanel.getControl("editActionRow01").assertVisible(true);
		myRliPanel.getControl("unlinkActionRow01").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}