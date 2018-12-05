package com.sugarcrm.test.KnowledgeBase;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29626 extends SugarTest {
	DataSource kbData = new DataSource();

	public void setup() throws Exception {
		sugar().login();
		kbData = testData.get(testName);

		// Enable Knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Select the Settings option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Adding one new localization language 
		// TODO: VOOD-1762
		new VoodooControl("button", "css", ".edit.fld_languages button[name='add']").click();
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) input[name='key_languages']").set(kbData.get(0).get("langCode"));
		new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) div:nth-of-type(2) input[name='value_languages']").set(kbData.get(0).get("langLabel"));
		new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']").click();
		VoodooUtils.waitForReady(30000);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that frequency is increased correctly when create Localization and Revision in KB
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29626_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create KB article
		sugar().knowledgeBase.create();

		// Verify "View Count" equals 1 in listView
		sugar().knowledgeBase.listView.getDetailField(1, "viewCount").assertEquals(kbData.get(0).get("viewCount"), true);

		// Click Create revision
		// TODO: VOOD-1760
		sugar().knowledgeBase.listView.openRowActionDropdown(1);
		new VoodooControl("a", "css", ".fld_create_revision_button a").click();

		// Edit the name field
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbData.get(0).get("revision"));
		sugar().knowledgeBase.createDrawer.save();

		// Verify View count is increased by one i.e "viewCount" becomes two.
		sugar().knowledgeBase.listView.getDetailField(1, "viewCount").assertEquals(kbData.get(1).get("viewCount"), true);

		// Create localization
		// TODO: VOOD-1760
		sugar().knowledgeBase.listView.openRowActionDropdown(1);
		new VoodooControl("a", "css", ".fld_create_localization_button a").click();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbData.get(0).get("localization"));
		sugar().knowledgeBase.createDrawer.save();

		// Verify View count is equal to 3
		sugar().knowledgeBase.listView.getDetailField(1, "viewCount").assertEquals(kbData.get(2).get("viewCount"), true);

		// Create a KB record 
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// Verify View count is equal to 1
		sugar().knowledgeBase.listView.getDetailField(1, "viewCount").assertEquals(kbData.get(0).get("viewCount"), true);

		// Verify the View count on Record View
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.getDetailField("viewCount").assertEquals(kbData.get(1).get("viewCount"), true);

		// Create Revision from revisions subpanel of KB record
		new VoodooControl("a", "css",".panel-top-for-revisions .fld_create_button a").click();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbData.get(0).get("name"));
		sugar().knowledgeBase.createDrawer.save();

		// Verify the View count equal to 3 after the name has been changed above
		sugar().knowledgeBase.recordView.getDetailField("viewCount").assertEquals(kbData.get(2).get("viewCount"), true);

		// Navigate to List View and verify the View Count equal to 3
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.getDetailField(1,"viewCount").assertEquals(kbData.get(2).get("viewCount"), true);

		// Create Localization from Subpanels of KB record
		sugar().knowledgeBase.listView.clickRecord(1);
		new VoodooControl("a", "css",".panel-top-for-localizations .fld_create_button a").click();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(kbData.get(1).get("name"));
		sugar().knowledgeBase.createDrawer.save();
		sugar().knowledgeBase.navToListView();

		// Verify View count is equal to 5
		sugar().knowledgeBase.listView.getDetailField(1, "viewCount").assertEquals(kbData.get(3).get("viewCount"), true);
		sugar().knowledgeBase.listView.getDetailField(2, "viewCount").assertEquals(kbData.get(3).get("viewCount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}