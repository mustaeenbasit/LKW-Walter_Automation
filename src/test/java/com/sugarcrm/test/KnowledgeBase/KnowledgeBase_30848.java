package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30848 extends SugarTest {

	public void setup() throws Exception {
		// Login as Admin user
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Create a Category
		// TODO: VOOD-1754 Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(testName + '\uE007');
		VoodooUtils.waitForReady();		
	}

	/**
	 * Verify that User should able to click external article checkbox after editing category field in knowledge base module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30848_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB
		sugar().knowledgeBase.navToListView();

		// Click create button from list view
		sugar().knowledgeBase.listView.create();

		// Click "Show more..." link
		sugar().knowledgeBase.createDrawer.showMore();

		// Enter KB name
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);

		// Select any category
		sugar().knowledgeBase.createDrawer.getEditField("category").click();
		// TODO: VOOD-1754 Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		new VoodooControl("a", "css", ".jstree-focused li a").click();

		// Click External Article checkbox
		VoodooControl externalArticleCheckboxCtrl = sugar().knowledgeBase.createDrawer.getEditField("isExternal");
		externalArticleCheckboxCtrl.click();

		// Verify that "External Article" checkbox is checked
		Assert.assertTrue("External Article checkbox is not checked", externalArticleCheckboxCtrl.isChecked());

		// Deselect External Article checkbox
		externalArticleCheckboxCtrl.click();

		// Verify that "External Article" checkbox is unchecked
		Assert.assertFalse("External Article checkbox is checked", externalArticleCheckboxCtrl.isChecked());

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
} 