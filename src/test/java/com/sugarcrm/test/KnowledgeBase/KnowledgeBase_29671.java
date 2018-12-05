package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29671 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Select the Settings (previously 'Configure') option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Add new localization language
		// TODO: VOOD-1762 : Need library support for adding/removing languages in Knowledge Base
		new VoodooControl("button", "css", "[data-action='add-field']").click();
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) input[name='key_languages']").set(testName.substring(0, 1));
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) div input[name='value_languages']").set(testName);

		// Save the languages
		// TODO: VOOD-1762
		new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that single drawer is opened on “Create Localization / Create Revision” from Knowledge-Base Listview.
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29671_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Knowledge Base Module and Click on action dropdown and hit “Create Article”
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// TODO: VOOD-738
		// On Listview, Click on “Create Localization” from Actions (for same record).
		sugar().knowledgeBase.listView.openRowActionDropdown(1);
		new VoodooControl("a", "css", ".list.fld_create_localization_button a").click();

		// Click on Cancel button
		sugar().knowledgeBase.createDrawer.cancel();

		// Verify that if clicked on Cancel button then back to listView Page, in this way we can verify that there have only on CreateDrawer
		// (only one drawer should be displayed after clicking “Create Localization”.)
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}