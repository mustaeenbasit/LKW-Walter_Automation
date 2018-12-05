package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_19027 extends SugarTest {
	DataSource customBugsDS = new DataSource(), customCasesDS = new DataSource();

	public void setup() throws Exception {
		customBugsDS = testData.get(testName+"_bugsData");
		customCasesDS = testData.get(testName+"_casesData");

		// Create two cases and two bugs
		sugar().cases.api.create(customCasesDS);
		sugar().bugs.api.create(customBugsDS);
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Bug number and name is able to be searched in global search
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_19027_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		try {
			VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
			globalSearchCtrl.click();
			VoodooUtils.waitForReady();
			VoodooControl searchModDropDownCtrl = sugar().navbar.search.getControl("searchModuleDropdown");
			searchModDropDownCtrl.click();
			FieldSet customFS = testData.get(testName).get(0);

			// In global searching box, enter "2" to search.
			globalSearchCtrl.set(customFS.get("searchIntegerValue"));
			VoodooUtils.waitForReady();

			// Verify that the results including Bugs and Cases, and possible others depending on whether "2" is included in the hit results.
			VoodooControl searchResultCtrl = sugar().navbar.search.getControl("searchResults");
			searchResultCtrl.assertContains(customBugsDS.get(1).get("name"), true);
			searchResultCtrl.assertContains(customCasesDS.get(1).get("name"), true);

			// In global searching box, enter "bad" to search.
			globalSearchCtrl.set(customFS.get("searchText"));
			VoodooUtils.waitForReady();

			// Verify that the results including Bugs and possible others depending on whether "bad" is included in the hit records.
			searchResultCtrl.assertContains(customBugsDS.get(2).get("name"), true);
			searchResultCtrl.assertContains(customCasesDS.get(2).get("name"), true);

			// Change to search for Bugs module only.  
			searchModDropDownCtrl.click();
			sugar().navbar.search.getControl("searchBugs").click();

			// In global searching box, enter "2" to search.
			globalSearchCtrl.set(customFS.get("searchIntegerValue"));
			VoodooUtils.waitForReady();

			// Verify that the results only in Bugs.
			searchResultCtrl.assertContains(customBugsDS.get(1).get("name"), true);
			searchResultCtrl.assertContains(customCasesDS.get(1).get("name"), false);

			// In global searching box, enter "2" to search.
			globalSearchCtrl.set(customFS.get("searchText"));
			VoodooUtils.waitForReady();

			// Verify that the results only in Bugs.
			searchResultCtrl.assertContains(customBugsDS.get(2).get("name"), true);
			searchResultCtrl.assertContains(customCasesDS.get(2).get("name"), false);

		} finally {
			// Better to close global search bar
			sugar().navbar.search.getControl("cancelSearch").click();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}