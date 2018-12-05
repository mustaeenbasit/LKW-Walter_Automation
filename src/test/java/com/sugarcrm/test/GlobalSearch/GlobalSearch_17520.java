package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_17520 extends SugarTest {
	
	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		fs.put("firstName", testName);
		
		// Create record of Leads and Contacts module
		sugar().leads.api.create(fs);
		sugar().contacts.api.create(fs);
		sugar().login();
	}

	/**
	 * Select individual module for global search from the searchable modules list
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_17520_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet modulesLabel = testData.get(testName).get(0);
		
		// Define controls
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		VoodooControl searchLeads = sugar().navbar.search.getControl("searchLeads");
		VoodooControl searchContacts = sugar().navbar.search.getControl("searchContacts");
		VoodooControl searchResults = sugar().navbar.search.getControl("searchResults");
		VoodooControl searchModuleIcons = sugar().navbar.search.getControl("searchModuleIcons");
		
		// Click down arrow in global search box
		globalSearch.click();
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		
		// Click checkbox before modules that want to perform search against (Leads and Contacts)
		searchLeads.click();
		searchContacts.click();
		
		// Verify that the modules are selected
		searchLeads.assertAttribute("class", "selected", true);
		searchContacts.assertAttribute("class", "selected", true);
		
		// Input data in global search box and perform search
		globalSearch.set(testName);
		VoodooUtils.waitForReady();
		
		// Verify Records in the selected modules and match the search criteria are searched out
		// TODO: VOOD-1843 -Uncomment lines#59-62 and remove lines#64-69 after VOOD-1843 resolved
		searchResults.assertContains(testName, true);
		// searchResults.getChildElement("span", "css", ".label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		// searchModuleIcons.getChildElement("span", "css", ".label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		// searchResults.getChildElement("span", "css", ".label-" + sugar().contacts.moduleNamePlural).assertEquals(modulesLabel.get("contactsLabel"), true);
		// searchModuleIcons.getChildElement("span", "css", ".label-" + sugar().contacts.moduleNamePlural).assertEquals(modulesLabel.get("contactsLabel"), true);
		
		String searchResultsHookString = sugar().navbar.search.getControl("searchResults").getHookString();
		String searchModuleIconsHookString = sugar().navbar.search.getControl("searchResults").getHookString();
		new VoodooControl("span", "css", searchResultsHookString + " .label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		new VoodooControl("span", "css", searchModuleIconsHookString + " .label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		new VoodooControl("span", "css", searchResultsHookString + " .label-" + sugar().contacts.moduleNamePlural).assertEquals(modulesLabel.get("contactsLabel"), true);
		new VoodooControl("span", "css", searchModuleIconsHookString + " .label-" + sugar().contacts.moduleNamePlural).assertEquals(modulesLabel.get("contactsLabel"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}