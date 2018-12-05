package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29967 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that deleted Categories should not shown in "Move To" option in search and select categories drawer
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29967_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource kbCategories = testData.get(testName);
		
		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB module and create Article
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.showMore();
		
		// Click on expand icon under category drop-down.
		sugar().knowledgeBase.createDrawer.getEditField("category").click();
		
		// TODO: VOOD-1754 : Need Lib support for Categories in KnowledgeBase Module
		new VoodooControl("button", "css", "[data-action='full-screen']").click();
		VoodooUtils.waitForReady();

		VoodooControl createBtn = new VoodooControl("a", "css", ".fld_add_node_button a");
		
		// Create more than 8 categories.
		for(int i=0 ; i < kbCategories.size() ; i++) {
			
			// Clicking on Create button
			createBtn.click();
			
			// Entering KB Category name and enter
			new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(kbCategories.get(i).get("name") + '\ue007');
			VoodooUtils.waitForReady();
		}
		
		VoodooControl dropdownArrow = new VoodooControl("a", "css", ".btn.jstree-contextmenu");
		
		// Delete a few of the above created categories
		for(int i=0 ; i < Math.round(kbCategories.size() / 2) ; i++) {

			// Clicking on Category inline dropdown arrow
			dropdownArrow.click();

			// Clicking Delete
			new VoodooControl("a", "css", "#vakata-contextmenu ul li:nth-child(9) a").click();
			
			// Confirming Delete
			sugar().alerts.getAlert().confirmAlert();
			
			VoodooUtils.waitForReady();
		}
		
		// Now click on 'Action' drop-down and select 'Move To' option.
		new VoodooControl("a", "css", ".jstree-contextmenu").click();
		new VoodooControl("a", "css", "#vakata-contextmenu ul li:nth-child(7) a").click();
		
		VoodooControl visibleMoveToOptions = new VoodooControl("ul", "css", "#vakata-contextmenu li:nth-child(7) ul");
		
		// Verify deleted Categories should not be showing in "Move To" option in search and select categories drawer.
		for(int i = 0 ; i < Math.round(kbCategories.size() / 2) ; i++) {
			visibleMoveToOptions.assertContains(kbCategories.get(i).get("name"), false);
		}
		
		// Verify existing Categories are shown in "Move To" option in search and select categories drawer.
		for(int i = Math.round(kbCategories.size() / 2) + 1 ; i < kbCategories.size() ; i++) {
			visibleMoveToOptions.assertContains(kbCategories.get(i).get("name"), true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}