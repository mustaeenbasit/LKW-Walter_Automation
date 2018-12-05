package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29431 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();
		
		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that duplicating a published article should default the new article to a status of "Draft".
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29431_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.copy();

		// Verify that Status should be "Draft".
		sugar().knowledgeBase.createDrawer.getEditField("status").assertEquals("Draft", true);

		// Cancel creating duplicate record
		sugar().knowledgeBase.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}