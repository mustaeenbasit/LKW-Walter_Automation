package com.sugarcrm.test.KnowledgeBase;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30652 extends SugarTest {
	FieldSet kbData = new FieldSet();
	VoodooControl langSaveButton;

	public void setup() throws Exception {
		kbData = testData.get(testName).get(0);
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Select the Settings option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Adding one new localization language 
		// TODO: VOOD-1762
		langSaveButton = new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']");
		new VoodooControl("button", "css", ".edit.fld_languages button[name='add']").click();
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) input[name='key_languages']").set(kbData.get("langCode"));
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) div:nth-of-type(2) input[name='value_languages']").set(kbData.get("langLabel"));

		// Save the language
		langSaveButton.click();
		VoodooUtils.waitForReady(30000);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that New localization in Approved status should not be created with empty Publish Date
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30652_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set next day date
		Date date = new Date();
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		int nextDayDate = calendar.get(Calendar.DAY_OF_MONTH);

		// Define Controls for KB create drawer
		VoodooControl kbStatus = sugar().knowledgeBase.createDrawer.getEditField("status");

		// Edit the KB a Publish KB, Fill in required data, Expiration Date
		// TODO: VOOD-444
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.recordView.showMore();
		kbStatus.set(kbData.get("status"));
		sugar().knowledgeBase.recordView.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Click Create Localization, Set the status of the localization to Approved. Delete Publish Date (if exists).
		// TODO: VOOD-1760
		new VoodooControl("a", "css", "[data-subpanel-link='localizations'] [name='create_button']").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.createDrawer.showMore();
		kbStatus.set(kbData.get("approvedStatus"));
		sugar().knowledgeBase.createDrawer.getEditField("date_publish").set("");

		// Save localization
		sugar().knowledgeBase.createDrawer.getControl("saveButton").click();

		// A yellow warning message bar appears and asking "Schedule this article to be published by specifying the Publish Date. Do you wish to continue without entering a Publish Date?"
		Alert warning = sugar().alerts.getWarning();
		warning.assertContains(kbData.get("warningMessage"), true);
		warning.cancelAlert();

		// Verify that the Calendar appears allow to select which date
		// TODO: VOOD-910 
		new VoodooControl("div", "css", ".layout_KBContents.active .datepicker.dropdown-menu").assertVisible(true);

		// Select tomorrow's date
		// Need to select next date therefore using xPath 
		new VoodooControl("td", "xPath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div[1]/table/tbody/tr/td[not(contains(@class, 'old')) and text()='" + nextDayDate + "']").click();

		// Click on Save
		sugar().knowledgeBase.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify that the valid localization KB has been saved correctly
		// TODO: VOOD-1760
		new VoodooControl("a", "css", "div[data-subpanel-link='localizations'] .fld_name a").assertEquals(sugar().knowledgeBase.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 