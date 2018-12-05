package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23179 extends SugarTest {
	LeadRecord myLead;
	StandardSubpanel leadSub;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
		
		// link account with Leads
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		leadSub = sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSub.linkExistingRecord(myLead);	
	}

	/**
	 * Verify that removing lead record related to this account is canceled.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23179_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// leadSub.unlinkRecord(1);
		// Click "rem" icon on the right edge of a Lead record on "LEADS" sub-panel and click on "Cancel" button on the pop-up dialog.
		sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural).hover();
		leadSub.getControl("expandActionRow01").click();
		leadSub.getControl("unlinkActionRow01").click();
		sugar().alerts.getAlert().cancelAlert();
		
		FieldSet fs = new FieldSet();
		fs.put("fullName", sugar().leads.getDefaultData().get("fullName"));
		fs.put("phoneWork", sugar().leads.getDefaultData().get("phoneWork"));
		leadSub.verify(1, fs, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}