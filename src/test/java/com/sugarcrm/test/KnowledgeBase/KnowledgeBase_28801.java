package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28801 extends SugarTest {

	public void setup() throws Exception {
		DataSource kbRecords = testData.get(testName+"_cat_data");
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Go to KB Categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// TODO: VOOD-1754 Need Lib support for Categories in KnowledgeBase Module
		new VoodooControl("a", "css", ".fld_add_node_button a").click();

		// Entering KB Category name and enter
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(kbRecords.get(0).get("category") + '\ue007');
		VoodooUtils.waitForReady();

		// Create a new sub-category CAT1.1 inside CAT1
		new VoodooControl("span", "css", ".tree-component ul li a span.fa.fa-plus").click();
		new VoodooControl("input", "css", ".tree-component ul li input.jstree-rename-input:not([style *='display: none'])").set(kbRecords.get(1).get("category") + '\ue007');
		VoodooUtils.waitForReady();
		
		// Create a new sub-category CAT1.1.1 inside CAT1.1
		new VoodooControl("span", "css", ".tree-component ul li ul li a span.fa.fa-plus").click();
		new VoodooControl("input", "css", ".tree-component ul li ul li input.jstree-rename-input:not([style *='display: none'])").set(kbRecords.get(2).get("category") + '\ue007');
		VoodooUtils.waitForReady();
	}

	/**
	 * Knowledge Base: Verify that user can add categories and they are correctly nested
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28801_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB module List View
		sugar().knowledgeBase.navToListView();
		FieldSet customFS  = testData.get(testName).get(0);

		// Create a new Published article and assign it to CAT1.1.1 category
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("status").set(customFS.get("status"));
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("category").click();

		// TODO: VOOD-1754
		// Select CAT1.1.1
		new VoodooControl("ins", "css", ".dropdown-menu .jstree li ins.jstree-icon._parse_json_outer").click();
		new VoodooControl("ins", "css", ".dropdown-menu .jstree li ul li ins.jstree-icon._parse_json_outer").click();
		new VoodooControl("ins", "css", ".dropdown-menu .jstree li ul li ul li a").click();

		// Save create KB
		sugar().knowledgeBase.createDrawer.save();

		// Select My Dashboard from the right hand side
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(customFS.get("dashboardTitle"), true))
			sugar().dashboard.chooseDashboard(customFS.get("dashboardTitle"));

		// TODO: VOOD-591
		// Observe a categories dashlet and try to expand CAT1 category to make the created article visible
		new VoodooControl("ins", "css", "[data-place='dashlet-tree'] ul li ins.jstree-icon._parse_json_outer").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ins", "css", "[data-place='dashlet-tree'] ul li ul li ins.jstree-icon._parse_json_outer").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ins", "css", "[data-place='dashlet-tree'] ul li ul li ul li ins.jstree-icon._parse_json_outer").click();
		VoodooUtils.waitForReady(30000);

		// Verify that the Categories tree is correctly expanded. Created article is visible in dashlet.
		new VoodooControl("a", "css", "[data-place='dashlet-tree'] ul li ul li ul li ul li a").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}