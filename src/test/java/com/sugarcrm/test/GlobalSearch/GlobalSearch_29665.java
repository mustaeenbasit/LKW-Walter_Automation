package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_29665 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Disabled Modules are not displayed in Global Search-Module List Dropdown.
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_29665_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1860
		// Disabling Account module for global search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();
		VoodooControl enabledModuleCtrl = new VoodooControl("tr", "css", "#enabled_div .yui-dt-rec.yui-dt-first");
		VoodooControl disabledModuleCtrl = new VoodooControl("tr", "css", "#disabled_div .yui-dt-rec.yui-dt-first");
		enabledModuleCtrl.dragNDrop(disabledModuleCtrl);
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Click down arrow in global search box to see module list
		sugar().navbar.getControl("globalSearch").click();
		sugar().navbar.search.getControl("searchModuleDropdown").click();

		// Verifying Accounts module is not showing in Global Search module list
		sugar().navbar.search.getControl("searchModuleList").assertContains(sugar().accounts.moduleNamePlural, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}