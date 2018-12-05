package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28653 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify that module filters can be applied for global search
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28653_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Click on Global search textbox to expand
		sugar().navbar.getControl("globalSearch").click();

		// Define Global Search Controls
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		VoodooControl cancelSearchCtrl = sugar().navbar.search.getControl("cancelSearch");

		// Click on "All" (Module Filter) dropdown
		sugar().navbar.search.getControl("searchModuleDropdown").click();

		// Select two-three modules say Accounts, Contacts and Opportunities
		sugar().navbar.search.getControl("searchAccounts").click();
		sugar().navbar.search.getControl("searchContacts").click();
		sugar().navbar.search.getControl("searchOpportunities").click();

		// Verify that "Avatar" for selected module should be displayed 
		VoodooControl modulesIconCtrl = sugar().navbar.search.getControl("searchModuleIcons");
		modulesIconCtrl.getChildElement("span", "css", ".label-" + sugar().accounts.moduleNamePlural).assertEquals(customFS.get("accountsLabel"), true);
		modulesIconCtrl.getChildElement("span", "css", ".label-" + sugar().contacts.moduleNamePlural).assertEquals(customFS.get("contactsLabel"), true);
		modulesIconCtrl.getChildElement("span", "css", ".label-" + sugar().opportunities.moduleNamePlural).assertEquals(customFS.get("opportunitiesLabel"), true);

		// Enter any existing Accounts record name and hit enter
		globalSearchCtrl.click();
		// TODO: CB-252, VOOD-1849
		globalSearchCtrl.set(sugar().accounts.getDefaultData().get("name")+'\uE007');
		VoodooUtils.waitForReady();

		// Verify that the search results should display records related to the selected modules 
		// TODO: VOOD-1853
		VoodooControl firstSearchedCtrl = new VoodooControl("a", "css", "ul.search-results .search-result:nth-child(1) h3 a");
		firstSearchedCtrl.assertContains(sugar().accounts.getDefaultData().get("name"), true);

		// Enter any existing Contacts record name and hit enter
		globalSearchCtrl.click();
		// TODO: CB-252, VOOD-1849
		cancelSearchCtrl.click();
		globalSearchCtrl.set(sugar().contacts.getDefaultData().get("lastName")+'\uE007');
		VoodooUtils.waitForReady();

		// Verify that the search results should display records related to the selected modules 
		firstSearchedCtrl.assertContains(sugar().contacts.getDefaultData().get("firstName") + " " + sugar().contacts.getDefaultData().get("lastName"), true);

		// Enter any existing Opportunity record name and hit enter
		globalSearchCtrl.click();
		// TODO: CB-252, VOOD-1849
		cancelSearchCtrl.click();
		globalSearchCtrl.set(sugar().opportunities.getDefaultData().get("name")+'\uE007');
		VoodooUtils.waitForReady();

		// Verify that the search results should display records related to the selected modules 
		firstSearchedCtrl.assertContains(sugar().opportunities.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}