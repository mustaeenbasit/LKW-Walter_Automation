package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_27784 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Creating Lead via Account module Leads subpanel should auto populate Account Name
	 * @throws Exception
	 */
	@Test
	public void Accounts_27784_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural).addRecord();

		// Verify account name field is populated by default on leads create Drawer
		sugar().leads.createDrawer.getEditField("accountName").assertEquals(sugar().accounts.getDefaultData().get("name"), true);
		sugar().leads.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}