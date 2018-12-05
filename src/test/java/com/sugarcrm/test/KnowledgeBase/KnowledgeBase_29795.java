package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29795 extends SugarTest {

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable knowledgeBase Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to View Categories and Create a category
		// TODO: VOOD-1754
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		VoodooControl addNode = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");
		addNode.set(testName+'\uE007');		
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".btn.jstree-addnode").click();
		addNode.set(testName+'\uE007');
	}

	/**
	 * Verify that user should not be navigated to the home screen after,click on Category in Knowledge base Categories Dashlet
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29795_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Knowledge Base List View
		sugar().knowledgeBase.navToListView();

		// Create a Dashboard of KnowledgeBase Categories
		// TODO: VOOD-1652,VOOD-960
		sugar().knowledgeBase.dashboard.clickCreate();
		sugar().knowledgeBase.dashboard.addRow();
		sugar().knowledgeBase.dashboard.addDashlet(1,1);
		FieldSet dashletinfo = testData.get(testName).get(0);
		new VoodooControl("input", "css", ".inline-drawer-header .search").set(dashletinfo.get("name"));
		new VoodooControl("a", "css", ".list.fld_title a").click();
		new VoodooControl("a", "css", ".drawer.active .fld_save_button a").click();
		sugar().knowledgeBase.dashboard.getControl("title").set(testName);
		sugar().knowledgeBase.dashboard.save();

		// Click the configure icon on the Dashlet
		new VoodooControl("i", "css", ".dashlet-header .dropdown-toggle i").click();

		// Click Edit in the dropdown
		new VoodooControl("a", "css", ".dashlet-header .dropdown-menu a").click();
		VoodooUtils.waitForReady();

		// Click Cancel 
		new VoodooControl("a", "css", ".drawer.active .fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		// Click on the Category available in the Dashlet
		new VoodooControl("a", "css", ".jstree-last.jstree-closed a").click();

		// Verify user is not navigated to the HomePage,stays on the same page
		String url = VoodooUtils.getUrl();
		Assert.assertFalse("Home is present in the url", url.contains(dashletinfo.get("url")));
		sugar().knowledgeBase.listView.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}