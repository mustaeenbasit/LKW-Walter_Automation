package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30190 extends SugarTest {
	FieldSet customFS = new FieldSet();
	
	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Select the Settings option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Adding one new localization language 
		// TODO: VOOD-1762
		new VoodooControl("button", "css", ".edit.fld_languages button[name='add']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) input[name='key_languages']").set(customFS.get("languageCode"));
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) div:nth-of-type(2) input[name='value_languages']").set(customFS.get("languageLabel"));

		// Save the language
		new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']").click();
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Verify that create a localization or revision from KB action list in recordview correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30190_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-1760
		new VoodooControl("a", "css", ".detail.fld_create_localization_button a").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customFS.get("localizationName"));
		new VoodooControl("a", "css", "#drawers .active .fld_save_button a:not(.hide)").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1760
		// Verify that the saved KB appears in Localization KB subpanel.
		VoodooControl localizationStubPanel = new VoodooControl("a", "css", "div[data-subpanel-link='localizations'] .fld_name a");
		localizationStubPanel.assertEquals(customFS.get("localizationName"), true);
		
		// Verify that the Expend or collapse the subpanel, the KB remains.
		VoodooControl localizationExpandCollapse = new VoodooControl("div", "css", "div[data-subpanel-link='localizations'] .subpanel-header");
		localizationExpandCollapse.click(); // Subpanel collapse
		localizationExpandCollapse.click(); // Subpanel Expand
		localizationStubPanel.assertEquals(customFS.get("localizationName"), true);

		// In the same KB record view, open action drop down list. Select "Create Revision".
		sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".detail.fld_create_revision_button a").click();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customFS.get("revisionName"));
		new VoodooControl("a", "css", "#drawers .active .fld_save_button a:not(.hide)").click();
		VoodooUtils.waitForReady();
		
		// Verify that the saved KB appears in Revisions KB subpanel.
		VoodooControl revisionStubPanel = new VoodooControl("a", "css", "div[data-subpanel-link='revisions'] .fld_name a");
		revisionStubPanel.assertEquals(customFS.get("revisionName"), true);
		
		// Expend or collapse the subpanel, the KB remains.
		VoodooControl revisionExpandCollapse = new VoodooControl("div", "css", "div[data-subpanel-link='revisions'] .subpanel-header");
		revisionExpandCollapse.click(); // Subpanel collapse
		revisionExpandCollapse.click(); // Subpanel Expand
		revisionStubPanel.assertEquals(customFS.get("revisionName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}