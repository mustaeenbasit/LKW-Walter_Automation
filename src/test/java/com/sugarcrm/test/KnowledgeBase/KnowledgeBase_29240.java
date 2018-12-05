package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29240 extends SugarTest {
	DataSource kbRecordsDS = new DataSource();

	public void setup() throws Exception {
		kbRecordsDS = testData.get(testName);
		sugar().knowledgeBase.api.create(kbRecordsDS);
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify KB's main menu and submenu displays correctly, and opens correct page
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29240_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource kbMenuItemsDS = testData.get(testName + "_kbMenuItems");
		VoodooControl moduleTitle = sugar().knowledgeBase.listView.getControl("moduleTitle");

		// Goto KB module and click on module drop down button
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);

		// Verifying the items availability in menu drop down of KB
		for (int i = 0; i < kbMenuItemsDS.size(); i++) {
			sugar().knowledgeBase.menu.getControl(kbMenuItemsDS.get(i).get("menuItemsControls")).assertEquals(kbMenuItemsDS.get(i).get("menuItemsText"), true);
		}

		// Clicking on any 1 record and back to list view
		sugar().knowledgeBase.listView.clickRecord(1);

		// Verifying recently opened article is showing in megamenu of KB
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);

		// TODO: VOOD-771: need defined control for recently viewed list on navbar
		new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-child(8)").assertEquals(kbRecordsDS.get(4).get("name"), true);

		// Verifying visibility of create drawer of KB after clicking on create article
		sugar().knowledgeBase.menu.getControl("createArticle").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1887: Differentiate modules in view element hooks. Once resolved, below commented line should work
		// sugar().knowledgeBase.createDrawer.assertVisible(true);
		new VoodooControl("div", "css", ".layout_KBContents.drawer.active").assertVisible(true);
		sugar().knowledgeBase.createDrawer.cancel();

		// Verifying visibility of create drawer of KB templates after clicking on create templates
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
		sugar().knowledgeBase.menu.getControl("createTemplate").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1756: Need Lib support for Templates in KnowledgeBase Module.
		new VoodooControl("div", "css", ".layout_KBContentTemplates.drawer.active").assertVisible(true);
		new VoodooControl("a", "css", ".drawer.active .layout_KBContentTemplates .fld_cancel_button a").click();

		// Verifying visibility of list view after clicking on create view articles
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
		sugar().knowledgeBase.menu.getControl("viewArticles").click();
		VoodooUtils.waitForReady();
		moduleTitle.assertContains(kbRecordsDS.get(2).get("name"), true);

		// Verifying visibility of list view of templates after clicking on view templates
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewTemplates");
		moduleTitle.assertContains(kbRecordsDS.get(3).get("name"), true);

		// Verifying visibility of list view of categories after clicking on view categories
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
		sugar().knowledgeBase.menu.getControl("viewCategories").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1754: Need Lib support for Categories field and Help Text (in RHS) in KnowledgeBase create Drawer.
		new VoodooControl("input", "css", ".layout_Categories.drawer.active .search-name").assertVisible(true);
		new VoodooControl("a", "css", ".layout_Categories .fld_close a").click();

		// Verifying visibility of setting page after clicking on settings
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
		sugar().knowledgeBase.menu.getControl("configure").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1762: Need library support for adding/removing languages in Knowledge Base
		new VoodooControl("span", "css", ".layout_KBContents .module-title").assertEquals(kbRecordsDS.get(4).get("name"), true);
		new VoodooControl("a", "css", ".drawer.active.layout_KBContents .fld_cancel_button a").click();

		// Verifying visibility of record after clicking on recently created record
		sugar().navbar.clickRecentlyViewed(sugar().knowledgeBase, kbRecordsDS.get(4).get("name"));
		sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(kbRecordsDS.get(4).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}