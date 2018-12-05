package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29624 extends SugarTest {
	DataSource categoryData = new DataSource();
	VoodooControl moduleCtrl, studioCtrl;

	public void setup() throws Exception {
		categoryData = testData.get(testName);

		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Define controls for the KB category page
		// TODO: VOOD-1754
		VoodooControl addBtnCtrl = new VoodooControl("a", "css", "a[name='add_node_button']");
		VoodooControl categoryNameCtrl = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");		
		VoodooControl categoriesLayoutCtrl = new VoodooControl("div", "css", ".layout_Categories .nested-set-full");
		String parentCategoryName = sugar().documents.moduleNamePlural;

		// Create nested categories. such as Documents/nest1/nest2/nest3/nest4, Documents/Calls/call1/call2 and Documents/email
		// Create four categories Named 'Documents'
		for(int i = 0; i < categoryData.size(); i++) {
			addBtnCtrl.click();
			VoodooUtils.waitForReady();
			// TODO: VOOD-1437 and CB-252
			categoryNameCtrl.set(parentCategoryName + '\uE007');		
			VoodooUtils.waitForReady();
		}

		// Create nested categories
		for(int i = 0; i < categoryData.size(); i++) {
			new VoodooControl("span", "css", categoriesLayoutCtrl.getHookString() + " .jstree-leaf:nth-child(" + (i+1) + ") .jstree-addnode span").click();
			for(int j = 0; j < categoryData.size(); j++) {
				// TODO: VOOD-1437 and CB-252
				categoryNameCtrl.set(categoryData.get(j).get("categoryName" + "_" + i) + '\uE007');
				VoodooUtils.waitForReady();
				if((j < categoryData.size() - 1)) {
					if(categoryData.get(j+1).get("categoryName" + "_" + i).equals("null")) {
						break;
					} else {
						new VoodooControl("span", "css", categoriesLayoutCtrl.getHookString() + " li:nth-child(" + (i+1) + ") .jstree-leaf .jstree-addnode span").click();
					}
					VoodooUtils.waitForReady();
				}
			}
		}
	}

	/**
	 * Verify that user can select Category in KB create form when re-select multiple times
	 *   
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29624_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view and create a new KB record
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();

		// Define controls for KB create drawer
		// TODO: VOOD-1754
		VoodooControl categoryRecordViewCtrl = sugar().knowledgeBase.createDrawer.getEditField("category");
		VoodooControl selectCategoryCtrl = new VoodooControl("b", "css", categoryRecordViewCtrl.getHookString() + " .select-arrow b");
		VoodooControl clearCategoryCtrl = new VoodooControl("i", "css", categoryRecordViewCtrl.getHookString() + " .clear-field i");

		// During creation, in Category field, try to select one existing Category by clicking on it.  e.g. expand Documents node, expend Calls/call1/call2, select call2.  Now call2 appears in the Category field.
		selectCategoryCtrl.click();
		// TODO: VOOD-629 and VOOD-1754
		VoodooControl callsCategoryCtrl = new VoodooControl("li", "css", ".jstree-sugar.tree-component li:nth-child(2)");
		new VoodooControl("ins", "css", callsCategoryCtrl.getHookString() + " ins").click();
		new VoodooControl("ins", "css", callsCategoryCtrl.getHookString() + " ul ins").click();
		new VoodooControl("ins", "css", callsCategoryCtrl.getHookString() + " ul ul ins").click();
		new VoodooControl("a", "css", callsCategoryCtrl.getHookString() + " ul ul ul a").click();
		VoodooUtils.waitForReady();

		// Verify that the selected category is displayed
		categoryRecordViewCtrl.assertEquals(categoryData.get(2).get("categoryName_1"), true);

		// Click on x in Category field to DE-select call2
		clearCategoryCtrl.click();
		VoodooUtils.waitForReady();

		// Click on 'Select Category' again to select Calendars
		selectCategoryCtrl.click();
		VoodooControl calendarsCategoryCtrl = new VoodooControl("li", "css", ".jstree-sugar.tree-component li:nth-child(3)");
		new VoodooControl("ins", "css", calendarsCategoryCtrl.getHookString() + " ins").click();
		new VoodooControl("ins", "css", calendarsCategoryCtrl.getHookString() + " ul a").click();
		VoodooUtils.waitForReady();

		// Verify that the selected category is displayed
		categoryRecordViewCtrl.assertEquals(categoryData.get(0).get("categoryName_2"), true);

		// If it is selected, then De-Select it again, try to re-select another one, such as "email sent by today"
		clearCategoryCtrl.click();
		VoodooUtils.waitForReady();
		selectCategoryCtrl.click();
		VoodooControl emailCategoryCtrl = new VoodooControl("li", "css", ".jstree-sugar.tree-component li:nth-child(4)");
		new VoodooControl("ins", "css", emailCategoryCtrl.getHookString() + " ins").click();
		new VoodooControl("ins", "css", emailCategoryCtrl.getHookString() + " ul a").click();
		VoodooUtils.waitForReady();

		// Verify that the selected category is displayed
		categoryRecordViewCtrl.assertEquals(categoryData.get(0).get("categoryName_3"), true);

		// Cancel the create drawer
		sugar().knowledgeBase.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}