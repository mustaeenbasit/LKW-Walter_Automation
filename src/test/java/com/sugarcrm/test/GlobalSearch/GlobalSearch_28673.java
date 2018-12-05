package com.sugarcrm.test.GlobalSearch;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class GlobalSearch_28673 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user can choose modules from global search module drop down list
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28673_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource moduleIconDS = testData.get(testName);

		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().bugs);
		modules.add(sugar().campaigns);
		modules.add(sugar().leads);
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		VoodooControl searchModuleDropdown = sugar().navbar.search.getControl("searchModuleDropdown");
		VoodooControl searchModuleIcon = sugar().navbar.search.getControl("searchModuleIcons");
		VoodooControl searchCall = sugar().navbar.search.getControl("searchCalls");

		globalSearch.click();
		searchModuleDropdown.click();

		// Verify by default Module Button Icon will say "All" and At least one option in the module drop down has to be selected
		searchModuleIcon.assertEquals(moduleIconDS.get(0).get("moduleIcons"), true);
		sugar().navbar.search.getControl("searchAll").assertAttribute("class", "selected", true);

		// Verify each Module Icon displays in the Search Bar for selected modules
		sugar().navbar.searchModules(modules);
		for(Module module : modules) {
			// TODO: VOOD-1843
			new VoodooControl("span", "css", searchModuleIcon.getHookString()+" .label-" + module.moduleNamePlural).assertEquals(module.moduleNamePlural.substring(0, 2), true);
		}

		// Verify the Module Button Icon will say "Multiple Modules".
		globalSearch.click();
		searchModuleDropdown.click();
		sugar().navbar.search.getControl("searchCases").click();
		searchModuleIcon.assertEquals(moduleIconDS.get(1).get("moduleIcons"), true);

		// Verify User can select or deselect the modules.
		globalSearch.click();
		searchModuleDropdown.click();
		searchCall.click();
		searchCall.assertAttribute("class", "selected", true);
		searchCall.click();
		searchCall.assertAttribute("class", "selected", false);

		sugar().navbar.search.getControl("cancelSearch").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}