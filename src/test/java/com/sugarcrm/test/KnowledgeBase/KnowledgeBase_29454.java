package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29454 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Login as an Admin
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB -> Settings
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");
		// Add a new language - fr | French
		// TODO: VOOD-1762 - Need library support for adding/removing languages in Knowledge Base
		new VoodooControl("button", "css", "button[name='add']").click();
		new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(2) div div input").set(customData.get("languageCode"));
		new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(2) div div:nth-child(2) input").set(customData.get("languageLabel"));

		// Save
		new VoodooControl("a", "css", ".fld_main_dropdown [name='save_button']").click();
		VoodooUtils.waitForReady();

		// Logout from Admin and Login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that custom category can be associated with the KB record  
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29454_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Published KB without a Category
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customData.get("name"));
		sugar().knowledgeBase.createDrawer.getEditField("status").set(customData.get("status"));
		sugar().knowledgeBase.createDrawer.save();

		// Select My Dashboard from the right hand side
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(customData.get("dashboardTitle"), true)) {
			sugar().dashboard.chooseDashboard(customData.get("dashboardTitle"));
		}

		// TODO: VOOD-976 - need lib support of RHS on record view
		VoodooControl dashletTitleCtrl = new VoodooControl("h4", "css", ".dashlet-row .row-fluid .dashlet-title");
		VoodooControl noDataAvailableCtrl = new VoodooControl("div", "css", ".dashlet-row .row-fluid .block-footer:not([style *='display: none'])");

		// Verify that in the KB list view, at the right hand side, by default, there is the "KB Categories" Dashlet
		dashletTitleCtrl.assertEquals(customData.get("dashletTitle"), true);

		// Verify that the above KB won't appear in the Dashlet
		noDataAvailableCtrl.assertEquals(customData.get("noDataAvailable"), true);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();
		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		// Create Category 
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(customData.get("categoryName")+'\uE007');		
		VoodooUtils.waitForReady();

		// In the list view, choose "Create a Localization" action from the above KB action list
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.openRowActionDropdown(1);
		// TODO: VOOD-695 - Lib support for primary button drop down
		new VoodooControl("a", "css", ".fld_create_localization_button a").click();

		// Now the create form opens, user can set the Status = Published, click on "Select Category"
		sugar().knowledgeBase.createDrawer.assertVisible(true);
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customData.get("localName"));
		sugar().knowledgeBase.createDrawer.getEditField("status").set(customData.get("status"));

		// Click on "Category" link, enter a new category name, e.g "new test category" and hit enter
		// The above step is blocked due to CB-252 and as the test case motive is not to verify the create Category functionality
		// so link existing Category instead of create new

		// Select this category for the Localization KB
		// TODO: VOOD-629 - Add support for accessing and manipulating individual components of a VoodooSelect.
		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		new VoodooControl("div", "css", ".fld_category_name.edit div").click();
		new VoodooControl("a", "css", ".list .jstree-sugar.tree-component a").click();

		// Click on Save
		sugar().knowledgeBase.createDrawer.save();

		sugar().knowledgeBase.navToListView();

		// Sort the list view in ascending order as order is not consistent
		sugar().knowledgeBase.listView.sortBy("headerName", true);

		// Verify that the new Localized KB is saved, also the Category appears
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(customData.get("localName"), true);
		// TODO: VOOD-1754 - Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		new VoodooControl("a", "css", ".fld_category_name a").assertEquals(customData.get("categoryName"), true);

		// Verify that the new custom category is appearing, the KB record is associated with it
		// TODO: VOOD-976 - need lib support of RHS on record view
		VoodooControl dashletDataCtrl = new VoodooControl("a", "css", ".dashlet-row .row-fluid .jstree-sugar.tree-component a[data-action='jstree-select']");
		dashletDataCtrl.assertContains(customData.get("categoryName"), true);

		VoodooControl localKBFolderCtrl = new VoodooControl("a", "css", ".dashlet-row .row-fluid .jstree-sugar.tree-component > ul > li > a");
		VoodooControl localKBCtrl = new VoodooControl("a", "css", ".dashlet-row .row-fluid .jstree-sugar.tree-component .jstree-leaf a");
		localKBFolderCtrl.click();
		VoodooUtils.waitForReady();
		localKBCtrl.waitForVisible(); // wait needed
		localKBCtrl.assertContains(customData.get("localName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}