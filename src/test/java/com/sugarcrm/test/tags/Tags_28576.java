package com.sugarcrm.test.tags;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Tags_28576 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}
	
	/**
	 * Verify user is able to navigate to the Tag record
	 * @throws Exception
	 */
	@Test
	public void Tags_28576_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to accounts and creating a new account record with tag
		sugar.navbar.selectMenuItem(sugar.accounts, "createAccount");
		sugar.accounts.createDrawer.getEditField("name").set(sugar.accounts.getDefaultData().get("name"));
		sugar.accounts.createDrawer.getEditField("tags").set(sugar.tags.getDefaultData().get("name"));
		sugar.accounts.createDrawer.save();
		
		// Navigating to tags module
		sugar.tags.navToListView();
		sugar.tags.listView.clickRecord(1);
		
		// Verifying the tag name
		sugar.tags.recordView.getDetailField("name").assertEquals
			(sugar.tags.getDefaultData().get("name"), true);
		
		StandardSubpanel accountSubpanel = sugar.tags.recordView.subpanels
				.get(sugar.accounts.moduleNamePlural);
		accountSubpanel.scrollIntoViewIfNeeded(false);
		accountSubpanel.expandSubpanel();
		
		// Verifying that account created before is displayed under Accounts subpanel
		accountSubpanel.getDetailField(1, "name").assertEquals
			(sugar.accounts.getDefaultData().get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}