package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30150 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName);

		// TODO: VOOD-1754
		// Lib support for Categories in KB
		VoodooControl kbCategoryCreateBtn = new VoodooControl("a", "css", ".layout_Categories.active a[name='add_node_button']");
		VoodooControl kbCategoryFieldCtrl = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");

		// Login as an Admin
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// TODO: VOOD-1754
		// Create three categories: help, calls and video
		for(int i = 0; i < customData.size(); i++) {
			kbCategoryCreateBtn.click();
			VoodooUtils.waitForReady();

			// TODO: CB-252
			kbCategoryFieldCtrl.set(customData.get(i).get("categoryName")+'\uE007');
			VoodooUtils.waitForReady();
		}

		// Create KB records with help, call and video category.
		sugar().knowledgeBase.navToListView();
		for(int i = 0; i < customData.size(); i++) {
			sugar().knowledgeBase.listView.create();
			sugar().knowledgeBase.createDrawer.showMore();
			sugar().knowledgeBase.createDrawer.getEditField("name").set(customData.get(i).get("kbName"));
			sugar().knowledgeBase.recordView.getEditField("category").click();

			// Selecting the category
			// TODO: VOOD-1754
			new VoodooControl("a", "css", ".list .jstree-sugar.tree-component li:nth-child(" + (i+1) + ") a").click();

			// Click on Save
			sugar().knowledgeBase.createDrawer.save();
		}
	}

	/**
	 * Verify Action dropdown and checkboxes in KB list view should work on applying filter on Category column
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30150_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create new Filter with field 'Category, Operator as 'is not', Filter value as 'help'.
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// TODO: VOOD-1785
		new VoodooSelect("div", "css", "div[data-filter='field']").set(customData.get(0).get("displayName"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(customData.get(0).get("operator"));
		new VoodooControl("input", "css", "div[name='category_name']").click();
		new VoodooControl("a", "css", ".fld_category_name .dropdown-menu .list li a").click();
		VoodooUtils.waitForReady();

		for (int i = 1; i <= sugar().knowledgeBase.listView.countRows(); i++) {
			// Verifying Action dropdown is opened for each row
			sugar().knowledgeBase.listView.openRowActionDropdown(i);
			sugar().knowledgeBase.listView.getControl(String.format("edit%02d", i)).assertVisible(true);

			// Verifying select checkbox is get selected for each row
			sugar().knowledgeBase.listView.checkRecord(i);
			sugar().knowledgeBase.listView.getControl(String.format("checkbox%02d", i)).assertChecked(true);
		}

		// Verifying toggle all action dropdown is opened
		sugar().knowledgeBase.listView.openActionDropdown();
		sugar().knowledgeBase.listView.getControl("deleteButton").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
