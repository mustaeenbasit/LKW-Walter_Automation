package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30113 extends SugarTest {
	FieldSet categoryData = new FieldSet();

	public void setup() throws Exception {
		categoryData = testData.get(testName).get(0);

		// Create KB records with status 'Published'
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		fs.put("status", categoryData.get("status"));
		sugar().knowledgeBase.api.create(fs);
		fs.clear();
		fs.put("status", categoryData.get("status"));
		sugar().knowledgeBase.api.create(fs);

		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// Create one parent and one sub category
		// TODO: VOOD-1754 and VOOD-1437
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooControl categoryNameCtrl = new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])");
		categoryNameCtrl.set(categoryData.get("parentCategory") + '\uE007');
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".tree-block.nested-set-full .btn.jstree-addnode").click();
		categoryNameCtrl.set(categoryData.get("subCategory") + '\uE007');
	}

	/**
	 * Verify that KB Categories Dashlet refreshes automatically when deleting associated 
	 * KB Article in List View
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30113_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB list view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.editRecord(1);

		// Add parent category to the first record
		VoodooControl categoryListEditViewCtrl1 = sugar().knowledgeBase.listView.getEditField(1, "category");
		// TODO: VOOD-1754
		new VoodooControl("b", "css", categoryListEditViewCtrl1.getHookString() + " .select-arrow b").click();
		// TODO: VOOD-629
		new VoodooControl("a", "css", ".list .jstree-sugar.tree-component li a").click();
		sugar().knowledgeBase.listView.saveRecord(1);

		// Add child category to the second record
		sugar().knowledgeBase.listView.editRecord(2);
		VoodooControl categoryListEditViewCtrl2 = sugar().knowledgeBase.listView.getEditField(2, "category");
		new VoodooControl("b", "css", categoryListEditViewCtrl2.getHookString() + " .select-arrow b").click();
		new VoodooControl("ins", "css", ".list .jstree-sugar.tree-component li ins").click();
		new VoodooControl("a", "css", ".list .jstree-sugar.tree-component li ul a").click();
		sugar().knowledgeBase.listView.saveRecord(2);

		// Select My Dashboard from the right hand side
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(categoryData.get("myDashboard"), true)) {
			sugar().dashboard.chooseDashboard(categoryData.get("myDashboard"));
		}

		// Expand all the category in 'Knowledge Base Categories & Published Articles'
		// TODO: VOOD-960
		new VoodooControl("ins", "css", ".dashlets li ul.dashlet-cell .nested-list-widget ul li ins").click();
		VoodooUtils.waitForReady();
		new VoodooControl("ins", "css", ".dashlets li ul.dashlet-cell .nested-list-widget ul li ul ins").click();

		// Verify the kb records present in 'Knowledge Base Categories & Published Articles' dashlet
		VoodooControl dashletValue = new VoodooControl("div", "css", ".jstree-sugar.tree-component");
		String kbName = sugar().knowledgeBase.getDefaultData().get("name");
		dashletValue.assertContains(kbName, true);
		dashletValue.assertContains(testName, true);

		// Deleting first record from kb list view
		sugar().knowledgeBase.listView.deleteRecord(1);
		sugar().knowledgeBase.listView.confirmDelete();

		// Verify the first record disappear from 'Knowledge Base Categories & Published Articles' dashlet
		dashletValue.assertContains(kbName, false);

		// Deleting second record from kb list view
		sugar().knowledgeBase.listView.deleteRecord(1);
		sugar().knowledgeBase.listView.confirmDelete();

		// Verify the second record disappear from 'Knowledge Base Categories & Published Articles' dashlet
		dashletValue.assertContains(testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}