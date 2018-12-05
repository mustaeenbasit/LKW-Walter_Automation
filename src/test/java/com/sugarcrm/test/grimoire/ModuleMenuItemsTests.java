package com.sugarcrm.test.grimoire;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class ModuleMenuItemsTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyModuleMenuItem() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModuleMenuItem()...");

		// Verify account menu items
		sugar().navbar.selectMenuItem(sugar().accounts, "createAccount");
		sugar().accounts.createDrawer.getEditField("name").assertVisible(true);
		sugar().accounts.createDrawer.cancel();

		sugar().navbar.selectMenuItem(sugar().accounts, "viewAccounts");
		sugar().accounts.listView.assertIsEmpty();

		sugar().navbar.selectMenuItem(sugar().accounts, "importAccounts");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1396
		new VoodooControl("h2", "css", "div.moduleTitle h2").assertContains("Upload Import File", true);
		VoodooUtils.focusDefault();

		sugar().navbar.selectMenuItem(sugar().accounts, "viewAccountReports");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().reports.listView.getControl("moduleTitle").assertEquals("Search", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyModuleMenuItem() complete.");
	}

	public void cleanup() throws Exception {}
}