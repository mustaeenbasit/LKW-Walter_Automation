package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28649 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that expanded quicksearch bar does not hide all module icons.
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28649_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on GlobalSearch bar
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		globalSearch.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1849
		// Verify that the bar expands but it should leave the first module name intact
		VoodooControl navBarMenu = new VoodooControl("span", "css", ".nav.megamenu[data-container=module-list] li[data-module='"+sugar().accounts.moduleNamePlural+"'] span.btn-group");
		navBarMenu.assertVisible(true);

		// Navigate to any module such as Leads. 
		sugar().leads.navToListView();

		// Hit Enter on quicksearch bar
		// TODO: VOOD-1849, CB-252
		globalSearch.set(testName+'\uE007');
		VoodooUtils.waitForReady();

		// TODO: VOOD-1849
		// Verify that the search bar expands and it leaves the Leads module intact since Leads is the active module.
		new VoodooControl("span", "css", ".nav.megamenu[data-container=module-list] li[data-module='"+sugar().leads.moduleNamePlural+"'] span.btn-group").assertVisible(true);
		navBarMenu.assertVisible(false); // False is for hidden Account menu from nav bar

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}