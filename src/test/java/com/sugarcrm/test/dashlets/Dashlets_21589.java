package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21589 extends SugarTest {
	
	public void setup() throws Exception {
		// Creating a test Account and Contact Record
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		
		// Login as admin
		sugar().login();
		
		// Update Contact's Account Name
		// TODO: VOOD-444
		sugar().contacts.navToListView();
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.getEditField(1, "relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.listView.saveRecord(1);
	}

	/**
	 * Verify that dashlet displays data correctly upon dashlet refresh [page_dashlets_refresh]
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21589_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-591 Dashlets support
		VoodooControl cogIcon = new VoodooControl("a", "css", "li:nth-child(3) a.dropdown-toggle");
		VoodooControl dashletContactName = new VoodooControl("div", "css", "li:nth-child(3) .fld_full_name div");
		VoodooControl dashletAccountName = new VoodooControl("div", "css", "li:nth-child(3) .fld_account_name div");
		VoodooControl dashletContactTitle = new VoodooControl("div", "css", "li:nth-child(3) .fld_title div");

		// Navigate to Home
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		
		String fullName = sugar().contacts.getDefaultData().get("fullName");
		String accountName= sugar().accounts.getDefaultData().get("name");
		String title = sugar().contacts.getDefaultData().get("title");
		
		// Verifying initially that Contact record is displayed correctly in 'My Contacts' dashlet
		dashletContactName.assertEquals(fullName, true);
		dashletAccountName.assertEquals(accountName, true);
		dashletContactTitle.assertEquals(title, true);
		
		// Clicking the Configuration Cog icon on My Contacts dashlet
		cogIcon.click();
		
		// Select 'Edit'
		new VoodooControl("li", "css", "li:nth-child(3) .btn-toolbar li").click();
		VoodooUtils.waitForReady();
		
		// Edit the dashlet: remove the 'Office Phone' column 
		new VoodooControl("a", "css", ".select2-search-choice:nth-child(3) a").click();
		
		// Save dashlet
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();
		
		// Refresh the dashlet
		cogIcon.click();
		new VoodooControl("li", "css", "li:nth-child(3) .btn-toolbar li:nth-child(2)").click();
		VoodooUtils.waitForReady();
		
		// Verify that data is displayed as it was initially displayed upon refreshing the dashlet
		dashletContactName.assertEquals(fullName, true);
		dashletAccountName.assertEquals(accountName, true);
		dashletContactTitle.assertEquals(title, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}