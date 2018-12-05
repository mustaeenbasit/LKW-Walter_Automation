package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29658 extends SugarTest {
	DataSource customDS, kbData = new DataSource();

	public void setup() throws Exception {
		kbData = testData.get(testName+"_data");
		customDS = testData.get(testName);
		sugar().knowledgeBase.api.create(kbData);
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Go to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Create Three Category 
		VoodooControl addButtonCtrl = new VoodooControl("a", "css", "a[name='add_node_button']");
		VoodooControl dropDownLitst = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");
		for(int i = 0; i < customDS.size(); i++) {
			// TODO: VOOD-1754
			addButtonCtrl.click();
			VoodooUtils.waitForReady();

			// TODO: VOOD-1437 and CB-252
			dropDownLitst.set(customDS.get(i).get("categoryName") + '\uE007');		
			VoodooUtils.waitForReady();
		}

		// Create KB records with help, call and video category.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		VoodooControl categoryField = sugar().knowledgeBase.recordView.getEditField("category");
		for(int i = 0; i < kbData.size(); i++) {
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.showMore();
			categoryField.click();

			// Select Category
			// TODO: VOOD-1754
			new VoodooControl("a", "css", ".list .jstree-sugar.tree-component li:nth-child(" + (i+1) + ") a").click();

			// Click on Save
			sugar().knowledgeBase.recordView.save();
			sugar().knowledgeBase.recordView.gotoNextRecord();
		}
	}

	/**
	 * Verify user can sort by Category after using filter
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29658_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB listView
		sugar().knowledgeBase.navToListView();

		// Create new Filter with field 'Category, Operator as 'is not', Filter value as 'help'.
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// TODO: VOOD-1785
		new VoodooSelect("div", "css", "div[data-filter='field']").set(customDS.get(0).get("displayName"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(customDS.get(0).get("operator"));
		new VoodooControl("input", "css", "input[name='kbdocument_body']").set(kbData.get(0).get("body"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[data-voodoo-name='filter-actions'] input").set(testName);
		sugar().knowledgeBase.listView.filterCreate.getControl("saveButton").click();
		VoodooUtils.waitForReady();

		// Verify that the filter work perfectly
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbData.get(2).get("name"));

		// Sort by Category (click Category column)
		sugar().knowledgeBase.listView.sortBy("headerCategoryname", true);

		// Verify that the filter is correctly applied, and user is able to successfully sort by Category.
		sugar().knowledgeBase.listView.verifyField(1, "name", kbData.get(2).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", kbData.get(1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}