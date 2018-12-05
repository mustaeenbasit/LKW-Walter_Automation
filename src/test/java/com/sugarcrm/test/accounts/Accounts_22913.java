package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22913 extends SugarTest {
	StandardSubpanel leadsSubpanel;

	public void setup() throws Exception {
		AccountRecord myAccount = (AccountRecord)sugar().accounts.api.create();
		LeadRecord myLead =(LeadRecord) sugar().leads.api.create();
		sugar().login();
		myAccount.navToRecord();
		leadsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecord(myLead);
	}

	/**
	 * Verify that lead record related to this account can be viewed by clicking name link on "LEADS" sub-panel
	 *	@throws Exception
	 */
	@Test
	public void Accounts_22913_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		leadsSubpanel.scrollIntoViewIfNeeded(false);;
		leadsSubpanel.clickRecord(1);

		// verify Leads Record View is opened
		sugar().leads.recordView.getControl("showMore").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}