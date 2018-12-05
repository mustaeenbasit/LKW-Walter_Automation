package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29646 extends SugarTest {
	DataSource categoryData = new DataSource(), KBData = new DataSource();

	public void setup() throws Exception {
		categoryData = testData.get(testName+"_catData");
		KBData = testData.get(testName+"_data");
		
		sugar().knowledgeBase.api.create(KBData);

		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Create 5 Categories
		for(int i = 0; i < categoryData.size(); i++) {
			// TODO: VOOD-1754
			new VoodooControl("a", "css", "a[name='add_node_button']").click();
			VoodooUtils.waitForReady();
			// TODO: VOOD-1437 and CB-252
			new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(categoryData.get(i).get("categoryName") + '\uE007');		
			VoodooUtils.waitForReady();
		}

		// Navigate to KB record and add Category value
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		VoodooControl categoryField = sugar().knowledgeBase.recordView.getEditField("category");
		for(int j = 0; j < KBData.size(); j++) {
			sugar().knowledgeBase.recordView.edit();
			sugar().knowledgeBase.recordView.showMore();
			categoryField.click();

			// Select Category
			// TODO: VOOD-1754
			new VoodooControl("a", "css", ".list .jstree-sugar.tree-component li:nth-child(" + (j+1) + ") a").click();
			sugar().knowledgeBase.recordView.save();
			sugar().knowledgeBase.recordView.gotoNextRecord();
		}
	}

	/**
	 * Verify that Categories tree loading correctly
	 *   
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29646_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB lit view 
		sugar().knowledgeBase.navToListView();
		
		// Select My Dashboard from the right hand side
		FieldSet customFS = testData.get(testName).get(0);
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(customFS.get("dashboardTitle"), true))
			sugar().dashboard.chooseDashboard(customFS.get("dashboardTitle"));

		VoodooUtils.waitForReady();
		for(int i = 0, k=4; i < KBData.size(); i++, k--) {
			String cssSelector = ".dashlets li ul.dashlet-cell .nested-list-widget ul li:nth-child("+(i+1)+")";
			
			// "KB Categories" Dashlet appears "loading..." until all the records and categories structures are loaded completely.
			new VoodooControl("li", "css", cssSelector).assertContains(categoryData.get(i).get("categoryName"), true);
			
			// Click on Category
			new VoodooControl("li", "css", cssSelector+" a").click();
			VoodooControl kbNameCtrl = new VoodooControl("li", "css", cssSelector+" ul li a");
			kbNameCtrl.waitForVisible(30000);
			
			// Verify that a Category associated with KB record
			kbNameCtrl.assertContains(KBData.get(k).get("name"), true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}