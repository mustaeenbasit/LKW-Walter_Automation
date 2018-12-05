package com.sugarcrm.test.GlobalSearch;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class GlobalSearch_28671 extends SugarTest {
	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		// Account, Opportunity, Contact record with same name created via API
		sugar().accounts.api.create(fs);
		sugar().opportunities.api.create(fs);
		fs.clear();
		fs.put("firstName", "");
		fs.put("lastName", testName);
		sugar().contacts.api.create(fs);

		sugar().login();
	}

	/**
	 * Verify Back key navigation to Global Search from another page
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28671_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String currentPageURL = VoodooUtils.getUrl();

		// Select Accounts, Contacts & Opportunities module.
		ArrayList<Module> modules = new ArrayList<Module>();
		modules.add(sugar().accounts);
		modules.add(sugar().contacts);
		modules.add(sugar().opportunities);

		sugar().navbar.searchModules(modules);

		// Search with Keyword and click on record
		sugar().navbar.setGlobalSearch(testName);
		sugar().navbar.clickSearchResult(1);
		Assert.assertFalse("Browser doesnot forward from Home Dashboard View to Global search page", VoodooUtils.getUrl().equals(currentPageURL));

		// Click on back button
		VoodooUtils.back();

		// Verify user navigate to back page
		Assert.assertTrue("Browser doesnot backward from Global search page to Home Dashboard View", VoodooUtils.getUrl().equals(currentPageURL));

		// Verify  Global search bar should include Accounts, Contacts & Opportunities modules with last search query.
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		globalSearch.click();
		VoodooUtils.waitForReady();

		for(Module module : modules) {
			sugar().navbar.search.getControl("searchModuleIcons").getChildElement("span", "css", ".label-" + module.moduleNamePlural).assertExists(true);
		}

		globalSearch.assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}