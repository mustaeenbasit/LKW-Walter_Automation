package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29640 extends SugarTest {
	DataSource customData = new DataSource();
	VoodooControl moduleCtrl, studioCtrl;

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		customData = testData.get(testName);

		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Create Two Category 
		for(int i = 0; i < customData.size(); i++) {
			// TODO: VOOD-1754
			new VoodooControl("a", "css", "a[name='add_node_button']").click();
			VoodooUtils.waitForReady();
			// TODO: VOOD-1437 and CB-252
			new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(customData.get(i).get("categoryName") + '\uE007');		
			VoodooUtils.waitForReady();
		}

		// Navigate to KB record and add Tag and Category value
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.getEditField("category").click();
		// TODO: VOOD-629 and VOOD-1754
		new VoodooControl("a", "css", ".list .jstree-sugar.tree-component a").click();
		sugar().knowledgeBase.recordView.getEditField("tags").set(customData.get(0).get("tagName"));
		sugar().knowledgeBase.recordView.save();

		// Go to Admin -> Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Go to KB -> Layout -> List View
		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_KBContents");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Enable Tags field
		// TODO: VOOD-1506
		VoodooControl defaultColumnCtrl =  new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "li[data-name='tag']").dragNDrop(defaultColumnCtrl);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify inline category fields in KB list view are updating on preview properly
	 *   
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29640_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB List View and click on "Preview" for KB record
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Define Controls for KB preview pane
		VoodooControl tagPreviewCtrl = sugar().previewPane.getPreviewPaneField("tags");
		VoodooControl categoryPreviewCtrl = sugar().previewPane.getPreviewPaneField("category");

		// Verify that the Tags/Category fields show in the preview pane
		categoryPreviewCtrl.assertContains(customData.get(0).get("categoryName"), true);
		tagPreviewCtrl.assertContains(customData.get(0).get("tagName"), true);

		// Inline edit this record
		sugar().knowledgeBase.listView.editRecord(1);

		// Update Category field
		VoodooControl categoryListEditViewCtrl = sugar().knowledgeBase.listView.getEditField(1, "category");
		// TODO: VOOD-1754
		new VoodooControl("b", "css", categoryListEditViewCtrl.getHookString() + " .select-arrow b").click();
		categoryListEditViewCtrl.click();
		// TODO: VOOD-629 and VOOD-1754
		new VoodooControl("a", "css", ".list .jstree-sugar.tree-component li:nth-child(2) a").click();

		// Update Tag field
		// TODO: VOOD-1753
		new VoodooControl("a", "css", ".fld_tag.edit li a").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1437 and CB-252
		new VoodooControl("input", "css", ".fld_tag.edit input").set(customData.get(1).get("tagName") + '\uE007');

		// Click on Save
		sugar().knowledgeBase.listView.saveRecord(1);

		// Verify that the Tags/Category fields should show the updated(in step5) values for Tags/Category fields in preview pane.
		categoryPreviewCtrl.assertContains(customData.get(1).get("categoryName"), true);
		tagPreviewCtrl.assertContains(customData.get(1).get("tagName"), true);

		// Also verify that the old values of Tags/Category fields are not showing in the preview pane
		categoryPreviewCtrl.assertContains(customData.get(0).get("categoryName"), false);
		tagPreviewCtrl.assertContains(customData.get(0).get("tagName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}