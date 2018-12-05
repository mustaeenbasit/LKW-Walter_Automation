package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29450 extends SugarTest {
	DataSource customData = new DataSource();
	VoodooControl selectCustomFilter, filterName;

	public void setup() throws Exception {
		customData = testData.get(testName);

		// Login as an Admin
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// TODO: VOOD-1754
		// Create two categories: Calls and Meeting
		for(int i = 0; i < customData.size(); i++){
			new VoodooControl("a", "css", ".layout_Categories.active a[name='add_node_button']").click();
			VoodooUtils.waitForReady();
			// TODO: CB-252
			new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(customData.get(i).get("name")+'\uE007');
			VoodooUtils.waitForReady();
		}

		// Create KB records with Calls and Meeting category.
		sugar().knowledgeBase.navToListView();
		for(int i = 0; i < customData.size(); i++) {
			sugar().knowledgeBase.listView.create();
			sugar().knowledgeBase.createDrawer.showMore();
			sugar().knowledgeBase.createDrawer.getEditField("name").set(customData.get(i).get("kbName"));

			// Select this category for the Localization KB
			// TODO: VOOD-629 and VOOD-1754
			new VoodooControl("div", "css", ".fld_category_name.edit div").click();
			new VoodooControl("a", "css", ".list .jstree-sugar.tree-component li:nth-child(" + (i+1) + ") a").click();

			// Click on Save
			sugar().knowledgeBase.createDrawer.save();
		}
	}

	/**
	 * Verify that create a filter on sub-category in KB list view correctly 
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29450_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create new Filter with field 'Category, Operator as 'is', Filter value as 'Calls'.
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();
		// TODO: VOOD-1832
		selectCustomFilter = new VoodooControl("div", "css", ".search-filter-dropdown li:nth-child(2)");
		new VoodooSelect("div", "css", "div[data-filter='field']").set(customData.get(0).get("displayName"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(customData.get(0).get("operator"));
		new VoodooControl("input", "css", "div[name='category_name']").click();
		new VoodooControl("a", "css", ".fld_category_name .dropdown-menu .list li:nth-child(1) a").click();
		VoodooUtils.waitForReady();

		// Enter a name of the filter and Save it
		new VoodooControl("input", "css", "div[data-voodoo-name='filter-actions'] input").set(customData.get(0).get("filterField"));
		VoodooUtils.waitForReady(); // wait needed
		new VoodooControl("a", "css", ".btn-primary.save_button").click();
		VoodooUtils.waitForReady();

		// Verify that the correct set of KB records with Calls Category is filtered and saved
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(customData.get(0).get("kbName"), true);
		Assert.assertTrue("Correctly filtered KB records, with Calls Category, do not appear", sugar().knowledgeBase.listView.countRows() == 1);

		// Click on the filter to dismiss it in the Filter header.
		// TODO: VOOD-1832
		new VoodooControl("i", "css", ".choice-filter-close i").click();
		VoodooUtils.waitForReady();

		// Verify that all records are listed
		sugar().knowledgeBase.listView.getDetailField(2, "name").assertEquals(customData.get(0).get("kbName"), true);
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(customData.get(1).get("kbName"), true);
		Assert.assertTrue("All KB records, without any filter, are not listed", sugar().knowledgeBase.listView.countRows() == 2);

		// Click on Filter drop down to select that custom filter
		sugar().contacts.listView.openFilterDropdown();
		selectCustomFilter.click();
		VoodooUtils.waitForReady();

		// Verify that the custom filter appears again with the correct KB record set
		// TODO: VOOD-1832
		filterName = new VoodooControl("span", "css", ".choice-filter-label");
		filterName.assertEquals(customData.get(0).get("filterField"), true);
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(customData.get(0).get("kbName"), true);
		Assert.assertTrue("Correctly filtered KB records, with Calls Category, do not appear", sugar().knowledgeBase.listView.countRows() == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
