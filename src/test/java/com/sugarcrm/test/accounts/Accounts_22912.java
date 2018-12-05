package com.sugarcrm.test.accounts;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Accounts_22912 extends SugarTest {
	AccountRecord myAccount;
	OpportunityRecord myOpp;
	StandardSubpanel opportunitiesSubpanel;

	public void setup() throws Exception {
		sugar().login();

		// Create a new account and a contact
		myAccount = (AccountRecord)sugar().accounts.api.create();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();

		// Add Account to OpportunityRecord
		// TODO: VOOD-444 
		myOpp.navToRecord();
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().opportunities.recordView.save();
	}

	/**
	 * Verify that contact record related to this account can be viewed by clicking subject link on "opportunities" sub-panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22912_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto Accounts record view
		myAccount.navToRecord();
		opportunitiesSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitiesSubpanel.expandSubpanel();

		// Click one contact's name link on "opportunities" sub-panel
		opportunitiesSubpanel.clickRecord(1);
		
		// Verify that Opportunity recordview appears

		sugar().opportunities.recordView.assertVisible(true);
		sugar().opportunities.recordView.getDetailField("name").assertContains(myOpp.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}