package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17729 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the inline editing on email address can be saved in the list view 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17729_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		sugar().accounts.navToListView();
		sugar().accounts.listView.toggleSidebar();
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1, "emailAddress").set(sugar().accounts.getDefaultData().get("emailAddress"));
		sugar().accounts.listView.saveRecord(1);
		sugar().accounts.listView.toggleSidebar();

		// Verify that email address field is updated correctly
		sugar().accounts.listView.getDetailField(1, "emailAddress").assertEquals(sugar().accounts.getDefaultData().get("emailAddress"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}