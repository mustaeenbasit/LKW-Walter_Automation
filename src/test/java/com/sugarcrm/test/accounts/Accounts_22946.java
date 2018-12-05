package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22946 extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		myAccount = (AccountRecord) sugar().accounts.api.create(fs);
		sugar().login();
	}

	/**
	 * Account Detail - Member Organizations sub-panel - Select_Verify that the account member can be selected by 
	 * checking the check box in front of the account member records from the select account member pop-up box.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22946_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to an account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Link Account member to Account by checking the 'check box' in front of the account member records in 'Search & Select Drawer'
		StandardSubpanel memberOrganizations = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizations.linkExistingRecord(myAccount);
		
		// Verify selected account member is displayed on "Member Organization" sub-panel
		memberOrganizations.expandSubpanel();
		memberOrganizations.getDetailField(1, "name").assertEquals(testName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}