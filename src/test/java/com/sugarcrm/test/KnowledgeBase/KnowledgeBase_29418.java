package com.sugarcrm.test.KnowledgeBase;
import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29418 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();
		
		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Navigate away from Canceling of edit a KB don't threw warning message
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29418_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to knowledgeBase record view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		
		// Click on Edit button
		sugar().knowledgeBase.recordView.edit();
		
		// Click on Cancel
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.recordView.cancel();
		
		// Navigate to other modules by click on Accounts module in the mega menu bar
		sugar().accounts.navToListView();
		
		// Verify that it allow to navigate other module list view without any warning message bar
		Assert.assertTrue("Warning message appears", sugar().alerts.getWarning().queryVisible() == false);
		VoodooUtils.waitForReady();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}