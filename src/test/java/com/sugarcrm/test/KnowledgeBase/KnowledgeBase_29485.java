package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29485 extends SugarTest {

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Blank record can not be saved while creating a Category in Knowledge Base Categories
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29485_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB categories listView
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");

		// TODO: VOOD-1754
		// Create a category
		new VoodooControl("a", "css", ".layout_Categories.active a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		FieldSet customFS = testData.get(testName).get(0);

		// TODO: CB-252
		// In the input box,clear the text box having "default title" and then enter space, hit enter
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(" "+'\uE007');

		// Verify a red color error message box appears saying "Error You can not add a category without title" No blank category is saved
		sugar().alerts.getError().assertContains(customFS.get("errorMsg"), true);

		// Navigate to the Knowledge Base list view
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Go to create knowledge base record
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();

		// Click on select Category
		sugar().knowledgeBase.createDrawer.getEditField("category").click();

		// TODO: VOOD-1754
		// Click on Category create link
		VoodooControl categoryLinkCtrl = new VoodooControl("div", "css", ".parenttree.open .dropdown-menu li [data-action='create-new']");
		categoryLinkCtrl.click();

		// TODO: CB-252
		// In the input box,clear the text box having "default title" and then enter space, hit enter
		VoodooControl categoryInputCtrl = new VoodooControl("input", "css", ".parenttree.open .dropdown-menu li [data-place='bottom-create'] input");
		categoryInputCtrl.set(" "+'\uE007');

		// Verify a red color error message box appears saying "Error You can not add a category without title" No blank category is saved.
		sugar().alerts.getError().assertContains(customFS.get("errorMsg"), true);

		// Go to KB listViw
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.editRecord(1);
		
		// TODO: VOOD-1754
		// Click on select category
		new VoodooControl("div", "css", ".fld_category_name.edit div").click();
		
		// Click on Category create link and clear the text box having "default title" and then enter space, hit enter
		categoryLinkCtrl.click();
		categoryInputCtrl.set(" "+'\uE007');

		// Verify a red color error message box appears saying "Error You can not add a category without title" No blank category is saved.
		sugar().alerts.getError().assertContains(customFS.get("errorMsg"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}