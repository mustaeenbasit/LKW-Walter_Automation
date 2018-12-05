package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class AccountCreateViaMenuTest extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void AccountCreateViaMenuTest_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.save();

		// Verify account is created
		sugar().accounts.listView.getDetailField(1, "name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}