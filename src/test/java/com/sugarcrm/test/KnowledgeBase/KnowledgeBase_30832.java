package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30832 extends SugarTest {

	public void setup() throws Exception {
		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Create one category
		// TODO: VOOD-1754 and VOOD-1437
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(testName + '\uE007');
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that Navbar dropdown, Profile Icon dropdown and Quick Create should not becomes unresponsive while creating Knowledge Base Article using Category
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30832_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Navigate to Knowledge Base from navbar
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		// Now Click on Create button 
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		// In the KB create drawer enter the Name and Select Category
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("category").click();
		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer
		new VoodooControl("a", "css", ".jstree-focused li a").click();

		// Now Click on Navbar Dropdown, Profile Icon and Quick Create to assert that these are responsive
		sugar().navbar.clickModuleDropdown(sugar.knowledgeBase);
		sugar().knowledgeBase.menu.getControl("createArticle").assertVisible(true);
		sugar().navbar.toggleUserActionsMenu();
		sugar().navbar.openQuickCreateMenu();

		// Save the Record
		sugar().knowledgeBase.createDrawer.save();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}