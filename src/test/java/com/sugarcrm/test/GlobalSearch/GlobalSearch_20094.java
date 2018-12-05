package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20094 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Login
		sugar().login();
	}

	/**
	 * Auto display results for global search terms of length 3 or more
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20094_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Input 1 or more characters in the global search box(make sure the system has some records meet the search criteria)
		String accountName = sugar().accounts.getDefaultData().get("name");
		sugar().navbar.setGlobalSearch(accountName.substring(0, 2));

		// Verify that search results will automatically be populated into the search result window
		sugar().navbar.search.getControl("searchResults").assertContains(accountName, true);

		// Hit drop down arrow in right-hand side of global search box 
		sugar().navbar.search.getControl("searchModuleDropdown").click();

		// Define Controls for Global Search window
		VoodooControl searchAllCtrl = sugar().navbar.search.getControl("searchAll");
		VoodooControl accountsModuleCtrl = sugar().navbar.search.getControl("searchAccounts");
		VoodooControl bugssModuleCtrl = sugar().navbar.search.getControl("searchBugs");
		VoodooControl contactssModuleCtrl = sugar().navbar.search.getControl("searchContacts");

		// Verify that drop down returns checklist of search criteria
		searchAllCtrl.assertAttribute("class", customFS.get("selected"), true);

		// Select few modules from the drop down
		accountsModuleCtrl.click();
		bugssModuleCtrl.click();
		contactssModuleCtrl.click();

		// Verify that the selected modules are checked in the Global Search window drop down 
		accountsModuleCtrl.assertAttribute("class", customFS.get("selected"), true);
		bugssModuleCtrl.assertAttribute("class", customFS.get("selected"), true);
		contactssModuleCtrl.assertAttribute("class", customFS.get("selected"), true);

		// Verify that Global search bar should include Icons of Contacts, Accounts and Bugs 
		accountsModuleCtrl.getChildElement("span", "css", ".label-" + sugar().accounts.moduleNamePlural).assertExists(true);
		bugssModuleCtrl.getChildElement("span", "css", ".label-" + sugar().bugs.moduleNamePlural).assertExists(true);
		contactssModuleCtrl.getChildElement("span", "css", ".label-" + sugar().contacts.moduleNamePlural).assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}