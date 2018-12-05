package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_17521 extends SugarTest {
	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		fs.put("firstName", testName);
		
		// Create records of Leads and Targets module
		sugar().leads.api.create(fs);
		sugar().targets.api.create(fs);
		sugar().login();
	}

	/**
	 * de-select modules for global search from the searchable module list
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_17521_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet modulesLabel = testData.get(testName).get(0);
		
		// Define controls
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		VoodooControl searchLeads = sugar().navbar.search.getControl("searchLeads");
		VoodooControl searchProspects = sugar().navbar.search.getControl("searchProspects");
		VoodooControl searchResults = sugar().navbar.search.getControl("searchResults");
		VoodooControl searchModuleIcons = sugar().navbar.search.getControl("searchModuleIcons");
		
		// Click down arrow in global search box
		globalSearch.click();
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		
		// Click checkbox before some modules
		searchLeads.click();
		searchProspects.click();
			
		// Input data in global search box and perform search
		globalSearch.set(testName);
		VoodooUtils.waitForReady();
		
		// Click down arrow in global search box
		globalSearch.click();
		sugar().navbar.search.getControl("searchModuleDropdown").click();
				
		// Uncheck checkbox for any module
		searchProspects.click();
		
		// Verify that the module is de-selected
		searchProspects.assertAttribute("class", "selected", false);
		
		// Cancel previous searching
		sugar().navbar.search.getControl("cancelSearch").click();
		
		// Input data in global search box and perform search
		globalSearch.click();
		globalSearch.set(testName);
		VoodooUtils.waitForReady();
		
		// Verify Records in the de-selected modules and match the search criteria shouldn't searched out
		// TODO: VOOD-1843 -Uncomment lines#68-71 and remove lines#73-76 after VOOD-1843 resolved
		searchResults.assertContains(testName, true);
		// searchResults.getChildElement("span", "css", ".label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		// searchModuleIcons.getChildElement("span", "css", ".label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		// searchResults.getChildElement("span", "css", ".label-" + sugar().targets.moduleNamePlural).assertVisible(false);
		// searchModuleIcons.getChildElement("span", "css", ".label-" + sugar().targets.moduleNamePlural).assertVisible(false);
		
		new VoodooControl("span", "css", ".navbar .search .dropdown-menu.search-results .label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		new VoodooControl("span", "css", ".navbar .search [data-label='module-icons'] .label-" + sugar().leads.moduleNamePlural).assertEquals(modulesLabel.get("leadsLabel"), true);
		new VoodooControl("span", "css", ".navbar .search .dropdown-menu.search-results .label-" + sugar().targets.moduleNamePlural).assertVisible(false);
		new VoodooControl("span", "css", ".navbar .search [data-label='module-icons'] .label-" + sugar().targets.moduleNamePlural).assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}