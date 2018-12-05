package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29531 extends SugarTest {
	FieldSet kbData = new FieldSet();
	DataSource kbLang = new DataSource();
	int langCount = 0;
	VoodooControl langSaveButton;

	public void setup() throws Exception {
		kbLang = testData.get(testName);

		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a KB record with English language
		// TODO: VOOD-444
		kbData.put("status", kbLang.get(0).get("status"));
		sugar().knowledgeBase.create(kbData);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
		kbData.clear();

		langCount = kbLang.size();

		// Select the Settings option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Adding two new localization languages 
		// TODO: VOOD-1762
		VoodooControl addLangBtn = new VoodooControl("button", "css", ".edit.fld_languages button[name='add']");
		langSaveButton = new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']");

		for(int i = 0; i < langCount; i++){
			addLangBtn.click();
			new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(" + (2+ i) + ") input[name='key_languages']").set(kbLang.get(i).get("langCode"));
			new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(" + (2 + i) + ") div:nth-of-type(2) input[name='value_languages']").set(kbLang.get(i).get("langLabel"));
		}

		// Save the languages
		langSaveButton.click();
		VoodooUtils.waitForReady(30000); // Needed extra wait
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Logout from admin user and login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that the correct KB records are appearing in "Other Languages" Dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29531_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open the exist record
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// Switch to My Dashboard in order to view the Other Languages Dashlet
		VoodooControl dashboardTitle = sugar().knowledgeBase.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(kbLang.get(0).get("myDashboard"), true))
			sugar().dashboard.chooseDashboard(kbLang.get(0).get("myDashboard"));

		// Define Controls for 'Published Articles in Other Languages' dashlets
		// TODO: VOOD-960
		VoodooControl otherLanguagesDashletCtrl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(2) .dashlet-container:nth-of-type(1)");
		VoodooControl noDataAvailableCtrl = new VoodooControl("div", "css", otherLanguagesDashletCtrl.getHookString() + " .block-footer");
		VoodooControl firstRecordInDashletCtrl = new VoodooControl("i", "css", otherLanguagesDashletCtrl.getHookString() + " .dashlet-content .kbcontents li");
		VoodooControl secondRecordInDashletCtrl = new VoodooControl("a", "css", otherLanguagesDashletCtrl.getHookString() + " .dashlet-content .kbcontents li:nth-child(2)");
		VoodooControl thirdRecordInDashletCtrl = new VoodooControl("a", "css", otherLanguagesDashletCtrl.getHookString() + " .dashlet-content .kbcontents li:nth-child(3)");

		// Verify that 'No data available' in "Other Languages" Dashlet.
		noDataAvailableCtrl.assertEquals(kbLang.get(0).get("noDataAvailable"), true);

		// Create 2 Localizations with status = Published
		// TODO: VOOD-1760
		String publishStatus = kbLang.get(0).get("status");
		String kbNameString = sugar().knowledgeBase.getDefaultData().get("name");
		VoodooControl createLocalizationBtn = new VoodooControl("a", "css", "[data-subpanel-link='localizations'] [name='create_button']");
		VoodooControl kbNameField,kbStatus;
		kbNameField = sugar().knowledgeBase.createDrawer.getEditField("name");
		kbStatus = sugar().knowledgeBase.createDrawer.getEditField("status");
		// TODO: VOOD-1843
		VoodooSelect kbLanguage = new VoodooSelect("span", "css", sugar().knowledgeBase.createDrawer.getEditField("language").getHookString() + ".edit");

		for(int i = 0; i < langCount; i++){
			createLocalizationBtn.click();
			VoodooUtils.waitForReady();
			kbNameField.set(kbLang.get(i).get("langLabel"));
			kbStatus.set(publishStatus);
			sugar().knowledgeBase.createDrawer.showMore();
			kbLanguage.set(kbLang.get(i).get("langLabel"));
			sugar().knowledgeBase.createDrawer.save();
			if (sugar().alerts.getSuccess().queryVisible())
				sugar().alerts.getSuccess().closeAlert();
		}

		// Refresh the "Other Languages" Dashlet
		// TODO: VOOD-960
		new VoodooControl("i", "css", otherLanguagesDashletCtrl.getHookString() + " a[data-original-title='Configure'] i").click();
		new VoodooControl("a", "css", otherLanguagesDashletCtrl.getHookString() + " .dropdown-menu.left li:nth-child(2) a").click();
		VoodooUtils.waitForReady();

		// Verify that the 2 new published KBs are appearing in the "Other Languages" Dashlet
		firstRecordInDashletCtrl.assertContains(kbLang.get(1).get("langLabel"), true);
		secondRecordInDashletCtrl.assertContains(kbLang.get(0).get("langLabel"), true);

		// QAUser create a new published KB
		kbData.put("name", testName);
		kbData.put("status", kbLang.get(0).get("status"));
		sugar().knowledgeBase.create(kbData);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Open the new KB record
		sugar().knowledgeBase.listView.clickRecord(1);

		// Switch to My Dashboard in order to view the Other Languages Dashlet
		if(!dashboardTitle.queryContains(kbLang.get(0).get("myDashboard"), true))
			sugar().dashboard.chooseDashboard(kbLang.get(0).get("myDashboard"));

		// Verify that 'No data available' in "Other Languages" Dashlet.
		noDataAvailableCtrl.assertEquals(kbLang.get(0).get("noDataAvailable"), true);

		// Click on "btn btn-invisible next-row" button
		sugar().knowledgeBase.recordView.gotoNextRecord();

		// Verify that Only localization KB records appear in "Other Languages" Dashlet in the current KB record view.  Other KB records should not be appearing
		firstRecordInDashletCtrl.assertContains(kbLang.get(0).get("langLabel"), true);
		secondRecordInDashletCtrl.assertContains(kbNameString, true);
		thirdRecordInDashletCtrl.assertExists(false);

		// Click on "btn btn-invisible next-row" button
		sugar().knowledgeBase.recordView.gotoNextRecord();

		// Verify that Only localization KB records appear in "Other Languages" Dashlet in the current KB record view.  Other KB records should not be appearing
		firstRecordInDashletCtrl.assertContains(kbLang.get(1).get("langLabel"), true);
		secondRecordInDashletCtrl.assertContains(kbNameString, true);
		thirdRecordInDashletCtrl.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}