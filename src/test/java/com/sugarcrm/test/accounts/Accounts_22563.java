package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_22563 extends SugarTest {
	AccountRecord myAccount;
	LeadRecord myLead;
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		// create account and lead
		myAccount = (AccountRecord) sugar().accounts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		myLead.edit(ds.get(0));
		myAccount.navToRecord();		
	}

	/**
	 * Verify a Lead record with salutation is correctly displayed in subpanel listview
	 * @throws Exception
	 */
	@Test
	public void Accounts_22563_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural).clickLinkExisting();
		VoodooUtils.waitForAlertExpiration();
		// TODO VOOD-726		
		new VoodooControl("input", "css", "table.search-and-select input.toggle-all").click();
		new VoodooControl("a", "css" ,"div.btn-toolbar.pull-right.dropdown a.btn.btn-primary").click();
		VoodooUtils.waitForAlertExpiration();
		// TODO VOOD-609		
		// getRecordIdentifier() not fully work for leads
		// new VoodooControl("div", "css" ,"div.layout_Leads").assertElementContains(myLead.getRecordIdentifier(),true);		
		new VoodooControl("span", "css" ,"div.layout_Leads table span[data-voodoo-name='full_name']").assertElementContains(myLead.get("salutation")+" "+myLead.get("firstName")+" "+myLead.get("lastName"),true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}