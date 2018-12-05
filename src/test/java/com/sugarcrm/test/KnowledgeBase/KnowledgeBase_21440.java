package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21440 extends SugarTest {
	FieldSet categoryData = new FieldSet();

	public void setup() throws Exception {
		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Creating two nodes with same name in different paths and two nodes where one is subset of other
		// TODO: VOOD-1754 and VOOD-1437
		VoodooControl createCategoryCtrl = new VoodooControl("a", "css", "a[name='add_node_button']");
		createCategoryCtrl.click();
		categoryData = testData.get(testName).get(0);
		VoodooControl categoryNameCtrl = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");
		categoryNameCtrl.set(categoryData.get("parentCategory1") + '\uE007');
		VoodooUtils.waitForReady();
		VoodooControl addNewSubCategory1 = new VoodooControl("a", "css", ".tree-block.nested-set-full .btn.jstree-addnode");
		addNewSubCategory1.click();
		categoryNameCtrl.set(categoryData.get("subCategory1") + '\uE007');
		addNewSubCategory1.click();
		categoryNameCtrl.set(categoryData.get("subCategory2") + '\uE007');
		createCategoryCtrl.click();
		categoryNameCtrl.set(categoryData.get("parentCategory2") + '\uE007');
		VoodooUtils.waitForReady();
		VoodooControl addNewSubCategory2 = new VoodooControl("a", "css", ".jstree-last:nth-of-type(2)[data-level='1'] .jstree-addnode");
		addNewSubCategory2.click();
		categoryNameCtrl.set(categoryData.get("subCategory1") + '\uE007');
		addNewSubCategory2.click();
		categoryNameCtrl.set(categoryData.get("subCategory3") + '\uE007');
	}

	/**
	 * Verify that KB Categories can be correctly searched in KB's create drawer 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21440_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB create drawer
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("category").click();

		// Verify the nodes with same name in different path can be searched 
		// TODO: VOOD-1754, CB-252
		String commonNode = categoryData.get("subCategory1");
		VoodooControl searchCtrl  = new VoodooControl("input", "css", ".fld_category_name.edit .dropdown-menu input");
		searchCtrl.set(commonNode + '\uE007');
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".jstree-focused ul li ul li a").assertEquals(commonNode, true);
		new VoodooControl("a", "css", ".jstree-focused ul li:nth-child(2) ul li a").assertEquals(commonNode, true);

		// Verify the nodes where one is subset of other can be searched correctly
		String subsetNode = categoryData.get("subCategory2");
		searchCtrl.set(subsetNode + '\uE007');
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".jstree-focused ul li ul li:nth-child(2) a").assertEquals(subsetNode, true);
		new VoodooControl("a", "css", ".jstree-focused ul li:nth-child(2) ul li:nth-child(2) a").assertEquals(categoryData.get("subCategory3"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}