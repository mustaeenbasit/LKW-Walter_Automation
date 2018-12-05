package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30213 extends SugarTest {
	VoodooControl kbSettingSaveCtrl, studioKbCtrl, studioKbSubpanelCtrl,
			studioKbLocalizationCtrl, studioKbRevisionCtrl, studioSavectrl, studioCtrl;
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Frequency is changed to "View Count" in KB.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30213_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		VoodooControl kbViewCountListView = sugar().knowledgeBase.listView.getDetailField(1, "viewCount");
		VoodooControl kbViewCountRecordView = sugar().knowledgeBase.recordView.getDetailField("viewCount");
		VoodooControl hiddenPaneViewCountCtrl = new VoodooControl("li", "css", "#Hidden [data-name='viewcount']");
		VoodooControl defaultPaneStatusCtrl = new VoodooControl("li", "css", "#Default [data-name='status']");
		int kbInitialFrequencyCount = 0;


		// Navigating to kb list view 
		sugar().knowledgeBase.navToListView();

		// Verifying View Count is appearing in list view of KB.
		kbViewCountListView.assertVisible(true);

		// Verifying View Count initial value is zero in list view.
		kbViewCountListView.assertEquals(Integer.toString(kbInitialFrequencyCount), true);
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();

		// Verifying View Count is appearing in Record view of KB.
		kbViewCountRecordView.assertVisible(true);

		// Verifying View Count is increased by one (i.e view count = 1) in Record view of KB.
		kbViewCountRecordView.assertEquals(Integer.toString(kbInitialFrequencyCount + 1), true);
		sugar().knowledgeBase.navToListView();

		// Verifying View Count = 1 in list view of KB.
		kbViewCountListView.assertEquals(Integer.toString(kbInitialFrequencyCount + 1), true);

		// Navigating to admin tool
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		studioKbCtrl = new VoodooControl("a", "id", "studiolink_KBContents");
		studioKbCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1511
		// Adding view count in localization by navigating admin->studio->KB->Subpanels.
		studioKbSubpanelCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		studioKbSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		studioKbLocalizationCtrl = new VoodooControl("tr", "css", "#Buttons td:nth-child(1) tr:nth-child(2)");
		studioKbLocalizationCtrl.click();
		hiddenPaneViewCountCtrl.dragNDropViaJS(defaultPaneStatusCtrl);
		studioSavectrl = new VoodooControl("input", "id", "savebtn");
		studioSavectrl.click();
		VoodooUtils.waitForReady();

		// Adding view count in revision by navigating admin->studio->KB->Subpanels.
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		studioKbCtrl.click();
		VoodooUtils.waitForReady();
		studioKbSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		studioKbRevisionCtrl = new VoodooControl("a", "css", "#Buttons td:nth-child(2) .studiolink");
		studioKbRevisionCtrl.click();
		VoodooUtils.waitForReady();
		hiddenPaneViewCountCtrl.dragNDropViaJS(defaultPaneStatusCtrl);
		studioSavectrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// For creating localization, adding one more language in KB settings.
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// TODO: VOOD-1762
		// Adding Deutsch language in KB->Settings
		new VoodooControl("button", "css", ".btn.first").click();
		new VoodooControl("input", "css", "div:nth-child(2)[data-name='languages_languages'] [name='key_languages']").set(customData.get("languageCode"));
		new VoodooControl("input", "css", "div:nth-child(2)[data-name='languages_languages'] [name='value_languages']").set(customData.get("languageName"));
		kbSettingSaveCtrl = new VoodooControl("span", "css", ".config-header-buttons.fld_save_button");
		kbSettingSaveCtrl.click();
		VoodooUtils.waitForReady(30000);  // Needed Extra wait

		// Open the exist record
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// TODO: VOOD-1760
		// Creating one revision from revision subpanel
		new VoodooControl("a", "css", "[data-subpanel-link='revisions'] [name='create_button']").click();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customData.get("kbRevisionName"));
		sugar().knowledgeBase.createDrawer.save();

		// TODO: VOOD-1760
		// Creating one localization from localization subpanel
		new VoodooControl("a", "css", "[data-subpanel-link='localizations'] [name='create_button']").click();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customData.get("kbLocalizationName"));
		sugar().knowledgeBase.createDrawer.save();

		// Clicking on new created revision of KB to open
		sugar().knowledgeBase.recordView.subpanels.get(sugar().notes.moduleNamePlural).hover();
		new VoodooControl("span", "css", "[data-voodoo-name='subpanel-for-kbcontents-revisions'] .list.fld_name").click();

		// Verifying view count is appearing in revision.
		kbViewCountRecordView.assertVisible(true);

		// Verifying view count = 4 in  kb revision.
		kbViewCountRecordView.assertEquals(customData.get("finalViewCount"), true);

		// Verifying view count is appearing in original kb.
		new VoodooControl("th", "css", "[data-voodoo-name='subpanel-for-kbcontents-revisions'] .orderByviewcount").assertContains(customData.get("viewCountText"), true);

		// Verifying view count = 4 in  original kb.
		new VoodooControl("span", "css", "[data-voodoo-name='subpanel-for-kbcontents-revisions'] .list.fld_viewcount").assertContains(customData.get("finalViewCount"), true);

		// Verifying view count is appearing in localization.
		new VoodooControl("th", "css", "[data-voodoo-name='subpanel-for-kbcontents-localizations'] .orderByviewcount").assertContains(customData.get("viewCountText"), true);

		// Verifying view count = 4 in  localization.
		new VoodooControl("span", "css", "[data-voodoo-name='subpanel-for-kbcontents-localizations'] .list.fld_viewcount").assertContains(customData.get("finalViewCount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}