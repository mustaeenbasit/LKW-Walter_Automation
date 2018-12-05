package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RenameModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyRenameModule() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyRenameModule()...");

		// Rename 'Opportunities' module
		sugar().admin.renameModule(sugar().opportunities, "Opp", "Opps");

		// Navigate to Opportunities module and verify that module is renamed successfully
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		VoodooControl opportunityModuleTitle = sugar().opportunities.listView.getControl("moduleTitle");
		opportunityModuleTitle.assertEquals("Opps", true);

		VoodooUtils.voodoo.log.info("verifyRenameModule() test complete.");
	}

	public void cleanup() throws Exception {}
}