package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_20251 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Navigation and Cancel from menu Quick Create Accounts
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_20251_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Quick create Account
		sugar().navbar.quickCreateAction(sugar().accounts.moduleNamePlural);
		sugar().accounts.createDrawer.getEditField("name").assertVisible(true);
		sugar().accounts.createDrawer.cancel();
	}

	public void cleanup() throws Exception {}
}