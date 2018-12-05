package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_31286 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that when a parent category is deleted then its child categories also get deleted
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_31286_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB categories page
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();
		DataSource customDS = testData.get(testName);

		// TODO: VOOD-1754 Need Lib support for Categories in KnowledgeBase Module
		new VoodooControl("a", "css", ".fld_add_node_button a").click();

		// Enter the name of the Category (e.g. Test1) and hit Enter key
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(customDS.get(0).get("category") + '\ue007');
		VoodooUtils.waitForReady();

		// Now add a subcategory (e.g. Test2) in "Test1" by clicking the + icon.
		new VoodooControl("span", "css", ".tree-component ul li a span.fa.fa-plus").click();
		new VoodooControl("input", "css", ".tree-component ul li input.jstree-rename-input:not([style *='display: none'])").set(customDS.get(1).get("category") + '\ue007');
		VoodooUtils.waitForReady();

		// Add another subcategory (e.g. Test3) in "Test2".
		new VoodooControl("span", "css", ".tree-component ul li ul li a span.fa.fa-plus").click();
		new VoodooControl("input", "css", ".tree-component ul li ul li input.jstree-rename-input:not([style *='display: none'])").set(customDS.get(2).get("category") + '\ue007');
		VoodooUtils.waitForReady();

		// Also, add a subcategory (e.g. Test4) in "Test3"
		new VoodooControl("span", "css", ".tree-component ul li ul li ul li a span.fa.fa-plus").click();
		new VoodooControl("input", "css", ".tree-component ul li ul li ul li input.jstree-rename-input:not([style *='display: none'])").set(customDS.get(3).get("category") + '\ue007');
		VoodooUtils.waitForReady();

		// Now click the Context menu drop down (i.e down arrow) of the Parent category "Test2"
		VoodooControl caretDown2 = new VoodooControl("span", "css", ".tree-component ul li ul li a span.fa.fa-caret-down");
		caretDown2.click();

		// Click the "Delete" option from the context menu drop down.
		VoodooControl deleteCtlr = new VoodooControl("li", "css", "#vakata-contextmenu ul li:nth-child(9)");
		deleteCtlr.click();

		// Verify that Confirmation message appears i.e Are you sure you want to delete the category Test2? All nested categories will be removed as well. 
		// and with options: 'Confirm' and 'cancel'.
		String message = customDS.get(1).get("confirmMsg").replaceAll("%s", customDS.get(1).get("category"));
		sugar().alerts.getWarning().assertContains(message, true);

		// Click the cancel button in the confirmation prompt
		sugar().alerts.getWarning().cancelAlert();

		// Verify that Confirmation message disappears and category does not get deleted.
		new VoodooControl("span", "css", ".tree-component ul li ul li").assertContains(customDS.get(1).get("category"), true);

		// Now again try to delete the category "Test2" and click the "Confirm" option in the confirmation prompt
		caretDown2.click();
		deleteCtlr.click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that Confirmation message disappears and parent category (Test2) and all nested categories(i.e Test3 and Test4) get deleted
		VoodooControl viwCatListViewCtrl = new VoodooControl("span", "css", ".tree-component");
		viwCatListViewCtrl.assertContains(customDS.get(1).get("category"), false);

		// Delete the remaining category i.e Test1.
		VoodooControl caretDown1 = new VoodooControl("span", "css", ".tree-component ul li a span.fa.fa-caret-down");
		caretDown1.click();
		deleteCtlr.click();

		// Verify that Confirmation prompt appears with a message i.e Are you sure you want to delete the category Test1?
		message = "";
		message = customDS.get(0).get("confirmMsg").replaceAll("%s", customDS.get(0).get("category"));
		sugar().alerts.getWarning().assertContains(message, true);

		// Click the cancel button in the confirmation prompt
		sugar().alerts.getWarning().cancelAlert();

		// Verify that Confirmation message disappears and category does not get deleted.
		new VoodooControl("span", "css", ".tree-component ul li").assertContains(customDS.get(0).get("category"), true);

		// Now again try to delete the category "Test1" and click the "Confirm" option in the confirmation prompt
		caretDown1.click();
		deleteCtlr.click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify Confirmation message disappears and category Test1 gets deleted.
		viwCatListViewCtrl.assertContains(customDS.get(0).get("category"), false);

		// Now Click the KB drop down i.e in the top navbar and select "View Categories" option.
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();

		// Verify that there shouldn't be any category in the view categories drawer.
		new VoodooControl("div", "css", "[data-type='jstree-no-data']").assertContains(customDS.get(0).get("noDataAvailable"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}