package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28800 extends SugarTest {
	DataSource categoryKB = new DataSource();
	int rowCount = 0;

	public void setup() throws Exception {
		categoryKB = testData.get(testName);
		rowCount = categoryKB.size();	
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// TODO: VOOD-1754 - Need Lib support for Categories in Knowledge Base Module
		// Create 5 different Categories
		VoodooControl createCategoryBtn = new VoodooControl("a", "css", "a[name='add_node_button']");
		VoodooControl categoryNameFld = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");
		for(int i=0; i<rowCount; i++){
			createCategoryBtn.click();
			VoodooUtils.waitForReady();
			categoryNameFld.set(categoryKB.get(i).get("categoryName")+'\uE007');
			VoodooUtils.waitForReady();
		}

		// Navigate to the Knowledge Base list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		VoodooControl nameKB = sugar().knowledgeBase.createDrawer.getEditField("name");
		VoodooControl editCategory = sugar().knowledgeBase.createDrawer.getEditField("category");;

		// Create 5 knowledge base records with different Categories
		for(int j=1; j<=rowCount; j++){
			sugar().knowledgeBase.listView.create();
			nameKB.set(categoryKB.get(j-1).get("categoryName"));
			sugar().knowledgeBase.createDrawer.showMore();

			// Select the category in the knowledge base createDrawer
			editCategory.click();
			// TODO: VOOD-1754
			new VoodooControl("a", "css", ".jstree-focused li:nth-child("+ j +") a").click();
			sugar().knowledgeBase.createDrawer.save();
		}
	}

	/**
	 * Knowledge Base: Verify user can correctly sort list view by category, and complete action in list view
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28800_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sort Category in Descending order
		// TODO: VOOD-1768
		VoodooControl headerCategory = new VoodooControl("th", "css", ".orderBycategory_name");
		headerCategory.click();
		VoodooUtils.waitForReady();
		headerCategory.assertAttribute("class", "sorting_desc", true);

		// Assert that the Category names are sorted in Descending order
		for(int i = 1; i <= rowCount; i++)
			sugar().knowledgeBase.listView.getDetailField(i, "category").assertEquals(categoryKB.get(rowCount-i).get("categoryName"), true);

		// Sort Category in Ascending order
		headerCategory.click();
		VoodooUtils.waitForReady();
		headerCategory.assertAttribute("class", "sorting_asc", true);

		// Assert that the Category names are sorted in Ascending order
		for(int i = 1; i <= rowCount; i++)
			sugar().knowledgeBase.listView.getDetailField(i, "category").assertEquals(categoryKB.get(i-1).get("categoryName"), true);

		// Select all the KB records and open action drop down
		sugar().knowledgeBase.listView.toggleSelectAll();
		sugar().knowledgeBase.listView.openActionDropdown();

		// TODO: VOOD-657 - Listview needs to provide access to all drop down menus
		// Assert that the "Merge" option is not available in the Knowledge Base list view action drop down
		new VoodooControl("ul", "css", ".actionmenu .dropdown-menu").assertContains(categoryKB.get(0).get("actionMerge"), false);

		// Perform Delete action on the selected KB articles
		sugar().knowledgeBase.listView.delete();
		sugar().knowledgeBase.listView.confirmDelete();
		VoodooUtils.waitForReady();

		// Assert that all the records have been deleted.
		sugar().knowledgeBase.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}