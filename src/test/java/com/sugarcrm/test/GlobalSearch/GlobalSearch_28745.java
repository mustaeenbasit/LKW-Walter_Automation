package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28745 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify changed globalsearch component structure
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28745_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on Global search bar
		// Assert that the search bar is a separate view from the quick results view
		sugar().navbar.getControl("globalSearch").set(sugar().accounts.getDefaultData().get("name"));
		VoodooUtils.waitForReady();
		sugar().navbar.search.getControl("searchResults").assertVisible(true);

		// Click the global search module drop down
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		VoodooUtils.waitForReady();
		// Select Accounts and Contacts module from the Global search module drop down list
		sugar().navbar.search.getControl("searchAccounts").click();
		sugar().navbar.search.getControl("searchContacts").click();

		VoodooControl searchModuleIcons = sugar().navbar.search.getControl("searchModuleIcons");
		String contactModuleIcon = sugar().contacts.moduleNamePlural.substring(0, 2);
		String accountModuleIcon = sugar().accounts.moduleNamePlural.substring(0, 2);
		// TODO: VOOD-1843 - Improve ChildElement to detect the parent strategy
		// Assert that Accounts & Contacts modules are tagged and displayed in global search bar 
		new VoodooControl("span", "css", searchModuleIcons.getHookString()+" .label-Accounts").assertEquals(accountModuleIcon, true);
		new VoodooControl("span", "css", searchModuleIcons.getHookString()+" .label-Contacts").assertEquals(contactModuleIcon, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}