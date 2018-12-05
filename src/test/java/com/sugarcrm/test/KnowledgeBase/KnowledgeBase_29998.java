package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29998 extends SugarTest {
	public void setup() throws Exception {
		// Log-In as an Admin
		sugar().login();
		
		// Enable Knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that select user page display on clicking 'search & select author'
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29998_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB module and create an Article
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.showMore();
		
		// Click on "Search and Select..."
		VoodooSelect relAssigned = (VoodooSelect) sugar().knowledgeBase.createDrawer.getEditField("relAssignedTo");
		relAssigned.click();
		relAssigned.selectWidget.getControl("searchForMoreLink").click();
		
		// Verify Search and Select Users Page is displayed
		FieldSet customData = testData.get(testName).get(0);
		sugar().users.searchSelect.assertVisible(true);
		sugar().users.searchSelect.getControl("moduleTitle").assertEquals(customData.get("moduleTitle"), true);
		sugar().users.searchSelect.getControl("count").assertEquals(customData.get("count"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}