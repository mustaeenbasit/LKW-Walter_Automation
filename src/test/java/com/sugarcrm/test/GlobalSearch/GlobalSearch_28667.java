package com.sugarcrm.test.GlobalSearch;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28667 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that routing to the search page with a module list should populate module dropdown
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28667_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Define 'search records' and controls
		ArrayList<String> searchRecord = new ArrayList<String>();
		searchRecord.add(sugar().accounts.getDefaultData().get("name"));
		searchRecord.add(sugar().contacts.getDefaultData().get("firstName") + " " + sugar().contacts.getDefaultData().get("lastName"));
		searchRecord.add(sugar().opportunities.getDefaultData().get("name"));

		// Define Controls
		// TODO: VOOD-1853
		VoodooControl firstSearchedCtrl = new VoodooControl("a", "css", "ul.search-results .search-result:nth-child(1) h3 a");
		String baseUrl = new SugarUrl().getBaseUrl();
		VoodooControl modulesIconCtrl = sugar().navbar.search.getControl("searchModuleIcons");

		for(int i = 0; i < searchRecord.size(); i++) {
			// Go to https://instance_url/#search/de?modules=Contacts,Accounts,Opportunities
			VoodooUtils.go(baseUrl + "#search/" + searchRecord.get(i).replaceAll(" ", "%20") + "?modules=" + sugar().accounts.moduleNamePlural + "," + sugar().contacts.moduleNamePlural + "," + sugar().opportunities.moduleNamePlural);
			VoodooUtils.waitForReady();

			// Verify that Contacts, Accounts and Opportunities are checked in the quicksearch module dropdown
			sugar().navbar.search.getControl("searchAccounts").assertAttribute("class", customFS.get("selected"), true);
			sugar().navbar.search.getControl("searchContacts").assertAttribute("class", customFS.get("selected"), true);
			sugar().navbar.search.getControl("searchOpportunities").assertAttribute("class", customFS.get("selected"), true);

			// Verfiy that Global search bar should include Icons of Contacts, Accounts and Opportunities 
			modulesIconCtrl.getChildElement("span", "css", ".label-" + sugar().accounts.moduleNamePlural).assertExists(true);
			modulesIconCtrl.getChildElement("span", "css", ".label-" + sugar().contacts.moduleNamePlural).assertExists(true);
			modulesIconCtrl.getChildElement("span", "css", ".label-" + sugar().opportunities.moduleNamePlural).assertExists(true);

			// Verify that search results for "search record" is displayed based on Contacts, Accounts and Opportunities
			firstSearchedCtrl.assertContains(searchRecord.get(i), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}