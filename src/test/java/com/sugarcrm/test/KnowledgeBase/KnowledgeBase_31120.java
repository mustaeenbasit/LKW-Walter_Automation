package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_31120 extends SugarTest {

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Revision Number should increment as new Knowledge Base revisions are created
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_31120_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the Knowledge Base > record view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// TODO: VOOD-1760 - Need library support for Localizations and Revisions in KB
		// Open the record "Test", click the "+" to create a new revision in the "Revisions" subpanel 
		VoodooControl revisionSubPanelBtn = new VoodooControl("a", "css", "[data-subpanel-link='revisions'] [name='create_button']");
		revisionSubPanelBtn.click();
		VoodooUtils.waitForReady();

		// Add some additional lines to the revision and name it somethings distinctive like "Test 1".
		VoodooControl nameCtrl = sugar().knowledgeBase.createDrawer.getEditField("name");
		FieldSet customFS = testData.get(testName).get(0);
		nameCtrl.set(customFS.get("revisionName1"));

		// Save Revision
		sugar().knowledgeBase.createDrawer.save();

		// Now again Open the record " Test", click the "+" to create a new revision
		revisionSubPanelBtn.click();
		VoodooUtils.waitForReady();

		// Add some more lines to the revision and name it like "Test 2".
		nameCtrl.set(customFS.get("revisionName2"));

		// Save Revision
		sugar().knowledgeBase.createDrawer.save();

		// TODO: VOOD-1760 - Need library support for Localizations and Revisions in KB
		// Verify that the revision number should be 2 for "Test 1" and 3 for "Test 2".(Both revision numbers should not be same)
		String cssSelector = "[data-voodoo-name='subpanel-for-revisions'] .dataTable tr";
		new VoodooControl("a", "css", cssSelector + " .list.fld_name").assertContains(customFS.get("revisionName2"), true);
		new VoodooControl("a", "css", cssSelector + " .list.fld_revision").assertContains(customFS.get("revisionCount2"), true);
		new VoodooControl("a", "css", cssSelector + ":nth-child(2) .list.fld_name").assertContains(customFS.get("revisionName1"), true);
		new VoodooControl("a", "css", cssSelector + ":nth-child(2) .list.fld_revision").assertContains(customFS.get("revisionCount1"), true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}