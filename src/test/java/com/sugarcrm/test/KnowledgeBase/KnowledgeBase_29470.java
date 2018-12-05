package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29470 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Login as an Admin
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to KB > Settings
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");
		// Add a new language - fr | French
		// TODO: VOOD-1762
		new VoodooControl("button", "css", "button[name='add']").click();
		new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(2) div div input").set(customData.get("languageCode"));
		new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(2) div div:nth-child(2) input").set(customData.get("languageLabel"));

		// Save
		new VoodooControl("a", "css", ".fld_main_dropdown [name='save_button']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that list view is loaded correctly after saving of create localization or create revision of KB
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29470_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new KB with any status
		sugar().knowledgeBase.create();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		String kbName = sugar().knowledgeBase.getDefaultData().get("name");
		String kbStatus = sugar().knowledgeBase.getDefaultData().get("status");
		// Verify that after saving KB record, automatically list view appear
		sugar().knowledgeBase.listView.assertVisible(true);
		VoodooControl listRecord1 = sugar().knowledgeBase.listView.getDetailField(1, "name");
		listRecord1.assertContains(kbName, true);

		// In list view, select that KB
		sugar().knowledgeBase.listView.clickRecord(1);

		// Create Localization
		// TODO: VOOD-1760
		new VoodooControl("a", "css", "[data-subpanel-link='localizations'] [name='create_button']").click();

		// Verify that the create form opens with pre-filled the kb name which is the original kb's name. Status is Draft
		VoodooControl createDrawerName = sugar().knowledgeBase.createDrawer.getEditField("name");
		createDrawerName.assertContains(kbName, true);
		VoodooControl createDrawerStatus = sugar().knowledgeBase.createDrawer.getEditField("status");
		createDrawerStatus.assertContains(kbStatus, true);

		// Now you can type any string in the name field to distinguish what it has.
		createDrawerName.set(customData.get("localName"));

		// Click on Save
		sugar().knowledgeBase.createDrawer.save();

		// Verify that success message appears for saved kb record
		sugar().alerts.getSuccess().assertContains(customData.get("alertMessage") + " " + customData.get("localName"), true);
		VoodooUtils.waitForReady();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify that the localization KB records appears in the subpanel view
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "div[data-voodoo-name='subpanel-for-localizations'] .fld_name a").assertEquals(customData.get("localName"), true);

		// Navigate to list view
		sugar().knowledgeBase.navToListView();

		// Verify that the localization KB records appears in the list view
		listRecord1.assertEquals(customData.get("localName"), true);

		// Create Revision
		// TODO: VOOD-1760
		sugar().knowledgeBase.listView.clickRecord(2);
		new VoodooControl("a", "css", "[data-subpanel-link='revisions'] [name='create_button']").click();

		// Verify that the create form opens with pre-filled the kb name which is the original kb's name. Status is Draft
		createDrawerName.assertContains(kbName, true);
		createDrawerStatus.assertContains(kbStatus, true);

		// Now you can type any string in the name field to distinguish what it has.
		createDrawerName.set(customData.get("revisionName"));

		// Click on Save
		sugar().knowledgeBase.createDrawer.save();

		// Verify that a green message bar tells the new kb is saved
		sugar().alerts.getSuccess().assertContains(customData.get("alertMessage") + " " + customData.get("revisionName"), true);
		VoodooUtils.waitForReady();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify that the Revision KB records appears in the subpanel view
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "div[data-voodoo-name='subpanel-for-revisions'] .fld_name a").assertEquals(customData.get("revisionName"), true);

		// Navigate to list view
		sugar().knowledgeBase.navToListView();

		// Verify that the Revision KB records appears in the list view
		listRecord1.assertEquals(customData.get("revisionName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}