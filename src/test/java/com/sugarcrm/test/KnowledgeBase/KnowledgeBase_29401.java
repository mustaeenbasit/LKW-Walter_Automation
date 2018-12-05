package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
public class KnowledgeBase_29401 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		
		// Enable knowledge Base Module
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		sugar().logout();
		
		// Login with qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that no duplicated Category is created
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29401_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to KnowledgeBase record view and edit
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.edit();
		
		// Edit the category field, click on "Category" link
		// TODO: VOOD-1754
		sugar().knowledgeBase.recordView.getEditField("category").click();
		new VoodooControl("div", "css", "[data-action='create-new']").click();
		
		// Type a string and hit enter to save the new Category
		// TODO: VOOD-1754, CB-252 ("\uE007" is used to hit Enter key)
		new VoodooControl("input", "css", "[data-place='bottom-create'] input").set(testName+'\uE007');
		
		VoodooUtils.waitForReady();
		
		// Verify Only one Category should be created
		// TODO: VOOD-1754
		new VoodooControl("li", "css", ".jstree-last.jstree-leaf").assertContains(testName, true);
		new VoodooControl("li", "css", ".tree-component ul li:nth-child(2)").assertExists(false);
		
		// Click cancel
		sugar().knowledgeBase.recordView.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}