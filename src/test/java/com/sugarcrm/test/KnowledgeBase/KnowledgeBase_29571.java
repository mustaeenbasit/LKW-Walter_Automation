package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29571 extends SugarTest {
	FieldSet categoryKBData = new FieldSet();

	public void setup() throws Exception {
		categoryKBData = testData.get(testName).get(0);

		// KB with without category exists
		// TODO: VOOD-444
		sugar().knowledgeBase.api.create();

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Create a Category
		// TODO: VOOD-1754
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(categoryKBData.get("categoryName")+'\uE007');
		VoodooUtils.waitForReady();

		// KB with category category exists
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("category").click();
		// TODO: VOOD-1754
		new VoodooControl("a", "css", ".jstree-focused li a").click();
		sugar().knowledgeBase.createDrawer.save();
	}

	/**
	 * Verify filter is working correctly with category is empty or is not empty
	 *  
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29571_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls for KB filter
		// TODO: VOOD-1785
		VoodooControl kbFilterFieldCtrl = new VoodooSelect("div", "css", "div[data-filter='field']");
		VoodooControl kbFilterOperatorFieldCtrl = new VoodooSelect("div", "css", "div[data-filter='operator']");

		// Define controls for KB list view
		VoodooControl listViewNameCtrl = sugar().knowledgeBase.listView.getDetailField(1, "name");
		VoodooControl listViewCategoryCtrl = sugar().knowledgeBase.listView.getDetailField(1, "category");

		try {
			// In list view, create new Filter
			sugar().knowledgeBase.listView.openFilterDropdown();
			sugar().knowledgeBase.listView.selectFilterCreateNew();

			// Set field 'Category', Operator set to 'is empty'
			kbFilterFieldCtrl.set(categoryKBData.get("displayName"));
			kbFilterOperatorFieldCtrl.set(categoryKBData.get("operatorEmpty"));
			VoodooUtils.waitForReady();

			// Verify that the Knowledge Base record with empty Category is listed according to filter
			listViewNameCtrl.assertEquals(sugar().knowledgeBase.getDefaultData().get("name"), true);
			listViewCategoryCtrl.assertContains("", true);

			// Set field 'Category', Operator set to 'is not empty'
			kbFilterOperatorFieldCtrl.set(categoryKBData.get("operatorNotEmpty"));
			VoodooUtils.waitForReady();

			// Verify that the Knowledge Base record with empty Category is listed according to filter
			listViewNameCtrl.assertEquals(testName, true);
			listViewCategoryCtrl.assertContains(categoryKBData.get("categoryName"), true);
		}
		// Use try finally to cancel filter create view, so no need to close the filter create view in clean up method
		finally {
			// Cancel the filter opened create view
			sugar().knowledgeBase.listView.filterCreate.cancel();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}