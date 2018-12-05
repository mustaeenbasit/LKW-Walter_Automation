package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23185 extends SugarTest {
	AccountRecord account;
	LeadRecord lead;
	StandardSubpanel leadsSubpanel;
	
	String leadName;
			
	public void setup() throws Exception {
		sugar().login();

		account = (AccountRecord)sugar().accounts.api.create();
		lead = (LeadRecord)sugar().leads.api.create();
		leadName = lead.getRecordIdentifier();
	}

	/**
	 * 23185 Verify that the lead can be selected by checking the check box in front of the lead records from the select leads panel
	 * @throws Exception
	 */
	@Test
	public void Accounts_23185_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		account.navToRecord();
		
		leadsSubpanel = sugar().accounts.recordView.subpanels.get("Leads");
		leadsSubpanel.clickLinkExisting();
		
		// TODO VOOD-739
		// Select the first Lead from the list
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("input", "css", "a[name='link_button']").click();
		sugar().alerts.getSuccess().closeAlert();

		// Verify that lead record is linked successfully
		leadsSubpanel.assertContains(leadName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
