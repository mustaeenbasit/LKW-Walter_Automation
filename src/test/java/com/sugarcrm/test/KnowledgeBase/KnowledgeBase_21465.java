package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21465 extends SugarTest {
	DataSource kbCategoriesData = new DataSource();

	public void setup() throws Exception {
		kbCategoriesData = testData.get(testName);

		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		// Controls for Category
		VoodooControl categoryNameCtrl = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");
		VoodooControl addNewCategoryCtrl = new VoodooControl("span", "css", ".btn.jstree-addnode .fa.fa-plus");

		// Creating KB record
		sugar().knowledgeBase.api.create();

		// Login as Admin
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		// Creating 1 parent category
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		new VoodooControl("a", "css", ".fld_add_node_button a").click();
		VoodooUtils.waitForReady();
		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		categoryNameCtrl.set(kbCategoriesData.get(0).get("parentCategory") + '\uE007');
		VoodooUtils.waitForReady();

		// Adding 3 child node to created parent category
		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		for (int i = 0; i < kbCategoriesData.size(); i++) {
			addNewCategoryCtrl.click();
			VoodooUtils.waitForReady();
			// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
			categoryNameCtrl.set(kbCategoriesData.get(i).get("childCategory") + '\uE007');
			VoodooUtils.waitForReady();
		}

		// Cancel the category create drawer
		new VoodooControl("a", "css", ".fld_close a").click();

		// Moving one article in one child node.
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.getEditField("category").click();
		new VoodooControl("ins", "css", ".jstree-icon._parse_json_outer").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "[style=''] [data-action='jstree-select']").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.recordView.save();
	}

	/**
	 * Verify that child nodes can be deleted from Knowledge Base Admin
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21465_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		VoodooControl nodeChild1Ctrl = new VoodooControl("span", "css", ".jstree-last.jstree-open li a");
		VoodooControl nodeChild2Ctrl = new VoodooControl("span", "css", ".jstree-last.jstree-open li:nth-child(2) a");
		VoodooControl nodeChild3Ctrl = new VoodooControl("span", "css", ".jstree-last.jstree-open li:nth-child(3) a");
		VoodooControl childCategoryDltCtrl = new VoodooControl("a", "css", "[rel='delete']");
		VoodooControl childCategoriesCtrl = new VoodooControl("ul", "css", ".jstree-last.jstree-open ul");

		// Navigate to View Category page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Expanding Parent Category to see child category
		new VoodooControl("ins", "css", ".layout_Categories .jstree-icon._parse_json_outer").click();

		// Deleting 3rd child category i.e child3 -> No article associated with this child category
		new VoodooControl("span", "css", ".jstree-last.jstree-open li:nth-child(3) .fa.fa-caret-down").click();
		childCategoryDltCtrl.click();
		sugar().alerts.getWarning().confirmAlert();

		// Verifying 3rd child category is deleted
		nodeChild3Ctrl.assertExists(false);
		childCategoriesCtrl.assertContains(kbCategoriesData.get(2).get("childCategory"), false);

		// Deleting 2nd child category
		new VoodooControl("span", "css", ".jstree-last.jstree-open li:nth-child(2) .fa.fa-caret-down").click();
		childCategoryDltCtrl.click();
		sugar().alerts.getWarning().cancelAlert();

		// Verifying 2nd child category is not deleted
		nodeChild2Ctrl.assertExists(true);
		nodeChild2Ctrl.assertEquals(kbCategoriesData.get(1).get("childCategory"), true);

		// Delete a child1 category having articles
		new VoodooControl("span", "css", ".jstree-last.jstree-open li .fa.fa-caret-down").click();
		childCategoryDltCtrl.click();
		sugar().alerts.getWarning().confirmAlert();

		// Verifying child1 category is deleted and child2 category is appearing at first row
		nodeChild1Ctrl.assertEquals(kbCategoriesData.get(0).get("childCategory"), false);
		nodeChild1Ctrl.assertEquals(kbCategoriesData.get(1).get("childCategory"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}