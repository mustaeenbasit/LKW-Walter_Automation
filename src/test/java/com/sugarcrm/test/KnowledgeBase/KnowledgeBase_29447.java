package com.sugarcrm.test.KnowledgeBase;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29447 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that auto-refresh in "KB Cateory" Dashlet when create a Localization or Revision from KB Listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29447_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB categories listView
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// TODO: VOOD-1754
		// Create a category
		new VoodooControl("a", "css", ".layout_Categories.active a[name='add_node_button']").click();
		VoodooUtils.waitForReady();

		// TODO: CB-252
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(testName+'\uE007');
		VoodooUtils.waitForReady();

		// Navigate to the Knowledge Base list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Create knowledge base record
		FieldSet customFS = testData.get(testName).get(0);
		sugar().knowledgeBase.listView.create();
		VoodooControl nameCtrl = sugar().knowledgeBase.createDrawer.getEditField("name");
		VoodooControl statusCtrl = sugar().knowledgeBase.createDrawer.getEditField("status");
		nameCtrl.set(customFS.get("kb_name"));
		statusCtrl.set(customFS.get("kb_status"));
		sugar().knowledgeBase.createDrawer.showMore();

		// Select the category in the knowledge base createDrawer
		sugar().knowledgeBase.createDrawer.getEditField("category").click();
		// TODO: VOOD-1754
		new VoodooControl("a", "css", ".jstree-focused li:nth-child(1) a").click();
		sugar().knowledgeBase.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Use of queryContains
		VoodooControl dashboard = sugar().accounts.dashboard.getControl("dashboard");
		if(!dashboard.queryContains(customFS.get("dashBoardTitle"), true))
			sugar().cases.dashboard.chooseDashboard(customFS.get("dashBoardTitle"));

		// TODO: VOOD-591
		// Click on Dashlet > Category 
		VoodooControl kbDashLetCtrl = new VoodooControl("a", "css", "div.jstree ul li.jstree-closed [data-action='jstree-select']");
		kbDashLetCtrl.click();
		VoodooUtils.waitForReady();

		// The newly created revision record appear in the KB Dashlet underneath the same Category node.
		VoodooControl kbNameDashlet = new VoodooControl("a", "css", "div.jstree ul li.jstree-open ul li a");
		kbNameDashlet.waitForVisible();
		kbNameDashlet.assertContains(customFS.get("kb_name"), true);

		// Create a revision KB from above KB in the listview, set status=published
		sugar().knowledgeBase.listView.openRowActionDropdown(1);

		// TODO: VOOD-1760
		new VoodooControl("span", "css", ".list.fld_create_revision_button").click();

		// Update name
		nameCtrl.set(customFS.get("kb_revisionName"));
		statusCtrl.set(customFS.get("kb_status"));
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Click on Dashlet > Category 
		kbDashLetCtrl.click();
		VoodooUtils.waitForReady();

		// The newly created revision record appear in the KB Dashlet underneath the same Category node. 
		kbNameDashlet.waitForVisible();
		kbNameDashlet.assertContains(customFS.get("kb_revisionName"), true);

		// Verify that the original KB record won't appear in that node any more
		new VoodooControl("a", "css", "div.jstree ul li.jstree-open ul li:nth-child(2)").assertExists(false);

		// Go to KB module list view -> Settings for create a new Language
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// TODO: VOOD-1843 - Once resolved it should replaced by getChildElement
		VoodooControl language = new VoodooControl("div", "css", "div:nth-child(2)[data-name='languages_languages']");

		// Try to create a new language set. Enter space in language code and language name. Click on Save.
		// TODO: VOOD-1762
		new VoodooControl("button", "css", "button[name='add']").click();
		new VoodooControl("input", "css", language.getHookString() + " [name='key_languages']").set(customFS.get("keyLanguage"));
		new VoodooControl("input", "css", language.getHookString() + " [name='value_languages']").set(testName);
		new VoodooControl("a", "css", ".config-header-buttons.fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Go to the Knowledge Base list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Create a Localization KB from the KB created from step2.
		sugar().knowledgeBase.listView.openRowActionDropdown(1);

		// TODO: VOOD-1760
		new VoodooControl("span", "css", ".list.fld_create_localization_button").click();
		VoodooUtils.waitForReady();

		// Update name
		nameCtrl.set(customFS.get("kb_localizationName"));
		statusCtrl.set(customFS.get("kb_status"));
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Click on Dashlet > Category 
		kbDashLetCtrl.click();
		VoodooUtils.waitForReady();

		// The newly created revision record appear in the KB Dashlet underneath the same Category node. 
		kbNameDashlet.waitForVisible();
		kbNameDashlet.assertContains(customFS.get("kb_localizationName"), true);
		new VoodooControl("a", "css", "div.jstree ul li.jstree-open ul li:nth-child(2) a").assertContains(customFS.get("kb_revisionName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 