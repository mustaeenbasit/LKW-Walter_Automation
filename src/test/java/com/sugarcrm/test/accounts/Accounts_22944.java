package com.sugarcrm.test.accounts;

import org.junit.Assert;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22944 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Account Detail - Member Organizations sub-panel - Create_Verify that creating new account member related to the 
	 * account is canceled in full form on "MEMBER ORGANIZATIONS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22944_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to an Account Record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Click "Create" on "Member Organizations" sub-panel
		StandardSubpanel memberOrganizationsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		memberOrganizationsSubpanel.addRecord();
		
		// Click "Show More" button and fill the fields
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		
		// Click "Cancel" button
		sugar().accounts.createDrawer.cancel();
		
		// Verify that account member record not displayed on "MEMBER ORGANIZATIONS" sub-panel
		memberOrganizationsSubpanel.expandSubpanel();
		Assert.assertEquals("Row count is not equal to 0 in subpanel", 0, memberOrganizationsSubpanel.countRows());
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}