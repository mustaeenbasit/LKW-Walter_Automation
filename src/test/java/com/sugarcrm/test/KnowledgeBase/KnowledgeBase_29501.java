package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29501 extends SugarTest {
	VoodooControl langSaveButton;
	DataSource kbData = new DataSource();

	public void setup() throws Exception {
		kbData = testData.get(testName);
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a KB record with English language
		sugar().knowledgeBase.create();

		// Select the Settings option
		sugar().navbar.clickModuleDropdown(sugar().knowledgeBase);
		sugar().knowledgeBase.menu.getControl("configure").click();
		VoodooUtils.waitForReady();

		// Adding a new localization language
		// VOOD: 1762 : Need library support for adding/removing languages in Knowledge Base
		new VoodooControl("button", "css", ".edit.fld_languages button[name='add']").click();
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) input[name='key_languages']").
		set(kbData.get(0).get("langKey"));
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) div:nth-of-type(2) input[name='value_languages']").
		set(kbData.get(0).get("langValue"));

		// Save the language
		langSaveButton = new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']");
		langSaveButton.click();
		// Page takes more than adequate time to load
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Verify the related localization doc is displayed and updated correctly in subpanel after inline editing
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29501_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create 1 Localization
		sugar().knowledgeBase.listView.openRowActionDropdown(1);

		// TODO: VOOD-568 - library support for all of the actions in the RowActionDropdown
		new VoodooControl("a", "css", ".list.fld_create_localization_button a").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// Open a KB record to its Record view
		sugar().knowledgeBase.listView.clickRecord(2);

		// TODO: VOOD-1760 - Need library support for Localizations and Revisions in KB
		// Expand the Localization subpanel
		new VoodooControl("div", "css", ".closed div[data-voodoo-name='panel-top-for-localizations']").click();
		VoodooControl rowActionDropDown = new VoodooControl("a", "css", "div[data-voodoo-name='subpanel-for-localizations'] .single td:nth-child(7) a[data-original-title='Actions']");
		VoodooControl editButton = new VoodooControl("a", "css", ".list.fld_edit_button a[name='edit_button']");
		VoodooControl kbNameEditField = new VoodooControl("input", "css", "[data-voodoo-name='subpanel-for-localizations'] .single td:nth-child(2) input[name='name']");
		VoodooControl inlineSaveButton = new VoodooControl("span", "css", "[data-voodoo-name='subpanel-for-localizations'] .single span.edit.fld_inline-save");
		new VoodooControl("div", "css", "[data-voodoo-name='panel-top-for-revisions']").scrollIntoViewIfNeeded(false);

		for(int i = 0; i < kbData.size(); i++) {
			// Open the rowAction dropdown
			rowActionDropDown.click();

			// Click the Edit option
			editButton.click();

			// Change the KB localization name and save it
			kbNameEditField.set(kbData.get(i).get("kbName"));
			inlineSaveButton.click();
			VoodooUtils.waitForReady();
		}

		// Assert that the Related localization is correctly updated and available in subpanel
		new VoodooControl("span", "css", "[data-voodoo-name='subpanel-for-localizations'] .single .fld_name.list").assertEquals(kbData.get(1).get("kbName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}