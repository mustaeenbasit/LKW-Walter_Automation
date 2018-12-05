package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23080 extends SugarTest {
	AccountRecord myAccount;
	LeadRecord myLead;
	StandardSubpanel leadSub;
	
	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		
		sugar().login();
		
		// account record with leads 
		myAccount.navToRecord();
		leadSub = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSub.linkExistingRecord(myLead);
		sugar().alerts.waitForLoadingExpiration();
	}
	
	/**
	 * Verify that "Leads" is correctly deleted on "Leads" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23080_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		leadSub.unlinkRecord(1);
		
		// TODO VOOD-609
		new VoodooControl("tr", "css", "div.layout_Leads table tbody tr").assertExists(false);
		myLead.verify();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}