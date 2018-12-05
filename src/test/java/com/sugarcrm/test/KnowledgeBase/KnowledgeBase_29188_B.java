package com.sugarcrm.test.KnowledgeBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29188_B extends SugarTest {
	DataSource kbData = new DataSource();
	VoodooControl kbNameCtrl, kbStatusCtrl, kbPublishedDateCtrl, publishedDateErrorCtrl, tooltipCtrl;
	String todaysDate, pastDayDate;

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		kbData = testData.get(testName);
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Adding two new localization languages 
		// TODO: VOOD-1762
		VoodooControl addLangBtn = new VoodooControl("button", "css", ".edit.fld_languages button[name='add']");
		VoodooControl langSaveButton = new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']");

		// Select the Settings option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		for(int i = 0; i < 2; i++){
			addLangBtn.click();
			new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(" + (2 + i) + ") input[name='key_languages']").set(kbData.get(i).get("langCode"));
			new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(" + (2 + i) + ") div:nth-of-type(2) input[name='value_languages']").set(kbData.get(i).get("langLabel"));
		}

		// Save the languages
		langSaveButton.click();
		VoodooUtils.waitForReady(30000); // Needed extra wait
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Past Day date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = sdf.format(date);
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -2);
		date = calendar.getTime();
		pastDayDate = sdf.format(date);

		// Define Controls for KB 
		kbNameCtrl = sugar().knowledgeBase.createDrawer.getEditField("name");
		kbStatusCtrl = sugar().knowledgeBase.createDrawer.getEditField("status");
		kbPublishedDateCtrl = sugar().knowledgeBase.createDrawer.getEditField("date_publish");
		// TODO: VOOD-1445 and VOOD-1292
		publishedDateErrorCtrl = new VoodooControl("span", "css", ".fld_active_date.edit");
		tooltipCtrl = new VoodooControl("span", "css", ".error-tooltip.add-on");
	}

	// Fill in required data in KB create drawer and Save the record
	private void createKbWithStatusAndSaveRecord(int nameIndex, int statusIndex) throws Exception {
		kbNameCtrl.set(kbData.get(nameIndex).get("name"));
		kbStatusCtrl.set(kbData.get(statusIndex).get("status"));
		kbPublishedDateCtrl.set(pastDayDate);
		sugar().knowledgeBase.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	// Fill in required data in KB create drawer and click on the save
	private void createKbWithStatusAndClickSave(int nameIndex, int statusIndex) throws Exception {
		kbNameCtrl.set(kbData.get(nameIndex).get("name"));
		kbStatusCtrl.set(kbData.get(statusIndex).get("status"));
		kbPublishedDateCtrl.set(pastDayDate);
		sugar().knowledgeBase.createDrawer.getControl("saveButton").click();
	}

	// For Approved Status -> Article should not be saved. User should be warned about invalid Publish Date
	private void approvedKBVerification() throws Exception {
		sugar().alerts.getError().closeAlert();
		publishedDateErrorCtrl.assertAttribute("class", kbData.get(0).get("error"), true);
		tooltipCtrl.assertAttribute("data-original-title", kbData.get(0).get("errorText"));

		// Cancel the KB create drawer
		sugar().knowledgeBase.createDrawer.cancel();
	}

	// For Published Status -> Publish Date should change to today's date when you save
	private void publishedKBDateVerification() throws Exception {
		VoodooUtils.pause(100); // Wait needed for verification of Publish Date 
		kbPublishedDateCtrl.assertContains(todaysDate, true);
		VoodooUtils.waitForReady();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that Published Date field has correct validations
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29188_B_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls for Localization and Revision sub-panel
		// TODO: VOOD-1760
		VoodooControl createLocalizationsCtrl = new VoodooControl("a", "css", "[data-subpanel-link='localizations'] [name='create_button']");
		VoodooControl createRevisionsCtrl = new VoodooControl("a", "css", "[data-subpanel-link='revisions'] [name='create_button']");
		VoodooControl localizationFirstRecordCtrl = new VoodooControl("a", "css", "div[data-subpanel-link='localizations'] .fld_name a");
		VoodooControl revisionsFirstRecordCtrl = new VoodooControl("a", "css", "div[data-subpanel-link='revisions'] .fld_name a");
		VoodooControl localizationFirstRecordStatusCtrl = new VoodooControl("a", "css", "div[data-subpanel-link='localizations'] .fld_status span");
		VoodooControl revisionsFirstRecordStatusCtrl = new VoodooControl("a", "css", "div[data-subpanel-link='revisions'] .fld_status span");

		// For Status = Draft
		// Go to KB module -> Open any KB record view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);

		// In Localizations sub-panel, click "+" icon to add an article
		createLocalizationsCtrl.click();
		VoodooUtils.waitForReady();

		// Fill in required data, Set 'Publish Date' with the date in the past and Draft Status -> Save
		createKbWithStatusAndSaveRecord(0, 0);

		// For Draft Status -> Article should be saved. User should not be warned about invalid Publish Date
		sugar().alerts.getWarning().assertExists(false);
		localizationFirstRecordCtrl.assertEquals(kbData.get(0).get("name"), true);
		localizationFirstRecordStatusCtrl.assertEquals(kbData.get(0).get("status"), true);

		// In Revision sub-panel, click "+" icon to add an article
		createRevisionsCtrl.click();
		VoodooUtils.waitForReady();

		// Fill in required data, Set 'Publish Date' with the date in the past and Draft Status -> Save
		createKbWithStatusAndSaveRecord(1, 0);

		// For Draft Status -> Article should be saved. User should not be warned about invalid Publish Date
		sugar().alerts.getWarning().assertExists(false);
		revisionsFirstRecordCtrl.assertEquals(kbData.get(1).get("name"), true);
		revisionsFirstRecordStatusCtrl.assertEquals(kbData.get(0).get("status"), true);

		// For Status = Approved
		// In Localizations sub-panel, click "+" icon to add an article
		createLocalizationsCtrl.click();
		VoodooUtils.waitForReady();

		// Fill in required data, Set 'Publish Date' with the date in the past and Status Approved -> Save
		createKbWithStatusAndClickSave(2, 1);

		// For Approved Status -> Article should not be saved. User should be warned about invalid Publish Date
		approvedKBVerification();

		// In revision sub-panel, click "+" icon to add an article
		createRevisionsCtrl.click();
		VoodooUtils.waitForReady();

		// Fill in required data, Set 'Publish Date' with the date in the past and Approved Status -> Save
		createKbWithStatusAndClickSave(3, 1);

		// For Approved Status -> Article should not be saved. User should be warned about invalid Publish Date
		approvedKBVerification();

		// For Status = Published
		// In Localization sub-panel, click "+" icon to add an article
		createLocalizationsCtrl.click();
		VoodooUtils.waitForReady();

		// Fill in required data, Set 'Publish Date' with the date in the past and Status Published -> Save
		createKbWithStatusAndClickSave(4, 2);

		// For Published Status -> Publish Date should change to today's date when you save
		publishedKBDateVerification();
		localizationFirstRecordCtrl.assertEquals(kbData.get(4).get("name"), true);
		localizationFirstRecordStatusCtrl.assertEquals(kbData.get(2).get("status"), true);

		// In Revision sub-panel, click "+" icon to add an article
		createRevisionsCtrl.click();
		VoodooUtils.waitForReady();

		// Fill in required data, Set 'Publish Date' with the date in the past and Published Status -> Save
		createKbWithStatusAndClickSave(5, 2);

		// For Published Status -> Publish Date should change to today's date when you save
		publishedKBDateVerification();
		revisionsFirstRecordCtrl.assertEquals(kbData.get(5).get("name"), true);
		revisionsFirstRecordStatusCtrl.assertEquals(kbData.get(2).get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 