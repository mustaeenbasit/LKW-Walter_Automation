package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22945 extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		myAccount = (AccountRecord) sugar().accounts.api.create(fs);
		sugar().login();
	}

	/**
	 * Account Detail - Member Organizations sub-panel - Select_Verify that the account member related to the account 
	 * can be selected by clicking the "Account Name" link from the select account members pop-up box.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22945_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to an account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Click Link button in "Member Organization" sub-panel
		StandardSubpanel memberOrganizations = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizations.clickLinkExisting();
		
		// Click "Account Name" link from "Member Organization" Search & Select Drawer
		// Not added reference of any VOOD as library method already exist but used VoodooControl due to test's demand (Click "Account Name" link instead of checkbox)
		new VoodooControl("div", "css", "#drawers .flex-list-view-content td:nth-child(2) div").click();
		sugar().accounts.searchSelect.link();
		VoodooUtils.waitForReady();
		
		// Verify selected account member is displayed on "Member Organization" sub-panel
		memberOrganizations.expandSubpanel();
		memberOrganizations.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}