package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28808 extends SugarTest {
	DataSource kbLang = new DataSource();
	int langCount = 0;
	VoodooControl langSaveButton,dashboardTitle;

	public void setup() throws Exception {
		kbLang = testData.get(testName);

		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a KB record with English language
		sugar().knowledgeBase.create();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		langCount = kbLang.size();

		// Select the Settings (previously 'Configure') option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Adding three new localization languages 
		// TODO: VOOD-1762 : Need library support for adding/removing languages in Knowledge Base
		VoodooControl addLangBtn = new VoodooControl("button", "css", ".edit.fld_languages button[name='add']");

		for(int i=0; i<langCount; i++){
			addLangBtn.click();
			new VoodooControl("input", "css", ".edit.fld_languages div:nth-child("+((langCount-1)+i)+") input[name='key_languages']").
			set(kbLang.get(i).get("langCode"));
			new VoodooControl("input", "css", ".edit.fld_languages div:nth-child("+((langCount-1)+i)+") div:nth-of-type(2) input[name='value_languages']").
			set(kbLang.get(i).get("langLabel"));
		}

		// Save the languages
		// TODO: VOOD-1762
		langSaveButton = new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']");
		langSaveButton.click();
		VoodooUtils.waitForReady(30000);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Create 3 Localizations with status = Published
		String publishStatus = kbLang.get(0).get("status");
		VoodooControl createLocalizationBtn = new VoodooControl("a", "css", ".list.fld_create_localization_button a");
		VoodooControl kbNameField,kbStatus;
		kbNameField = sugar().knowledgeBase.createDrawer.getEditField("name");
		kbStatus = sugar().knowledgeBase.createDrawer.getEditField("status");
		// TODO: VOOD-1843
		VoodooSelect kbLanguage = new VoodooSelect("span", "css", sugar().knowledgeBase.createDrawer.getEditField("language").getHookString() + ".edit");

		for(int j=0; j<langCount; j++){
			sugar().knowledgeBase.listView.openRowActionDropdown(j+1);
			createLocalizationBtn.click();
			VoodooUtils.waitForReady();
			kbNameField.set(kbLang.get(j).get("langLabel"));
			kbStatus.set(publishStatus);
			sugar().knowledgeBase.createDrawer.showMore();
			kbLanguage.set(kbLang.get(j).get("langLabel"));
			sugar().knowledgeBase.createDrawer.save();
			if (sugar().alerts.getSuccess().queryVisible())
				sugar().alerts.getSuccess().closeAlert();
		}
	}

	/**
	 * Verify that language code appears in "Other Languages" KB Dashlet
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28808_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define controls and get values
		String deDate,itDate,frDate,relativeTimeLocalizationDe,relativeTimeLocalizationIt,relativeTimeLocalizationFr;
		deDate = sugar().knowledgeBase.listView.getDetailField(1, "date_entered_date").getAttribute("data-original-title");
		itDate = sugar().knowledgeBase.listView.getDetailField(2, "date_entered_date").getAttribute("data-original-title");
		frDate = sugar().knowledgeBase.listView.getDetailField(3, "date_entered_date").getAttribute("data-original-title");

		// Open a KB record to its Record view
		sugar().knowledgeBase.listView.clickRecord(4);

		// Switch to My Dashboard in order to view the Other Languages Dashlet
		dashboardTitle = sugar().knowledgeBase.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(kbLang.get(0).get("myDashboard"), true))
			sugar().dashboard.chooseDashboard(kbLang.get(0).get("myDashboard"));

		relativeTimeLocalizationDe = new VoodooControl("a", "css", ".listed.kbcontents li time").getAttribute("title");
		relativeTimeLocalizationIt = new VoodooControl("a", "css", ".listed.kbcontents li:nth-child(2) time").getAttribute("title");
		relativeTimeLocalizationFr = new VoodooControl("a", "css", ".listed.kbcontents li:nth-child(3) time").getAttribute("title");

		// In Other Languages Dashlet, Assert the name, createdBy, languageCode and relative dateTime of the 
		// localizations created
		// TODO: VOOD-670 - More Dashlet Support
		for(int k=0; k<langCount; k++){
			// Assert KB name
			new VoodooControl("a", "css", ".listed.kbcontents li:nth-child("+(k+1)+") a").
			assertEquals(kbLang.get((langCount-1)-k).get("langLabel"), true);

			// Assert Created By 
			new VoodooControl("a", "css", ".listed.kbcontents li:nth-child("+(k+1)+") div").
			assertContains(kbLang.get(0).get("createdBy"), true);

			// Assert Language code
			new VoodooControl("span", "css", ".listed.kbcontents li:nth-child("+(k+1)+") span").
			assertEquals(kbLang.get((langCount-1)-k).get("langCode"), true);
		}

		// Assert the relative Date Time
		Assert.assertTrue("The relative time for Deutsch is incorrect!",relativeTimeLocalizationDe.equals(deDate));
		Assert.assertTrue("The relative time for Italian is incorrect!",relativeTimeLocalizationIt.equals(itDate));
		Assert.assertTrue("The relative time for French is incorrect!",relativeTimeLocalizationFr.equals(frDate));		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}