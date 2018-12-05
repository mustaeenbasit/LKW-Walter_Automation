package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29954 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that one records should be shown in list view of Notes module after created record in KB with attachment
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29954_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to create drawer of KB after clicking on create article
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");

		// Creating a kb article with attachment
		sugar().knowledgeBase.createDrawer.getEditField("name").set(sugar().knowledgeBase.getDefaultData().get("name"));
		VoodooFileField browseToImport = new VoodooFileField("input", "css", "[name='attachment_list']");
		browseToImport.set("src/test/resources/data/" + testName + ".csv");
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.createDrawer.save();

		// Asserting one record is present in notes list view with attachment. 
		sugar().notes.navToListView();
		sugar().notes.listView.getDetailField(1, "subject").assertContains(testName, true);
		int listCount = sugar().notes.listView.countRows();
		Assert.assertTrue("Notes record count is not equal to one in notes list view", listCount == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}