package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21449 extends SugarTest {
	public void setup() throws Exception {
		// Create a KB without revision
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * delete KB article that doesn't have revision in record view
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21449_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Article's detail view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// Click "Delete"
		sugar().knowledgeBase.recordView.delete();

		// Click Cancel
		sugar().alerts.getAlert().cancelAlert();

		sugar().knowledgeBase.navToListView();
		// Verify that article was not deleted
		Assert.assertTrue("Total no of records in KB list view is not 1", sugar().knowledgeBase.listView.countRows() == 1);
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(sugar().knowledgeBase.getDefaultData().get("name"), true);

		// Article's detail view
		sugar().knowledgeBase.listView.clickRecord(1);
		// Click "Delete"
		sugar().knowledgeBase.recordView.delete();
		// Click confirm
		sugar().alerts.getAlert().confirmAlert();

		// Verify the article is deleted.
		sugar().knowledgeBase.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}