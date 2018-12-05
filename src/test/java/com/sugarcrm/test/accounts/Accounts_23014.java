package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23014 extends SugarTest {
	AccountRecord myAccount;
	LeadRecord myLead;
	StandardSubpanel leadSub;
	
	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		myAccount.navToRecord();
		leadSub = sugar().accounts.recordView.subpanels.get("Leads");
		leadSub.scrollIntoViewIfNeeded(false);
		leadSub.clickLinkExisting();
		// TODO VOOD-726
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		VoodooUtils.waitForReady();
	}
	
	/**
	 * Verify that lead related to this account is viewed correctly.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23014_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// after VOOD-790 is fixed leadSub.clickRecord(1) should be used 
		new VoodooControl("a","css",".filtered.layout_Leads tbody tr:nth-of-type(1) .fld_full_name.list").scrollIntoViewIfNeeded(false);
		new VoodooControl("a","css",".filtered.layout_Leads tbody tr:nth-of-type(1) .fld_full_name.list").click();

		VoodooUtils.waitForAlertExpiration();
		sugar().leads.recordView.getControl("editButton").assertVisible(true);
		// TODO only get lead's first name data now
		// sugar().leads.recordView.getDetailField("fullName").assertEquals(myLead.getRecordIdentifier(), true);
		sugar().leads.recordView.getDetailField("fullName").assertContains(myLead.get("firstName")+" "+myLead.get("lastName"), true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}