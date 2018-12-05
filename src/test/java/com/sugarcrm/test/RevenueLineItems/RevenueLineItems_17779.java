package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17779 extends SugarTest {
	
	AccountRecord myAccount;
	OpportunityRecord myOpp;
	
	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord)sugar().accounts.api.create();
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		
		// Relate Account with Opportunity 
		FieldSet fs = new FieldSet();
		fs.put("relAccountName", myAccount.getRecordIdentifier());
		myOpp.edit(fs);
	}
	
	/**
	 * Verify that account field is populated when opportunity is selected in RLI edit view
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17779_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
		
		// Verify that account field is populated 
		new VoodooControl("a","css",".fld_account_name a.ellipsis_inline").assertContains(myAccount.getRecordIdentifier(), true);
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}