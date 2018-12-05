package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29167 extends SugarTest {
	FieldSet customData = new FieldSet();
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		fs.put("name", "TestKB");
		sugar().knowledgeBase.api.create(fs);
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().knowledgeBase);

		// Select the Settings (previously 'Configure') option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Adding an new localization language de - Deutsch 
		// VOOD: 1762 : Need library support for adding/removing languages in Knowledge Base
		new VoodooControl("button", "css", ".btn.first").click();
		new VoodooControl("input", "css", ".fld_languages div:nth-child(2).control-group [name='key_languages']")
		.set(customData.get("languageKey"));
		new VoodooControl("input", "css", ".fld_languages div:nth-child(2).control-group [name='value_languages']")
		.set(customData.get("languageValue"));
		new VoodooControl("a", "css", ".fld_main_dropdown [name='save_button']").click();
		VoodooUtils.waitForReady(30000);
		sugar().logout();
	}

	/**
	 * Verify regular user can only create one localization article per language
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29167_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logging in as qauser
		sugar().login(sugar().users.getQAUser());

		// Selecting first record in Knowledge Base
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// TOTO: VOOD-1760 : Need library support for Localizations and Revisions subpanel in KB Record View
		// Selecting the '+' button on Localization subpanel
		VoodooControl subpanelPlusButton = new VoodooControl("a", "css",".panel-top-for-localizations .fld_create_button a");
		subpanelPlusButton.click();

		String kbNameInDeutsch = fs.get("name") + "_" + customData.get("languageValue");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbNameInDeutsch);
		sugar().knowledgeBase.createDrawer.showMore();

		// TODO: VOOD-1843 - Once resolved it should use getChildElement method
		new VoodooSelect("span", "css", sugar().knowledgeBase.createDrawer.getEditField("language").getHookString() + ".edit").set(customData.get("languageValue"));
		sugar().knowledgeBase.createDrawer.save();

		// Selecting the localized Knowledge Base article i.e. the one in Deutsch
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.setSearchString(kbNameInDeutsch);
		sugar().knowledgeBase.listView.clickRecord(1);

		// Selecting the '+' button on Localization subpanel
		subpanelPlusButton.scrollIntoViewIfNeeded(false);
		subpanelPlusButton.click();
		Alert warning = sugar().alerts.getWarning();
		warning.waitForVisible();

		// Verify that a message/alert pops up and states : "Unable to create a new localization as a 
		// localization version exists for all available languages."
		warning.assertContains(customData.get("warningMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}