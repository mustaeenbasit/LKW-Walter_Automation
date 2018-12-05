package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class KnowledgeBase_28456 extends SugarTest {
	public void setup() throws Exception {
		// Creating 2 KB articles to test Custom Filter (My items and My Favorites) together
		sugar().knowledgeBase.api.create();
		FieldSet myFS = new FieldSet();
		myFS.put("name", testName);
		sugar().knowledgeBase.api.create(myFS);

		// Login and Nav to KB list View
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB module. Sorting is needed because order is not consistent in list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		sugar().knowledgeBase.listView.sortBy("headerName", true);

		// Set one article as favorite
		sugar().knowledgeBase.listView.toggleFavorite(1);
	}

	/**
	 * Verify that custom filter can be created correctly with new fields My Items and My Favorites
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28456_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new Custom Filter
		DataSource myDS = testData.get(testName);
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Search and Verify each option in Custom Filter's Field-Name drop-down list
		// TODO: VOOD-1463
		// TODO: VOOD-629 Add support for accessing and manipulating individual components of a VoodooSelect
		VoodooSelect searchBox = new VoodooSelect("span", "css", ".detail.fld_filter_row_name");
		VoodooControl dropdownListMatch = new VoodooControl("span", "css", ".select2-match");
		searchBox.click();
		VoodooUtils.waitForReady();
		for (int i = 0; i < myDS.size(); i++) {
			searchBox.selectWidget.getControl("searchBox").set(myDS.get(i).get("field"));
			VoodooUtils.waitForReady();
			dropdownListMatch.assertEquals(myDS.get(i).get("field"), true);
		}

		// Search and Select "My Items" option in Filter Row01 drop-down and Verify results 
		searchBox.selectWidget.getControl("searchBox").set(myDS.get(4).get("field"));
		dropdownListMatch.click();
		VoodooUtils.waitForReady();
		Assert.assertEquals("Assert Row Count = 2 FAILED",2, sugar().knowledgeBase.listView.countRows());
		sugar().knowledgeBase.listView.verifyField(1, "name", sugar().knowledgeBase.getDefaultData().get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", testName);

		// Add Custom Filter Row02 and Select Custom filter row2 "My Favorites"
		// Search and Select "My Favorites" option in Filter Row02 drop-down
		// TODO: VOOD-629 Add support for accessing and manipulating individual components of a VoodooSelect
		sugar().knowledgeBase.listView.filterCreate.getControl("addFilterRow01").click();
		new VoodooSelect("span", "css", "[data-filter='row']:nth-of-type(2) .fld_filter_row_name.detail").
		set(myDS.get(6).get("field"));
		VoodooUtils.waitForReady();

		// Name and Save Custom Filter and Verify results again after saving
		sugar().knowledgeBase.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.listView.filterCreate.getControl("saveButton").click();
		VoodooUtils.waitForReady(30000);

		// Verify Custom filter Row2 "My Favorites" AND Row1 "My Items" are applied correctly
		Assert.assertEquals("Assert Row Count = 1 FAILED",1, sugar().knowledgeBase.listView.countRows());
		sugar().knowledgeBase.listView.verifyField(1, "name", sugar().knowledgeBase.getDefaultData().get("name"));

		// Verify custom-filter-name is shown in Current Custom Filter
		sugar().knowledgeBase.listView.getControl("searchFilterCurrent").assertContains(testName, true);
		// Verify custom-filter-name is shown Highlighted in Filters dropDown
		sugar().knowledgeBase.listView.openFilterDropdown();
		new VoodooControl("li","css",
				".select2-results-dept-0.select2-result.select2-result-selectable.select2-highlighted").assertContains(testName, true);
		// Verify custom-filter-name is shown on list at no. 2 position (under Create) in Filters dropDown
		new VoodooControl("li","css",".select2-results li:nth-child(2)").assertContains(testName, true);
	}

	public void cleanup() throws Exception {}
}