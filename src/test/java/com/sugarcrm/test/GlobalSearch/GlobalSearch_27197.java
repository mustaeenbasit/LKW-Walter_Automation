package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_27197 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().targets.api.create();
		sugar().calls.api.create();
		FieldSet recordData = new FieldSet();
		recordData.put("name", customData.get("name"));
		sugar().accounts.api.create(recordData);
		recordData.clear();
		recordData.put("firstName", "");
		recordData.put("lastName", customData.get("name"));
		sugar().contacts.api.create(recordData);
		sugar().login();
	}
	/**
	 * Using ~ in search string to perform fuzzy query search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_27197_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Inputing the search string in global search box to perform fuzzy query search 
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.set(customData.get("searchString"));
		VoodooUtils.waitForReady();
		VoodooControl searchResultsCtrl = sugar().navbar.search.getControl("searchResults");

		// Verifying that Contact and Account records are displayed in the Search DropDown
		searchResultsCtrl.assertContains(customData.get("module1")+ "\n" + customData.get("name"), true);
		searchResultsCtrl.assertContains(customData.get("module2")+ "\n" + customData.get("name"), true);

		// Verifying that the Target and Call records are not displayed in the Search Dropdown
		searchResultsCtrl.assertContains(sugar().targets.getDefaultData().get("firstName"), false);
		searchResultsCtrl.assertContains(sugar().calls.getDefaultData().get("name"), false);
		
		// Making a search with a random string
		globalSearchCtrl.set(sugar().leads.getDefaultData().get("firstName"));
		VoodooUtils.waitForReady();
		
		// Verifying that 'No results were found.' is displayed when no results are found
		searchResultsCtrl.assertContains(customData.get("noResultsString"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}